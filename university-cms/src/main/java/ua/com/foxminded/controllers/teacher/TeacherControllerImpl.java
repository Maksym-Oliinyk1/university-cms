package ua.com.foxminded.controllers.teacher;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.controllers.TeacherController;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.service.TeacherService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher")
public class TeacherControllerImpl implements TeacherController {
    private final TeacherService teacherService;

    public TeacherControllerImpl(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TeacherDTO> save(@RequestBody @Valid TeacherDTO teacherDTO) {
        TeacherDTO savedTeacher = teacherService.save(teacherDTO);
        return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{teacherId}")
    public ResponseEntity<TeacherDTO> update(
            @RequestBody @Valid TeacherDTO teacherDTO, @PathVariable UUID teacherId) {
        TeacherDTO updatedTeacher = teacherService.update(teacherId, teacherDTO);
        return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherDTO> findById(@PathVariable UUID teacherId) {
        TeacherDTO teacherDTO = teacherService.findById(teacherId);
        return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> delete(@PathVariable UUID teacherId) {
        teacherService.delete(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
