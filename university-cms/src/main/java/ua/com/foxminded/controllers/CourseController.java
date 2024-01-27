package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

@RestController
public class CourseController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/showCourse/{id}")
    public String showCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/listCourses")
    public String listCourses(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Course> pageCourses = courseService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("courses", pageCourses.getContent());
        model.addAttribute("pageNumber", pageCourses.getNumber());
        model.addAttribute("totalPages", pageCourses.getTotalPages());
        return "list-courses";
    }

    @GetMapping("/createFormCourse")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create-form-course";
    }

    @PostMapping("/createCourse")
    public String createCourse(@ModelAttribute Course course) {
        courseService.save(course);
        return "create-form-course-successful";
    }

    @GetMapping("/updateFormCourse/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "update-form-course";
    }

    @PutMapping("/updateCourse/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course) {
        courseService.update(id, course);
        return "update-form-course-successful";
    }

    @DeleteMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "delete-form-course-successful";
    }
}
