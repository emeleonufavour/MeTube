package com.example.MeTube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.MeTube.model.Video;
import com.example.MeTube.service.VideoService;

@RestController
@RequestMapping("/api/v1")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadVideo(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        try {
            Video savedVideo = videoService.uploadVideo(file, title, description, userId);
            return ResponseEntity.ok().body(savedVideo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload video: " + e.getMessage());
        }
    }

    @GetMapping("/stream/{filename}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String filename) {
        Resource video = videoService.loadVideo(filename);
        return ResponseEntity.ok().header("Content-Type", "video/mp4").body(video);
    }

    @GetMapping("/videos")
    public ResponseEntity<?> getAllVideos() {
        return ResponseEntity.ok().body(videoService.getAllVideos());
    }

}
