package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    private final long currentUserId = 1; // 현재 유저 ID 가정

    @GetMapping
    public String list(Model model) {
        List<PlaylistDTO> playlists = playlistService.findAllByUserId(currentUserId);
        model.addAttribute("playlists", playlists);
        model.addAttribute("playlistCount", playlists.size());
        return "playlist/list";
    }

    @GetMapping("/select")
    public String selectPlaylist(@RequestParam long musicId, Model model) {
        model.addAttribute("playlists", playlistService.findAllByUserId(currentUserId)); // 현재 유저의 플레이리스트
        model.addAttribute("musicId", musicId); // 선택한 음악 ID 전달
        return "playlist/select";
    }

    @PostMapping("/addMusic")
    public String addMusicToPlaylist(@RequestParam long musicId, @RequestParam List<Long> playlistIds) {
        for (Long playlistId : playlistIds) {
            playlistService.addMusicToPlaylist(playlistId, musicId);
        }
        return "redirect:/playlist"; // 플레이리스트 페이지로 이동
    }
}
