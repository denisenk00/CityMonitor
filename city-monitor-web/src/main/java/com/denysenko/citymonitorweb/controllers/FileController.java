package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.entity.FileService;
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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('appeals:read', 'quizzes:read')")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id){
        File file = fileService.getFileByID(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null
                || (file.getAppealId() != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("appeals:read")))
                || (file.getQuizId() != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("quizzes:read")))){
            throw new AccessDeniedException("You don't have permission to save this file");
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
