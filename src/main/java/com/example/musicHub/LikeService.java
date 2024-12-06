package com.example.musicHub;

import java.util.List;

public interface LikeService {
    boolean toggleLike(long userId, long musicId);
    boolean isLiked(long userId, long musicId);
}
