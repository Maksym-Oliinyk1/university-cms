package ua.com.foxminded.controllers.student;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.StudentService;

@Controller
@RequestMapping("/general/student")
public class StudentGeneralController {

    private final StudentService studentService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public StudentGeneralController(
            StudentService studentService,
            JwtService jwtService,
            AuthenticationService authenticationService) {
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/showStudent")
    public String showStudent(@RequestParam("token") String token, Model model) {
        Long tokenUserId = jwtService.extractUserId(token);

        Student student = studentService.findById(tokenUserId);
        model.addAttribute("student", student);
        return "student";
    }

    @GetMapping("/updateFormStudent")
    public String showUpdateForm(@RequestParam("token") String token, Model model) {
        Long tokenUserId = jwtService.extractUserId(token);

        StudentDTO studentDTO = studentService.findByIdDTO(tokenUserId);
        studentDTO.setId(tokenUserId);
        model.addAttribute("student", studentDTO);
        return "update-form-student";
    }

    @PostMapping("/updateStudent/{id}")
    public String updateStudent(
            @PathVariable("id") Long id, @ModelAttribute @Valid StudentDTO studentDTO) {
        authenticationService.updateStudent(id, studentDTO);
        return "update-form-student-successful";
    }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("token") String token) {
        Long tokenUserId = jwtService.extractUserId(token);

        studentService.delete(tokenUserId);
        return "delete-form-student-successful";
    }
}
