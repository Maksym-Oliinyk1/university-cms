package ua.com.foxminded.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/studentAuthorization")
    public String studentAuthorization() {
        return "mock-student-authorization";
    }

    @GetMapping("/manageStudent")
    public String manageStudent() {
        return "manage-student";
    }

    @GetMapping("/showStudent")
    public String showStudent(@RequestParam("id") Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student";
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

    @GetMapping("/createFormStudent")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "create-form-student";
    }

    @PostMapping("/createStudent")
    public String createStudent(@ModelAttribute @Valid StudentDTO studentDTO) {
        studentService.save(studentDTO);
        return "create-form-student-successful";
    }

    @GetMapping("/updateFormStudent/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        StudentDTO studentDTO = studentService.findByIdDTO(id);
        studentDTO.setId(id);
        model.addAttribute("student", studentDTO);
        return "update-form-student";
    }

    @PostMapping("/updateStudent/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute @Valid StudentDTO studentDTO) {
        studentService.update(id, studentDTO);
        return "update-form-student-successful";
    }

    @PostMapping("deleteStudent/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "delete-form-student-successful";
    }
}
