package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@RestController
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/getListCourses")
    @ResponseBody
    public ResponseEntity<List<Course>> getCourses(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Course> coursesPage =
                courseService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Course> courses = coursesPage.getContent();
        return ResponseEntity.ok(courses);
    }
}
