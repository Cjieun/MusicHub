package com.example.musicHub;

import java.util.List;

public interface PlaylistService {
    List<PlaylistDTO> findAllByUserId(long userId);
    PlaylistDTO findById(long id);
    void save(PlaylistDTO playlistDTO, long userId);
    void addMusicToPlaylist(long playlistId, long musicId);
}
