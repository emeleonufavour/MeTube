package com.example.MeTube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MeTube.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}