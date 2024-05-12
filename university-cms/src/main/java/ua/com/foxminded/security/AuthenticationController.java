package ua.com.foxminded.security;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.dto.TeacherDTO;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String authenticate() {
        return "authenticate";
    }

    @GetMapping("/successful")
    public String successful() {
        return "successful";
    }


    @GetMapping("/createFormStudent")
    public String showCreateFormStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "create-form-student";
    }


    @GetMapping("/createFormTeacher")
    public String showCreateFormTeacher(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        return "create-form-teacher";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @ModelAttribute @Valid AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.signIn(authenticationRequest));
    }
}
