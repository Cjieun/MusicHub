package com.example.musicHub;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class MusicController {

    @Autowired
    MusicService musicService;
    @Autowired
    LikeService likeService;
    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home()  {
        return "home";
    }

    @RequestMapping("/music")
    public String list(@RequestParam(required = false, defaultValue = "idx") String sortBy, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("musics", musicService.getSortedMusics(sortBy));
        model.addAttribute("currentSort", sortBy);
        return "list";
    }

    @RequestMapping("/music/addform")
    public String addform()  {
        return "addform";
    }

    @RequestMapping("/music/add")
    public String add(@ModelAttribute MusicDTO music)  {
        musicService.save(music);
        return "redirect:/music";
    }

    @RequestMapping("/music/{idx}")
    public String read(@PathVariable long idx, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        musicService.incrementViews(idx);
        MusicDTO music = musicService.findById(idx);
        boolean isLiked = likeService.isLiked(loggedInUser.getId(), idx);

        model.addAttribute("music", music);
        model.addAttribute("isLiked", isLiked);
        return "read";
    }

    @RequestMapping("/music/delete/{idx}")
    public String delete(@PathVariable long idx)  {
        musicService.deleteById(idx);
        return "redirect:/music";
    }

    @RequestMapping("/music/updateform/{idx}")
    public String updatemusic(@PathVariable Long idx,  Model model) {
        MusicDTO music = musicService.findById(idx);
        model.addAttribute("music", music);
        return "updateform";
    }

    @RequestMapping("/music/update")
    public String update(@ModelAttribute MusicDTO music)  {
        musicService.save(music);
        return "redirect:/music";
    }

    @GetMapping("/music/search")
    public String search(@RequestParam String query, Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("musics", musicService.search(query));
        } else {
            model.addAttribute("musics", musicService.findAll());
        }
        return "list";
    }

    @GetMapping("/music/filter")
    public String filterByGenre(@RequestParam String genre, Model model) {
        List<MusicDTO> musics;
        if ("전체".equals(genre)) {
            musics = musicService.findAll(); // 전체 목록 반환
        } else {
            musics = musicService.findByGenre(genre); // 장르별 목록 반환
        }

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("musics", musics); // 장르별 검색 결과
        model.addAttribute("selectedGenre", genre); // 선택된 장르 전달 (필요 시 UI에서 활용 가능)
        return "list";
    }

    // MBTI 질문지 페이지를 로드하는 메서드
    @GetMapping("/recommend")
    public String showQuestionnaire() {
        return "questionnaire";
    }

    // MBTI 추천 처리 메서드
    @PostMapping("/recommend")
    public String processMbtiForm(@RequestParam Map<String, String> formData, Model model) {
        // 사용자가 제출한 formData를 기반으로 MBTI 결과 계산
        String mbti = calculateMbti(formData);

        // MBTI에 따른 추천 음악 리스트 가져오기
        List<MusicDTO> recommendedMusics = musicService.getRecommendationsForMbti(mbti);
        model.addAttribute("recommendedMusics", recommendedMusics);
        model.addAttribute("mbti", mbti);
        return "recommend"; // recommend.html을 반환
    }

    // 예시 MBTI 계산 메서드 (사용자 로직에 맞게 구현)
    private String calculateMbti(Map<String, String> formData) {
        StringBuilder mbti = new StringBuilder();
        mbti.append(formData.getOrDefault("q1", "E"));
        mbti.append(formData.getOrDefault("q2", "N"));
        mbti.append(formData.getOrDefault("q3", "F"));
        mbti.append(formData.getOrDefault("q4", "P"));
        return mbti.toString();
    }
    @GetMapping("/recommended-music")
    public String getRecommendedMusic(HttpSession session, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        String userMbti = loggedInUser.getMbti();
        if (userMbti == null || userMbti.isEmpty()) {
            return "redirect:/recommend"; // MBTI 정보가 없는 경우 추천 페이지로 리다이렉트
        }

        List<MusicDTO> recommendedMusics = musicService.getRecommendedMusicsByMbti(userMbti);
        model.addAttribute("recommendedMusics", recommendedMusics);
        return "recommended-music";
    }
    @PostMapping("/save-mbti")
    public String saveMbti(
            @RequestParam(value = "mbti", required = false) String selectedMbti, // 직접 선택한 MBTI
            @RequestParam(value = "detectedMbti", required = false) String detectedMbti, // 검사 결과로 얻은 MBTI
            HttpSession session) {

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 우선 순위: 직접 선택한 MBTI > 검사 결과로 얻은 MBTI
        String finalMbti = (selectedMbti != null && !selectedMbti.isEmpty()) ? selectedMbti : detectedMbti;

        if (finalMbti == null || finalMbti.isEmpty()) {
            return "redirect:/recommend"; // 아무 값도 없으면 추천 페이지로 리다이렉트
        }

        // 중복 저장 방지
        if (finalMbti.equals(loggedInUser.getMbti())) {
            return "redirect:/recommended-music";
        }

        // 사용자 정보 업데이트
        loggedInUser.setMbti(finalMbti);
        session.setAttribute("loggedInUser", loggedInUser);
        userService.updateUserMbti(loggedInUser);

        return "redirect:/recommended-music";
    }


    @GetMapping("/music/audio/{idx}")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> getAudioFile(@PathVariable Long idx) {
        MusicDTO music = musicService.findById(idx);
        if (music == null || music.getMp3Path() == null || music.getMp3Path().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get("src/main/resources/static", music.getMp3Path());
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        org.springframework.core.io.Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }
}