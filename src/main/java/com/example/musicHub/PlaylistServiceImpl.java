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
                .map(Utils::toDTO)
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
