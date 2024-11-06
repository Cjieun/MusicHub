package com.example.musicHub;

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
}
