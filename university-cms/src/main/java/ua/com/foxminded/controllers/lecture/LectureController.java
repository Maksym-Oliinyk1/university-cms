package ua.com.foxminded.controllers.lecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.TeacherService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/general/lecture")
public class LectureController {
  private final LectureService lectureService;
  private final CourseService courseService;
  private final GroupService groupService;
  private final TeacherService teacherService;

  public LectureController(
          LectureService lectureService,
          CourseService courseService,
          GroupService groupService,
          TeacherService teacherService) {
    this.lectureService = lectureService;
    this.courseService = courseService;
    this.groupService = groupService;
    this.teacherService = teacherService;
  }

  @GetMapping("/showLecture")
  public String showLecture(@RequestParam("id") Long id, Model model) {
    Lecture lecture = lectureService.findById(id);
    model.addAttribute("lecture", lecture);
    return "lecture";
  }

  @GetMapping("/showCourse")
  public String showCourse(@RequestParam("id") Long id, Model model) {
    Course course = courseService.findById(id);
    model.addAttribute("course", course);
    return "course";
  }

  @GetMapping("/showTeacher/{id}")
  public String showStudent(@PathVariable("id") Long id, Model model) {
    Teacher teacher = teacherService.findById(id);
    model.addAttribute("teacher", teacher);
    return "teacher";
  }

  @GetMapping("/listLectures")
  public String listLectures(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
    Page<Lecture> pageLecture =
            lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    model.addAttribute("lectures", pageLecture.getContent());
    model.addAttribute("pageNumber", pageLecture.getNumber());
    model.addAttribute("totalPages", pageLecture.getTotalPages());
    return "manage-lecture";
  }

  @GetMapping("/listLecturesByCourse/{courseId}")
  public String listLecturesByCourseId(
          @PathVariable Long courseId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
    Page<Lecture> pageLecture =
            lectureService.findAllByCourse(
                    courseId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    Course course = courseService.findById(courseId);
    model.addAttribute("lectures", pageLecture.getContent());
    model.addAttribute("pageNumber", pageLecture.getNumber());
    model.addAttribute("totalPages", pageLecture.getTotalPages());
    model.addAttribute("course", course);
    return "course";
  }

  @GetMapping("/listLecturesByGroup/{groupId}")
  public String listLecturesByGroupId(
          @PathVariable Long groupId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
    Page<Lecture> pageLecture =
            lectureService.findAllByGroup(
                    groupId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    Group group = groupService.findById(groupId);
    model.addAttribute("group", group);
    model.addAttribute("lectures", pageLecture.getContent());
    model.addAttribute("pageNumber", pageLecture.getNumber());
    model.addAttribute("totalPages", pageLecture.getTotalPages());
    return "group";
  }
}
