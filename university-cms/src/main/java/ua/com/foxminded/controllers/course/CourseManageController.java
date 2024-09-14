package ua.com.foxminded.controllers.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.service.CourseService;

@Controller
@RequestMapping("/manage/course")
public class CourseManageController {

    private final CourseService courseService;

    public CourseManageController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("")
    public String manageCourse() {
        return "manage-course";
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

    @PostMapping("/updateCourse/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course) {
        courseService.update(id, course);
        return "update-form-course-successful";
    }

    @PostMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "delete-form-course-successful";
    }
}
