package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
