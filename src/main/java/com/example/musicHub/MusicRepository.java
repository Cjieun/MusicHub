package com.example.musicHub;

import com.example.musicHub.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<MusicEntity, Long> {
    List<MusicEntity> findByGenre(String genre);
}