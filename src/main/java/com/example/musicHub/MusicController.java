package com.example.musicHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MusicController {

    @Autowired
    MusicService musicService;

    @RequestMapping("/music")
    public String list(@RequestParam(required = false, defaultValue = "idx") String sortBy, Model model) {
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
        musicService.incrementViews(idx);
        model.addAttribute("music", musicService.findById(idx));
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
        model.addAttribute("musics", musicService.findByGenre(genre)); // 장르별 검색 결과
        model.addAttribute("selectedGenre", genre); // 선택된 장르 전달 (필요 시 UI에서 활용 가능)
        return "list";
    }

}