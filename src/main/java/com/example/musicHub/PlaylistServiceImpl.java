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
                            .max(Comparator.comparing(MusicEntity::getReleaseDate))
                            .map(MusicEntity::getImage)
                            .orElse("/img/default-image.jpg"); // 이미지가 없으면 기본 이미지 사용
                    dto.setRecentImage(recentImage);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PlaylistDTO findById(long id) {
        PlaylistEntity playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid playlist ID"));

        playlist.setViews(playlist.getViews() + 1);
        playlistRepository.save(playlist);

        PlaylistDTO dto = Utils.toDTO(playlist);

        // 수록된 음악 조회
        List<MusicEntity> musicEntities = playlist.getMusics().stream()
                .sorted(Comparator.comparing(MusicEntity::getReleaseDate)) // 최신 추가 순 정렬
                .collect(Collectors.toList());
        List<MusicDTO> musics = musicEntities.stream()
                .map(Utils::toDTO)
                .collect(Collectors.toList());
        dto.setMusics(musics);

        String genres = musicEntities.stream()
                .map(MusicEntity::getGenre)
                .distinct()
                .collect(Collectors.joining(" / "));
        dto.setGenres(genres);

        String recentImage = musicEntities.stream()
                .max(Comparator.comparing(MusicEntity::getReleaseDate))
                .map(MusicEntity::getImage)
                .orElse("/img/default-image.jpg"); // 이미지가 없으면 기본값 사용
        dto.setRecentImage(recentImage);

        return dto;
    }

    @Override
    public void save(PlaylistDTO playlistDTO, long userId, List<Long> musicIds) {
        // 사용자 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // DTO -> Entity 변환
        PlaylistEntity playlistEntity = Utils.toEntity(playlistDTO, user);

        // 선택된 음악 ID로 음악 조회
        List<MusicEntity> selectedMusics = (musicIds != null && !musicIds.isEmpty())
                ? musicRepository.findAllByIdxIn(musicIds) // musicIds를 기준으로 음악 조회
                : new ArrayList<>(); // musicIds가 없으면 빈 리스트

        // 음악 리스트를 플레이리스트에 설정
        playlistEntity.setMusics(selectedMusics);

        // 플레이리스트 저장
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

    @Override
    public void deleteById(long id) {
        playlistRepository.deleteById(id);
    }

    @Override
    public void removeMusicFromPlaylist(long playlistId, long musicId) {
        // 해당 플레이리스트 조회
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid playlist ID"));

        // 해당 곡 제거
        playlist.setMusics(
                playlist.getMusics().stream()
                        .filter(music -> music.getIdx() != musicId) // musicId가 아닌 것만 남김
                        .collect(Collectors.toList())
        );

        // 업데이트된 플레이리스트 저장
        playlistRepository.save(playlist);
    }

    @Override
    public void updatePlaylist(PlaylistDTO playlistDTO, List<Long> musicIds) {
        PlaylistEntity playlist = playlistRepository.findById(playlistDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid playlist ID"));

        // 제목과 설명 업데이트
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());

        // 선택된 음악 업데이트
        List<MusicEntity> selectedMusics = (musicIds != null && !musicIds.isEmpty())
                ? musicRepository.findAllById(musicIds)
                : new ArrayList<>();
        playlist.setMusics(selectedMusics);

        playlistRepository.save(playlist); // 저장
    }

}
