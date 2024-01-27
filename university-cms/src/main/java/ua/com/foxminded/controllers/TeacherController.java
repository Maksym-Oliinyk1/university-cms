package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

@RestController
public class TeacherController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/showTeacher/{id}")
    public String showTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "create-form-teacher-successful";
    }

    @GetMapping("/listTeachers")
    public String listTeacher(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Teacher> pageTeacher = teacherService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("teachers", pageTeacher.getContent());
        model.addAttribute("pageNumber", pageTeacher.getNumber());
        model.addAttribute("totalPages", pageTeacher.getTotalPages());
        return "list-teachers";
    }

    @GetMapping("/createFormTeacher")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "create-form-teacher";
    }

    @PostMapping("/createTeacher")
    public String createTeacher(@ModelAttribute Teacher teacher, @RequestParam("imageFile") MultipartFile imageFile) {
        teacherService.save(teacher, imageFile);
        return "create-form-teacher-successful";
    }

    @GetMapping("/updateFormTeacher/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "update-form-teacher";
    }

    @PutMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute Teacher teacher, @RequestParam("imageFile") MultipartFile imageFile) {
        teacherService.update(id, teacher, imageFile);
        return "update-form-teacher-successful";
    }

    @DeleteMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
        return "delete-form-teacher-successful";
    }

    @PostMapping("/attachLectureToTeacher")
    public ResponseEntity<String> attachLectureToTeacher(
            @RequestParam Long lectureId,
            @RequestParam Long teacherId) {
        teacherService.attachLectureToTeacher(lectureId, teacherId);
        return ResponseEntity.ok("Lecture attached to teacher successfully");
    }

    @PostMapping("/detachLectureFromTeacher")
    public ResponseEntity<String> detachLectureFromTeacher(
            @RequestParam Long lectureId,
            @RequestParam Long teacherId) {
        teacherService.detachLectureFromTeacher(lectureId, teacherId);
        return ResponseEntity.ok("Lecture detached from teacher successfully");
    }
}
