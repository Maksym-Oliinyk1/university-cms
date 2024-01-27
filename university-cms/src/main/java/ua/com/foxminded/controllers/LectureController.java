package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.LectureService;

@Controller
public class LectureController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/showLecture/{id}")
    public String showLecture(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "lecture";
    }

    @GetMapping("/listLectures")
    public String listLectures(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Lecture> pageLecture = lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("lectures", pageLecture.getContent());
        model.addAttribute("pageNumber", pageLecture.getNumber());
        model.addAttribute("totalPages", pageLecture.getTotalPages());
        return "list-lectures";
    }

    @GetMapping("/createFormLecture")
    public String showCreateForm(Model model) {
        model.addAttribute("lecture", new Lecture());
        return "create-form-lecture";
    }

    @PostMapping("/createLecture")
    public String createLecture(@ModelAttribute Lecture lecture) {
        lectureService.save(lecture);
        return "create-form-lecture-successful";
    }

    @GetMapping("/updateFormLecture/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "update-form-lecture";
    }

    @PutMapping("/updateLecture/{id}")
    public String updateLecture(@PathVariable Long id, @ModelAttribute Lecture lecture) {
        lectureService.update(id, lecture);
        return "update-form-lecture-successful";
    }

    @DeleteMapping("/deleteLecture/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.delete(id);
        return "delete-form-lecture-successful";
    }

    @PostMapping("/attachGroupToLecture")
    public ResponseEntity<String> attachGroupToLecture(
            @RequestParam Long groupId,
            @RequestParam Long lectureId) {
        lectureService.attachGroupToLecture(groupId, lectureId);
        return ResponseEntity.ok("Group attached to lecture successfully");
    }

    @PostMapping("/detachGroupFromLecture")
    public ResponseEntity<String> detachGroupFromLecture(
            @RequestParam Long groupId,
            @RequestParam Long lectureId) {
        lectureService.detachGroupFromLecture(groupId, lectureId);
        return ResponseEntity.ok("Group detached from lecture successfully");
    }

}
