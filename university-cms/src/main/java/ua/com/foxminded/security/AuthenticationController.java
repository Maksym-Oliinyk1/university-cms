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

  @PostMapping("/admin/register")
  public ResponseEntity<AuthenticationResponse> registerAdmin(
          @ModelAttribute @Valid AdministratorDTO administratorDTO) {
    return ResponseEntity.ok(authenticationService.registerAdmin(administratorDTO));
  }

  @PostMapping("/maintainer/register")
  public ResponseEntity<AuthenticationResponse> registerMaintainer(
          @ModelAttribute @Valid MaintainerDTO maintainerDTO) {
    return ResponseEntity.ok(authenticationService.registerMaintainer(maintainerDTO));
  }

  @PostMapping("/student/register")
  public ResponseEntity<AuthenticationResponse> registerStudent(
          @ModelAttribute @Valid StudentDTO studentDTO) {
    return ResponseEntity.ok(authenticationService.registerStudent(studentDTO));
  }

  @GetMapping("/createFormStudent")
  public String showCreateFormStudent(Model model) {
    model.addAttribute("student", new StudentDTO());
    return "create-form-student";
  }

  @PostMapping("/teacher/register")
  public ResponseEntity<AuthenticationResponse> registerTeacher(
          @ModelAttribute @Valid TeacherDTO teacherDTO) {
    return ResponseEntity.ok(authenticationService.registerTeacher(teacherDTO));
  }

  @GetMapping("/createFormTeacher")
  public String showCreateFormTeacher(Model model) {
    model.addAttribute("teacher", new TeacherDTO());
    return "create-form-teacher";
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @ModelAttribute @Valid AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.signIn(authenticationRequest));
  }
}
