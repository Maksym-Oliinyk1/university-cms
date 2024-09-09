package ua.com.foxminded.controllers.student;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.security.AuthenticationResponse;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;
import static ua.com.foxminded.utill.UtilController.extractToken;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final JwtService jwtService;
    private final GroupService groupService;

    public StudentController(
            StudentService studentService, JwtService jwtService, GroupService groupService) {
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.groupService = groupService;
    }

    // General methods

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerStudent(
            @ModelAttribute @Valid StudentDTO studentDTO) {
        String token = jwtService.generateToken(studentService.save(studentDTO));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/createFormStudent")
    public String createFormStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "create-form-student-manage";
    }

    @GetMapping("/showStudent")
    public String showStudent(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenUserId = jwtService.extractUserId(token);

        Student student = studentService.findById(tokenUserId);
        model.addAttribute("student", student);
        return "student";
    }

    @GetMapping("/updateFormStudent")
    public String showUpdateForm(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenUserId = jwtService.extractUserId(token);

        StudentDTO studentDTO = studentService.findByIdDTO(tokenUserId);
        studentDTO.setId(tokenUserId);
        model.addAttribute("student", studentDTO);
        return "update-form-student";
    }

    @PostMapping("/updateStudent/{id}")
    public String updateStudent(
            @PathVariable("id") Long id, @ModelAttribute @Valid StudentDTO studentDTO) {
        studentService.update(id, studentDTO);
        return "update-form-student-successful";
    }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestHeader("Authorization") String authToken) {
        String token = extractToken(authToken);
        Long tokenUserId = jwtService.extractUserId(token);

        studentService.delete(tokenUserId);
        return "delete-form-student-successful";
    }

    @GetMapping("/listStudentsByGroup/{groupId}")
    public String listStudentsByGroup(
            @PathVariable Long groupId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Student> pageStudent =
                studentService.findAllStudentByGroup(
                        groupId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("students", pageStudent.getContent());
        model.addAttribute("pageNumber", pageStudent.getNumber());
        model.addAttribute("totalPages", pageStudent.getTotalPages());
        return "group";
    }

    @GetMapping("/showStudentToGroup/{id}")
    public String showStudentToGroup(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "group";
    }

    // Manage methods

    @GetMapping("/manage")
    public String manageStudent() {
        return "manage-student";
    }

    @GetMapping("/manage/showStudent/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student";
    }

    @PostMapping("/manage/createStudent")
    public String createStudent(@ModelAttribute @Valid StudentDTO studentDTO) {
        studentService.save(studentDTO);
        return "create-form-student-successful";
    }

    @GetMapping("/manage/updateFormStudent/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        StudentDTO studentDTO = studentService.findByIdDTO(id);
        studentDTO.setId(id);
        model.addAttribute("student", studentDTO);
        return "update-form-student-manage";
    }

    @PostMapping("/manage/updateStudent/{id}")
    public String updateStudentManage(
            @PathVariable("id") Long id, @ModelAttribute @Valid StudentDTO studentDTO) {
        studentService.update(id, studentDTO);
        return "update-form-student-successful";
    }

    @GetMapping("/manage/listStudents")
    public String listStudents(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Student> pageStudent =
                studentService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("students", pageStudent.getContent());
        model.addAttribute("pageNumber", pageStudent.getNumber());
        model.addAttribute("totalPages", pageStudent.getTotalPages());
        return "manage-student";
    }

    @PostMapping("/manage/deleteStudent/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return "delete-form-student-successful";
    }
}
