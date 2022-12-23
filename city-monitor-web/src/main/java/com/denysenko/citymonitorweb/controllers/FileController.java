package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.entity.FileService;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private TelegramService telegramService;

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('quizzes:read')")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id){
        File file = fileService.getFileByID(id);

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
    public ResponseEntity<byte[]> downloadTelegramFile(@PathVariable(name = "id") String tgFileId) {
        java.io.File file = null;

        byte[] resource = null;
        try {
            file = telegramService.getFileByID(tgFileId);
            resource = new FileInputStream(file).readAllBytes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
