package com.example.musicHub;

import java.util.List;

public interface MusicService {
    List<MusicDTO> findAll();
    MusicDTO findById(long idx);
    void save(MusicDTO music);
    void deleteById(long idx);
    List<MusicEntity> search(String query);
    List<MusicDTO> findByGenre(String genre);
    List<MusicDTO> getSortedMusics(String sortBy);
    void incrementViews(long idx);
    List<MusicDTO> getRecommendationsForMbti(String mbti);
}
