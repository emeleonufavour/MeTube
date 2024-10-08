package com.example.MeTube.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.MeTube.model.Video;
import com.example.MeTube.repository.VideoRepository;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final Path rootLocation = Paths.get("uploads");

    public VideoService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public Video uploadVideo(MultipartFile file, String title, String description, String userId) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));

            Video video = new Video();

            video.setTitle(title);
            video.setDescription(description);
            video.setUrl("/videos/" + fileName);
            video.setUploadDateTime(LocalDateTime.now());
            video.setUserId(userId);

            return videoRepository.save(video);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Resource loadVideo(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = resourceLoader.getResource("file:" + file.toString());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + fileName);
        }
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}
