package com.example.musicHub;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Utils {
    public static  MusicDTO toDTO(MusicEntity entity) {
        return MusicDTO.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .artist(entity.getArtist())
                .albumName(entity.getAlbumName())
                .genre(entity.getGenre())
                .releaseDate(entity.getReleaseDate())
                .views(entity.getViews())
                .image(entity.getImage())
                .mp3Path(entity.getMp3Path())
                .likeCount(entity.getLikeCount())
                .build();
    }
    public static MusicEntity toEntity(MusicDTO dto) {
        return MusicEntity.builder()
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .artist(dto.getArtist())
                .albumName(dto.getAlbumName())
                .genre(dto.getGenre())
                .releaseDate(dto.getReleaseDate())
                .views(dto.getViews())
                .image(dto.getImage())
                .mp3Path(dto.getMp3Path())
                .likeCount(dto.getLikeCount())
                .build();
    }

    public static UserDTO toDTO(UserEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword()) // 비밀번호는 필요에 따라 제거 가능
                .mbti(entity.getMbti())
                .createdAt(entity.getCreatedAt().format(formatter))
                .playlists(entity.getPlaylists().stream().map(Utils::toDTO).toList())
                .build();
    }

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .mbti(dto.getMbti())
                .build(); // createdAt은 자동 처리
    }

    public static PlaylistDTO toDTO(PlaylistEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return PlaylistDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt().format(formatter))
                .userId(entity.getUser().getId())
                .musics(entity.getMusics().stream().map(Utils::toDTO).collect(Collectors.toList()))
                .views(entity.getViews())
                .build();
    }

    public static PlaylistEntity toEntity(PlaylistDTO dto, UserEntity user) {
        return PlaylistEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .musics(dto.getMusics() != null ?
                        dto.getMusics().stream()
                                .map(Utils::toEntity) // MusicDTO -> MusicEntity 변환
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .views(dto.getViews())
                .build();
    }

    public static LikeDTO toDTO(LikeEntity entity) {
        return LikeDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .musicId(entity.getMusic().getIdx())
                .build();
    }

    public static LikeEntity toEntity(LikeDTO dto, UserEntity user, MusicEntity music) {
        return LikeEntity.builder()
                .id(dto.getId())
                .user(user)
                .music(music)
                .build();
    }
}