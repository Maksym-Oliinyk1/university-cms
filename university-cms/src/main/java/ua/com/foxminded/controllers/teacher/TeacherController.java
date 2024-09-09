package ua.com.foxminded.controllers.teacher;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.security.AuthenticationResponse;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.TeacherService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;
import static ua.com.foxminded.utill.UtilController.extractToken;

@Controller
@RequestMapping("/general/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final JwtService jwtService;

    public TeacherController(TeacherService teacherService, JwtService jwtService) {
        this.teacherService = teacherService;
        this.jwtService = jwtService;
    }

    // General methods

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerTeacher(
            @ModelAttribute @Valid TeacherDTO teacherDTO) {
        String token = jwtService.generateToken(teacherService.save(teacherDTO));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/createFormTeacher")
    public String createFormTeacher(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        return "create-form-teacher-manage";
    }

    @GetMapping("/showTeacher")
    public String showTeacher(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        Teacher teacher = teacherService.findById(tokenId);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @GetMapping("/showTeacherToLecture/{id}")
    public String showTeacherToLecture(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @GetMapping("/updateFormTeacher")
    public String showUpdateForm(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        TeacherDTO teacherDTO = teacherService.findByIdDTO(tokenId);
        teacherDTO.setId(tokenId);
        model.addAttribute("teacher", teacherDTO);
        return "update-form-teacher";
    }

    @PostMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute @Valid TeacherDTO teacherDTO) {
        teacherService.update(id, teacherDTO);
        return "update-form-teacher-successful";
    }

    @PostMapping("/deleteTeacher")
    public String deleteTeacher(@RequestHeader("Authorization") String authToken) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);

        teacherService.delete(tokenId);
        return "delete-form-teacher-successful";
    }

    @GetMapping("/list/teachers")
    @ResponseBody
    public ResponseEntity<List<Teacher>> getTeachers(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Teacher> teachersPage =
                teacherService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Teacher> teachers = teachersPage.getContent();
        return ResponseEntity.ok(teachers);
    }

    // Manage methods

    @GetMapping("/manage")
    public String manageTeacher() {
        return "manage-teacher";
    }

    @GetMapping("/manage/showTeacher/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @PostMapping("/manage/createTeacher")
    public String createTeacher(@ModelAttribute @Valid TeacherDTO teacherDTO) {
        teacherService.save(teacherDTO);
        return "create-form-teacher-successful";
    }

    @GetMapping("/manage/updateFormTeacher/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TeacherDTO teacherDTO = teacherService.findByIdDTO(id);
        teacherDTO.setId(id);
        model.addAttribute("teacher", teacherDTO);
        return "update-form-teacher-manage";
    }

    @PostMapping("/manage/updateTeacher/{id}")
    public String updateTeacherManage(
            @PathVariable("id") Long id, @ModelAttribute @Valid TeacherDTO teacherDTO) {
        teacherService.update(id, teacherDTO);
        return "update-form-teacher-successful";
    }

    @PostMapping("/manage/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return "delete-form-teacher-successful";
    }

    @GetMapping("/manage/listTeachers")
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
