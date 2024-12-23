package com.example.musicHub;

import java.util.List;
import java.util.Map;

public interface PlaylistService {
    List<PlaylistDTO> findAllByUserId(long userId);
    PlaylistDTO findById(long id);
    void save(PlaylistDTO playlistDTO, long userId, List<Long> musicIds);
    void addMusicToPlaylist(long playlistId, long musicId);
    void deleteById(long id);
    void removeMusicFromPlaylist(long playlistId, long musicId);
    void updatePlaylist(PlaylistDTO playlistDTO, List<Long> musicIds);

}


