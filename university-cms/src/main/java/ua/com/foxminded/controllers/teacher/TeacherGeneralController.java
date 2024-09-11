package ua.com.foxminded.controllers.teacher;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.TeacherService;

@Controller
@RequestMapping("/general/teacher")
public class TeacherGeneralController {
    private final TeacherService teacherService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public TeacherGeneralController(
            TeacherService teacherService,
            JwtService jwtService,
            AuthenticationService authenticationService) {
        this.teacherService = teacherService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/showTeacher")
    public String showTeacher(@RequestParam("token") String token, Model model) {
        Long tokenId = jwtService.extractUserId(token);
        Teacher teacher = teacherService.findById(tokenId);
        model.addAttribute("teacher", teacher);
        return "teacher";
    }

    @GetMapping("/updateFormTeacher")
    public String showUpdateForm(@RequestParam String token, Model model) {
        Long tokenId = jwtService.extractUserId(token);
        TeacherDTO teacherDTO = teacherService.findByIdDTO(tokenId);
        teacherDTO.setId(tokenId);
        model.addAttribute("teacher", teacherDTO);
        return "update-form-teacher";
    }

    @PostMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute @Valid TeacherDTO teacherDTO) {
        authenticationService.updateTeacher(id, teacherDTO);
        return "update-form-teacher-successful";
    }

    @PostMapping("/deleteTeacher")
    public String deleteTeacher(@RequestParam("token") String token) {
        Long tokenId = jwtService.extractUserId(token);

        teacherService.delete(tokenId);
        return "delete-form-teacher-successful";
    }
}
