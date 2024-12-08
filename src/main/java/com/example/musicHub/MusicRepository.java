package com.example.musicHub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Long> {

    // 장르별 음악 검색
    List<MusicEntity> findByGenre(String genre);

    // 특정 ID 목록에 해당하는 음악 검색
    List<MusicEntity> findAllByIdxIn(List<Long> idxs);

    // MBTI 기반 추천 음악 검색
    @Query("SELECT new com.example.musicHub.MusicDTO(" +
            "m.idx, m.title, m.artist, m.albumName, m.genre, m.releaseDate, m.views, m.image, m.mp3Path, COUNT(l)) " +
            "FROM MusicEntity m JOIN LikeEntity l ON m.idx = l.music.idx " +
            "WHERE l.mbti = :mbti " +
            "GROUP BY m.idx, m.title, m.artist, m.albumName, m.genre, m.releaseDate, m.views, m.image, m.mp3Path " +
            "ORDER BY COUNT(l) DESC")
    List<MusicDTO> findTopLikedMusicsByMbti(@Param("mbti") String mbti);
}
