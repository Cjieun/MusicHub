package com.example.musicHub;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    List<PlaylistEntity> findAllByUserId(long userId);
}
