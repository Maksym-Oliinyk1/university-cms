package ua.com.foxminded.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TeacherController {

    @RequestMapping("/teacher")
    public String showTeacherPage() {
        return "teacher";
    }
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/teacher/showLectures")
    public String showLecturesBetweenDates(
            @RequestParam Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime firstDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime secondDate,
            Model model) {
        try {
            List<Lecture> lectures = teacherService.showLecturesBetweenDates(teacherId, firstDate, secondDate);
            model.addAttribute("lectures", lectures);
            return "teacher";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }
}

