package ua.com.foxminded.controllers.teacher;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.TeacherService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/manage/teacher")
public class TeacherManageController {
    private final TeacherService teacherService;
    private final AuthenticationService authenticationService;

    public TeacherManageController(
            TeacherService teacherService, AuthenticationService authenticationService) {
        this.teacherService = teacherService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String manageTeacher() {
        return "manage-teacher";
    }

    @GetMapping("/showTeacher/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @GetMapping("/createFormTeacher")
    public String createFormTeacher(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        return "create-form-teacher-manage";
    }

    @PostMapping("/createTeacher")
    public String createTeacher(@ModelAttribute @Valid TeacherDTO teacherDTO) {
        authenticationService.registerTeacher(teacherDTO);
        return "create-form-teacher-successful";
    }

    @GetMapping("/updateFormTeacher/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TeacherDTO teacherDTO = teacherService.findByIdDTO(id);
        teacherDTO.setId(id);
        model.addAttribute("teacher", teacherDTO);
        return "update-form-teacher-manage";
    }

    @PostMapping("/updateTeacher/{id}")
    public String updateTeacher(
            @PathVariable("id") Long id, @ModelAttribute @Valid TeacherDTO teacherDTO) {
        authenticationService.updateTeacher(id, teacherDTO);
        return "update-form-teacher-successful";
    }

    @PostMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return "delete-form-teacher-successful";
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
}
