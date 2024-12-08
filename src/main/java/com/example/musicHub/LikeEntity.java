package com.example.musicHub;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // 사용자 정보
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "music_id") // 음악 정보
    private MusicEntity music;

    @Column(name = "mbti", length = 4, nullable = true) // MBTI 정보
    private String mbti;
}
