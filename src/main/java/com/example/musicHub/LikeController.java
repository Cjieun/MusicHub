package com.example.musicHub;

import jakarta.servlet.http.HttpSession;
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
    private HttpSession session;

    @PostMapping("/toggle/{musicId}")
    public String toggleLike(@PathVariable long musicId, @RequestParam String redirectPage) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인 필요
        }

        long userId = loggedInUser.getId();

        likeService.toggleLike(userId, musicId);

        if ("list".equals(redirectPage)) {
            return "redirect:/music";
        } else if ("read".equals(redirectPage)) {
            return "redirect:/music/" + musicId;
        }

        return "redirect:/music";
    }
}
