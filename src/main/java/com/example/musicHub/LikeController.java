package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private MusicService musicService;

    private final long currentUserId = 1;

    @PostMapping("/toggle/{musicId}")
    public String toggleLike(@PathVariable long musicId, @RequestParam String redirectPage) {
        likeService.toggleLike(1, musicId);

        if ("list".equals(redirectPage)) {
            return "redirect:/music";
        } else if ("read".equals(redirectPage)) {
            return "redirect:/music/" + musicId;
        }

        return "redirect:/music";
    }
}
