package ua.com.foxminded.controllers.student;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.service.StudentService;

import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StudentControllerImpl {

    private final StudentService studentService;

    public StudentControllerImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> save(@RequestBody @Valid StudentDTO studentDTO) {
        StudentDTO savedStudent = studentService.save(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> update(
            @RequestBody @Valid StudentDTO studentDTO, @PathVariable UUID studentId) {
        StudentDTO updatedStudent = studentService.update(studentId, studentDTO);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> get(@PathVariable UUID studentId) {
        StudentDTO studentDTO = studentService.findById(studentId);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID studentId) {
        studentService.delete(studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
