package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private  MusicRepository musicRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PlaylistDTO> findAllByUserId(long userId) {
        return playlistRepository.findAllByUserId(userId).stream()
                .map(playlist -> {
                    PlaylistDTO dto = Utils.toDTO(playlist);

                    // 음악 리스트 가져오기
                    List<MusicEntity> musicEntities = playlist.getMusics();

                    // 장르 최대 2개 추출
                    String genres = musicEntities.stream()
                            .map(MusicEntity::getGenre)
                            .distinct()
                            .limit(2) // 최대 2개
                            .collect(Collectors.joining(", "));
                    dto.setGenres(genres);

                    // 수록곡 수
                    dto.setSongCount(musicEntities.size());

                    // 가장 최근에 추가된 음악 2개 추출
                    List<MusicDTO> recentSongs = musicEntities.stream()
                            .sorted(Comparator.comparing(MusicEntity::getReleaseDate)) // 최신순 정렬
                            .limit(2) // 최대 2개
                            .map(Utils::toDTO) // DTO로 변환
                            .collect(Collectors.toList());
                    dto.setRecentSongs(recentSongs);

                    // 가장 최근 음악의 이미지 추출
                    String recentImage = musicEntities.stream()
                            .max(Comparator.comparing(MusicEntity::getReleaseDate).reversed())
                            .map(MusicEntity::getImage)
                            .orElse("/img/default-image.jpg"); // 이미지가 없으면 기본 이미지 사용
                    dto.setRecentImage(recentImage);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PlaylistDTO findById(long id) {
        return playlistRepository.findById(id)
                .map(Utils::toDTO)
                .orElse(null);
    }

    @Override
    public void save(PlaylistDTO playlistDTO, long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        PlaylistEntity playlistEntity = Utils.toEntity(playlistDTO, user);
        playlistRepository.save(playlistEntity);
    }

    @Override
    public void addMusicToPlaylist(long playlistId, long musicId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid playlist ID"));
        MusicEntity music = musicRepository.findById(musicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid music ID"));

        playlist.getMusics().add(music); // ManyToMany 관계 업데이트
        playlistRepository.save(playlist);
    }
}
