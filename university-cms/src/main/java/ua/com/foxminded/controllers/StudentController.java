package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.StudentService;

@RestController
public class StudentController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/showStudent/{id}")
    public String showStudent(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student";
    }

    @GetMapping("/listStudents")
    public String listStudents(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Student> pageStudent = studentService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("student", pageStudent.getContent());
        model.addAttribute("pageNumber", pageStudent.getNumber());
        model.addAttribute("totalPages", pageStudent.getTotalPages());
        return "list-students";
    }

    @GetMapping("/createFormStudent")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "create-form-student";
    }

    @PostMapping("/createStudent")
    public String createStudent(@ModelAttribute Student student, @RequestParam("imageFile") MultipartFile imageFile) {
        studentService.save(student, imageFile);
        return "create-form-student-successful";
    }

    @GetMapping("/updateFormStudent/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "update-form-student";
    }

    @PutMapping("updateStudent/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student, @RequestParam("imageFile") MultipartFile imageFile) {
        studentService.update(id, student, imageFile);
        return "update-form-student-successful";
    }

    @DeleteMapping("deleteStudent/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "delete-form-student-successful";
    }
}
