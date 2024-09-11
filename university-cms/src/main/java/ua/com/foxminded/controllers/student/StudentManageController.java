package ua.com.foxminded.controllers.student;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.StudentService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/manage/student")
public class StudentManageController {

    private final StudentService studentService;
    private final AuthenticationService authenticationService;

    public StudentManageController(
            StudentService studentService, AuthenticationService authenticationService) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String manageStudent() {
        return "manage-student";
    }

    @GetMapping("/showStudent/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student";
    }

    @GetMapping("/createFormStudent")
    public String createFormStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "create-form-student-manage";
    }

    @PostMapping("/createStudent")
    public String createStudent(@ModelAttribute @Valid StudentDTO studentDTO) {
        authenticationService.registerStudent(studentDTO);
        return "create-form-student-successful";
    }

    @GetMapping("/updateFormStudent/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        StudentDTO studentDTO = studentService.findByIdDTO(id);
        studentDTO.setId(id);
        model.addAttribute("student", studentDTO);
        return "update-form-student-manage";
    }

    @PostMapping("/updateStudent/{id}")
    public String updateStudent(
            @PathVariable("id") Long id, @ModelAttribute @Valid StudentDTO studentDTO) {
        authenticationService.updateStudent(id, studentDTO);
        return "update-form-student-successful";
    }

    @GetMapping("/listStudents")
    public String listStudents(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Student> pageStudent =
                studentService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("students", pageStudent.getContent());
        model.addAttribute("pageNumber", pageStudent.getNumber());
        model.addAttribute("totalPages", pageStudent.getTotalPages());
        return "manage-student";
    }

    @PostMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return "delete-form-student-successful";
    }
}
