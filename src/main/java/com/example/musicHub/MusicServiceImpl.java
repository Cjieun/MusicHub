package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MusicServiceImpl implements MusicService  {

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private LikeService likeService;

    @Override
    public List<MusicDTO> findAll() {

        List<MusicDTO> list = new ArrayList<>();

        return musicRepository.findAll().stream()
                .map(v ->  Utils.toDTO(v))
                .collect(Collectors.toList());

    }

    @Override
    public MusicDTO findById(long idx) {
        return musicRepository.findById(idx)
                .map(v ->  Utils.toDTO(v))
                .orElse(null);
    }

    @Override
    public void save(MusicDTO musicDTO) {
        MusicEntity music =  Utils.toEntity(musicDTO);
        musicRepository.save(music);
    }

    @Override
    public void deleteById(long idx) {
        musicRepository.deleteById(idx);
    }

    @Override
    public List<MusicEntity> search(String query) {
        return musicRepository.findAll().stream()
                .filter(music -> music.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        music.getArtist().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicDTO> findByGenre(String genre) {
        return musicRepository.findByGenre(genre).stream()
                .map(Utils::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicDTO> getSortedMusics(String sortBy) {
        List<MusicEntity> musics = musicRepository.findAll();
        long currentUserId = 1;

        switch (sortBy) {
            case "releaseDate":
                musics = musics.stream()
                        .sorted(Comparator.comparing(MusicEntity::getReleaseDate).reversed())
                        .collect(Collectors.toList());
                break;

            case "releaseDateAsc":
                musics = musics.stream()
                        .sorted(Comparator.comparing(MusicEntity::getReleaseDate)) // 오래된 순
                        .collect(Collectors.toList());
                break;
            case "likeCount":
                musics = musics.stream()
                        .sorted(Comparator.comparingLong(MusicEntity::getLikeCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "playlistCount":
                musics = musics.stream()
                        .sorted(Comparator.comparingInt(MusicEntity::getPlaylistCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "idx":
            default:
                musics = musics.stream()
                        .sorted(Comparator.comparingLong(MusicEntity::getIdx).reversed())
                        .collect(Collectors.toList());
                break;
        }

        return musics.stream().map(music -> {
            MusicDTO musicDTO = Utils.toDTO(music);
            musicDTO.setLiked(likeService.isLiked(currentUserId, music.getIdx()));
            return musicDTO;
        }).collect(Collectors.toList());
    }
    public List<MusicDTO> getRecommendationsForMbti(String mbti) {
        List<String> recommendedGenres = getRecommendedGenresForMbti(mbti);

        // 필터링하여 추천 음악을 반환
        return musicRepository.findAll().stream()
                .filter(music -> recommendedGenres.contains(music.getGenre()))
                .map(Utils::toDTO)
                .collect(Collectors.toList());
    }

    private List<String> getRecommendedGenresForMbti(String mbti) {
        switch (mbti) {
            case "INTJ":
                return List.of("클래식", "인디음악");
            case "ENFP":
                return List.of("팝", "댄스");
            case "ISFJ":
                return List.of("발라드", "R&B/Soul");
            case "ESTP":
                return List.of("힙합", "록/메탈");
            default:
                return List.of("팝", "발라드"); // 기본 추천 장르
        }
    }
    @Override
    public String getAudioPathById(long idx) {
        return musicRepository.findById(idx)
                .map(MusicEntity::getMp3Path)
                .orElse(null);
    }

    @Override
    public void incrementViews(long idx) {
        musicRepository.findById(idx).ifPresent(music -> {
            music.setViews(music.getViews() + 1);
            musicRepository.save(music);
        });
    }
}
