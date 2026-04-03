package com.example.qamanager.domain.plan.controller;

import com.example.qamanager.domain.plan.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        uploadService.upload(file);
        return "업로드 완료";
    }
}