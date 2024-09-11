package ua.com.foxminded.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/teacherAuthorization")
    public String teacherAuthorization() {
        return "mock-teacher-authorization";
    }

    @GetMapping("/manageTeacher")
    public String manageTeacher() {
        return "manage-teacher";
    }

    @GetMapping("/showTeacher")
    public String showTeacher(@RequestParam("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @GetMapping("/listTeachers")
    public String listTeacher(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        fillModelWithDataTeachers(pageNumber, model);
        return "manage-teacher";
    }

    private void fillModelWithDataTeachers(int pageNumber, Model model) {
        Page<Teacher> pageTeachers =
                teacherService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("teachers", pageTeachers.getContent());
        model.addAttribute("pageNumber", pageTeachers.getNumber());
        model.addAttribute("totalPages", pageTeachers.getTotalPages());
    }

    @GetMapping("/createFormTeacher")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        return "create-form-teacher";
    }

    @PostMapping("/createTeacher")
    public String createTeacher(@ModelAttribute @Valid TeacherDTO teacherDTO) {
        teacherService.save(teacherDTO);
        return "create-form-teacher-successful";
    }

    @GetMapping("/updateFormTeacher/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        TeacherDTO teacherDTO = teacherService.findByIdDTO(id);
        teacherDTO.setId(id);
        model.addAttribute("teacher", teacherDTO);
        return "update-form-teacher";
    }

    @PostMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute @Valid TeacherDTO teacherDTO) {
        teacherService.update(id, teacherDTO);
        return "update-form-teacher-successful";
    }

    @PostMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
        return "delete-form-teacher-successful";
    }
}
