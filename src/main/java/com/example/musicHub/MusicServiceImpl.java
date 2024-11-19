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
            case "views":
                musics = musics.stream()
                        .sorted(Comparator.comparingLong(MusicEntity::getViews).reversed())
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

        return musics.stream().map(Utils::toDTO).collect(Collectors.toList());
    }
}
