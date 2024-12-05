package com.example.musicHub;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService{
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Override
    @Transactional
    public boolean toggleLike(long userId, long musicId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        MusicEntity music = musicRepository.findById(musicId).orElseThrow();

        Optional<LikeEntity> existingLike = likeRepository.findByUserAndMusic(user, music);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            music.setLikeCount(music.getLikeCount() - 1); // 좋아요 수 감소
            musicRepository.save(music);
            return false; // 좋아요 삭제
        } else {
            LikeEntity like = LikeEntity.builder().user(user).music(music).build();
            likeRepository.save(like);
            music.setLikeCount(music.getLikeCount() + 1); // 좋아요 수 증가
            musicRepository.save(music);
            return true; // 좋아요 추가
        }
    }

    @Override
    public boolean isLiked(long userId, long musicId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        MusicEntity music = musicRepository.findById(musicId).orElseThrow();
        return likeRepository.findByUserAndMusic(user, music).isPresent();
    }
}
