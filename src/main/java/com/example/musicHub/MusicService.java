package com.example.musicHub;

import java.util.List;

public interface MusicService {
    List<MusicDTO> findAll();
    MusicDTO findById(long idx);
    void save(MusicDTO music);
    void deleteById(long idx);
    List<MusicEntity> search(String query);
}
