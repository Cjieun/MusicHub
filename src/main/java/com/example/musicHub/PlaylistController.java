package com.example.musicHub;

import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private HttpSession session;

    @GetMapping
    public String list(Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인 필요
        }

        long userId = loggedInUser.getId();

        List<PlaylistDTO> playlists = playlistService.findAllByUserId(userId);
        model.addAttribute("playlists", playlists);
        model.addAttribute("playlistCount", playlists.size());
        return "playlist/list";
    }

    @GetMapping("/select")
    public String selectPlaylist(@RequestParam long musicId, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        long userId = loggedInUser.getId();

        model.addAttribute("playlists", playlistService.findAllByUserId(userId));
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
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        long userId = loggedInUser.getId();

        playlistService.save(playlistDTO, userId, musicIds);
        return "redirect:/playlist";
    }

    @PostMapping("/delete")
    public String deletePlaylist(@RequestParam long id) {
        playlistService.deleteById(id); // 삭제 로직 호출
        return "redirect:/playlist"; // 삭제 후 플레이리스트 목록으로 리다이렉트
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        PlaylistDTO playlist = playlistService.findById(id);

        if (playlist == null) {
            return "redirect:/playlist"; // 존재하지 않는 경우 목록으로 리다이렉트
        }

        model.addAttribute("playlistCount", playlist.getMusics().size());
        model.addAttribute("playlist", playlist);
        return "playlist/read";
    }

    @PostMapping("/removeMusic")
    public String removeMusicFromPlaylist(@RequestParam long playlistId, @RequestParam long musicId) {
        playlistService.removeMusicFromPlaylist(playlistId, musicId);
        return "redirect:/playlist/" + playlistId;
    }

    @GetMapping("/update/{id}")
    public String updateform(@PathVariable long id, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 플레이리스트 데이터 가져오기
        PlaylistDTO playlist = playlistService.findById(id);
        if (playlist == null) {
            return "redirect:/playlist"; // 존재하지 않으면 목록으로 리다이렉트
        }

        // 모든 음악 가져오기 (사용자가 선택 가능하도록)
        List<MusicDTO> allMusics = musicService.findAll();

        model.addAttribute("playlist", playlist);
        model.addAttribute("musics", allMusics);
        return "playlist/updateform"; // 수정 페이지 뷰 이름
    }

    @PostMapping("/update")
    public String update(@ModelAttribute PlaylistDTO playlistDTO,
                                 @RequestParam(required = false) List<Long> musicIds) {
        playlistService.updatePlaylist(playlistDTO, musicIds);
        return "redirect:/playlist/" + playlistDTO.getId(); // 상세 페이지로 리다이렉트
    }
}
