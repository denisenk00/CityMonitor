package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.entity.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

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



}
