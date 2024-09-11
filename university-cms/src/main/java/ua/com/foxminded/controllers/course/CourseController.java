package ua.com.foxminded.controllers.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.FacultyService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/general/course")
public class CourseController {
  private final CourseService courseService;
  private final FacultyService facultyService;

  public CourseController(CourseService courseService, FacultyService facultyService) {
    this.courseService = courseService;
    this.facultyService = facultyService;
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
}
