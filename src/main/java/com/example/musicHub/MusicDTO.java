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
}
