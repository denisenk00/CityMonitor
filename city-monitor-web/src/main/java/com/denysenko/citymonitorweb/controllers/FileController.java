package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.exceptions.DownloadTelegramFileException;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.entity.FileService;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final TelegramService telegramService;

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id) {
        File file;
        try {
            file = fileService.getFileByID(id);
        } catch (EntityNotFoundException e) {
            throw new InputValidationException(e.getMessage(), e);
        }

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getContent()));

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName(), StandardCharsets.UTF_8)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.getContent().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/tgFile/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('appeals:read')")
    public ResponseEntity<byte[]> downloadFileFromTelegram(@PathVariable(name = "id") String tgFileId) {
        FileInputStream fileContent;
        File file;
        byte[] resource;

        try {
            file = fileService.getFileByTgFileId(tgFileId);
            fileContent = telegramService.getFileByID(tgFileId);
            resource = fileContent.readAllBytes();
        } catch (EntityNotFoundException e) {
            throw new InputValidationException(e.getMessage(), e);
        } catch (TelegramApiException e) {
            String reason = "Помилка при надсиланні запиту на завантаження файлу до Telegram API";
            throw new DownloadTelegramFileException(reason, e, tgFileId);
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось завантажити вміст файлу", e);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName(), StandardCharsets.UTF_8)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(resource.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
