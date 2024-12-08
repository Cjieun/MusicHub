package com.example.musicHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicDTO {
    private long idx;
    private String title;
    private String artist;
    private String albumName;
    private String genre;
    private String releaseDate;
    private long views;
    private String image;
    private int playlistCount;
    private String mp3Path;
    private long likeCount;
    private boolean isLiked;

    // JPQL 쿼리를 위한 생성자
    public MusicDTO(long idx, String title, String artist, String albumName, String genre, String releaseDate,
                    long views, String image, String mp3Path, long likeCount) {
        this.idx = idx;
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.views = views;
        this.image = image;
        this.mp3Path = mp3Path;
        this.likeCount = likeCount;
    }
}
