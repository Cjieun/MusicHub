package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpSession session, Model model) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) { // 단순 비밀번호 확인
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        // 세션에 사용자 정보를 저장
        session.setAttribute("loggedInUser", user);
        return "redirect:/music"; // 로그인 성공 시 메인 페이지로 리다이렉트
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }
}
