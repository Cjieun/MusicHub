package com.example.musicHub;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "music")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    private String title;
    private String artist;

    @Column(name = "album_name")
    private String albumName;

    private String genre;

    @Column(name = "release_date")
    private String releaseDate;

    private long views;

    private String image;

    private int playlistCount;
}
