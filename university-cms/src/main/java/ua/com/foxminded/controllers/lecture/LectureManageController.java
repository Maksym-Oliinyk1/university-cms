package ua.com.foxminded.controllers.lecture;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

@Controller
@RequestMapping("/manage/lecture")
public class LectureManageController {

    private final LectureService lectureService;

    public LectureManageController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("")
    public String manageLecture() {
        return "manage-lecture";
    }

    @GetMapping("/createFormLecture")
    public String showCreateForm(Model model) {
        model.addAttribute("lecture", new Lecture());
        return "create-form-lecture";
    }

    @PostMapping("/createLecture")
    public String createLecture(@ModelAttribute @Valid Lecture lecture) {
        lectureService.save(lecture);
        return "create-form-lecture-successful";
    }

    @GetMapping("/updateFormLecture/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "update-form-lecture";
    }

    @PostMapping("/updateLecture/{id}")
    public String updateLecture(
            @PathVariable Long id, @ModelAttribute @Valid Lecture lecture, Model model) {
        lectureService.update(id, lecture);
        model.addAttribute("lectureId", id);
        return "update-form-lecture-successful";
    }

    @PostMapping("/deleteLecture/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.delete(id);
        return "delete-form-lecture-successful";
    }

    @PostMapping("/attachGroupToLecture")
    public ResponseEntity<String> attachGroupToLecture(
            @RequestParam Long groupId, @RequestParam Long lectureId) {
        lectureService.attachGroupToLecture(groupId, lectureId);
        return ResponseEntity.ok("Group attached to lecture successfully");
    }

    @PostMapping("/detachGroupFromLecture")
    public ResponseEntity<String> detachGroupFromLecture(
            @RequestParam Long groupId, @RequestParam Long lectureId) {
        lectureService.detachGroupFromLecture(groupId, lectureId);
        return ResponseEntity.ok("Group detached from lecture successfully");
    }
}
