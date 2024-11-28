package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private  MusicService musicService;
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

    @GetMapping("/addform")
    public String addForm(Model model) {
        List<MusicDTO> musics = musicService.findAll();
        model.addAttribute("musics", musics);
        return "playlist/addform";
    }

    @PostMapping("/add")
    public String addPlaylist(@ModelAttribute PlaylistDTO playlistDTO,
                              @RequestParam(required = false) List<Long> musicIds) {
        playlistService.save(playlistDTO, currentUserId, musicIds);
        return "redirect:/playlist";
    }

    @PostMapping("/delete")
    public String deletePlaylist(@RequestParam long id) {
        playlistService.deleteById(id); // 삭제 로직 호출
        return "redirect:/playlist"; // 삭제 후 플레이리스트 목록으로 리다이렉트
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        PlaylistDTO playlist = playlistService.findById(id);
        if (playlist == null) {
            return "redirect:/playlist"; // 존재하지 않는 경우 목록으로 리다이렉트
        }
        model.addAttribute("playlist", playlist);
        return "playlist/read";
    }
}
