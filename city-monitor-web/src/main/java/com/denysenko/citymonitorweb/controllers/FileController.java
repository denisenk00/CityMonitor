package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.services.entity.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") Long id){
        File file = fileService.getIOFileByID(id);
        System.out.println("File name = " + file.getName());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getContent()));
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.getContent().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
