package com.denysenko.citymonitorweb.models.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileDTO implements MultipartFile {
    private Long id;
    private String name;
    private byte[] content;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes(){
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(content);
    }
}
