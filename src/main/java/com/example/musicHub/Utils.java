package com.example.musicHub;

import java.time.format.DateTimeFormatter;
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
                .build();
    }

    public static UserDTO toDTO(UserEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword()) // 비밀번호는 필요에 따라 제거 가능
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
                .build();
    }

    public static PlaylistEntity toEntity(PlaylistDTO dto, UserEntity user) {
        return PlaylistEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .musics(dto.getMusics().stream().map(Utils::toEntity).collect(Collectors.toList()))
                .build();
    }
}