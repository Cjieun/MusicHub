package com.example.musicHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    private long id;
    private String name;
    private String description;
    private String createdAt;
    private long userId;
    private List<MusicDTO> musics;

    private String genres; // 최대 2개의 장르를 저장
    private int songCount; // 총 음악 개수
    private List<MusicDTO> recentSongs; // 최신 2곡
    private String recentImage;
}
