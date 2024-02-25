package ua.com.foxminded.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;

@Controller
public class LectureController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final LectureService lectureService;
    private final CourseService courseService;
    private final GroupService groupService;

    public LectureController(LectureService lectureService, CourseService courseService, GroupService groupService) {
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.groupService = groupService;
    }

    @GetMapping("/manageLecture")
    public String manageLecture() {
        return "manage-lecture";
    }

    @GetMapping("/showLecture")
    public String showLecture(@RequestParam("id") Long id, Model model) {
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
        return "manage-lecture";
    }


    @GetMapping("/listLecturesByCourse/{courseId}")
    public String listLecturesByCourseId(@PathVariable Long courseId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Lecture> pageLecture = lectureService.findAllByCourse(courseId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        Course course = courseService.findById(courseId);
        model.addAttribute("lectures", pageLecture.getContent());
        model.addAttribute("pageNumber", pageLecture.getNumber());
        model.addAttribute("totalPages", pageLecture.getTotalPages());
        model.addAttribute("course", course);
        return "course";
    }


    @GetMapping("/listLecturesByGroup/{groupId}")
    public String listLecturesByGroupId(@PathVariable Long groupId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Lecture> pageLecture = lectureService.findAllByGroup(groupId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("lectures", pageLecture.getContent());
        model.addAttribute("pageNumber", pageLecture.getNumber());
        model.addAttribute("totalPages", pageLecture.getTotalPages());
        return "group";
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
    public String updateLecture(@PathVariable Long id, @ModelAttribute @Valid Lecture lecture, Model model) {
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
