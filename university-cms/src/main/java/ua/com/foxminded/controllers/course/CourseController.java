package ua.com.foxminded.controllers.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final FacultyService facultyService;

    public CourseController(CourseService courseService, FacultyService facultyService) {
        this.courseService = courseService;
        this.facultyService = facultyService;
    }

    // General methods

    @GetMapping("/showCourse")
    public String showCourse(@RequestParam("id") Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/listCourses")
    public String listCourses(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        fillModelWithDataCourses(pageNumber, model);
        return "manage-course";
    }

    @GetMapping("/listCoursesToLecture")
    public String listCoursesToLecture(
            @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        fillModelWithDataCourses(pageNumber, model);
        return "create-form-lecture";
    }

    private void fillModelWithDataCourses(int pageNumber, Model model) {
        Page<Course> pageCourses =
                courseService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("courses", pageCourses.getContent());
        model.addAttribute("pageNumber", pageCourses.getNumber());
        model.addAttribute("totalPages", pageCourses.getTotalPages());
    }

    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<List<Course>> getCourses(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Course> coursesPage =
                courseService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Course> courses = coursesPage.getContent();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/listCoursesByFaculty/{facultyId}")
    public String listCoursesByFacultyId(
            @PathVariable Long facultyId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Course> pageCourse =
                courseService.findAllOfFaculty(
                        facultyId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        Faculty faculty = facultyService.findById(facultyId);
        model.addAttribute("courses", pageCourse.getContent());
        model.addAttribute("pageNumber", pageCourse.getNumber());
        model.addAttribute("totalPages", pageCourse.getTotalPages());
        model.addAttribute("faculty", faculty);
        return "faculty";
    }

    // Manage methods

    @GetMapping("/manage")
    public String manageCourse() {
        return "manage-course";
    }

    @GetMapping("/manage/createFormCourse")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create-form-course";
    }

    @PostMapping("/manage/createCourse")
    public String createCourse(@ModelAttribute Course course) {
        courseService.save(course);
        return "create-form-course-successful";
    }

    @GetMapping("/manage/updateFormCourse/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "update-form-course";
    }

    @PostMapping("/manage/updateCourse/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course) {
        courseService.update(id, course);
        return "update-form-course-successful";
    }

    @PostMapping("/manage/deleteCourse/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "delete-form-course-successful";
    }
}
