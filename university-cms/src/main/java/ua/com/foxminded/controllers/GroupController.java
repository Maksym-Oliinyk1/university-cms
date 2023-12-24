package ua.com.foxminded.controllers;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.GroupService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GroupController {

    @RequestMapping("/group")
    public String showGroupPage() {
        return "group";
    }
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("group/showLectures")
    public String showLecturesBetweenDates(Model model,
                                           @RequestParam Long groupId,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime firstDate,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime secondDate) {
        try {
            List<Lecture> lectures = groupService.showLecturesBetweenDates(groupId, firstDate, secondDate);
            model.addAttribute("lectures", lectures);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "group";
    }
}

