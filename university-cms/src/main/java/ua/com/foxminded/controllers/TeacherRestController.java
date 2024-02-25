package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

import java.util.List;

@RestController
public class TeacherRestController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
    private final TeacherService teacherService;

    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/getListTeachers")
    @ResponseBody
    public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Teacher> teachersPage = teacherService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Teacher> teachers = teachersPage.getContent();
        return ResponseEntity.ok(teachers);
    }
}
