package ua.com.foxminded.controllers.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.*;
import ua.com.foxminded.service.*;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@RestController
@RequestMapping("/getList")
public class ListController {
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final LectureService lectureService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public ListController(
            FacultyService facultyService,
            GroupService groupService,
            LectureService lectureService,
            TeacherService teacherService,
            CourseService courseService) {
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.lectureService = lectureService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping("/faculties")
    @ResponseBody
    public ResponseEntity<List<Faculty>> getFaculties(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Faculty> facultyPage =
                facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Faculty> faculties = facultyPage.getContent();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/groups")
    @ResponseBody
    public ResponseEntity<List<Group>> getGroups(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Group> groupsPage =
                groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Group> groups = groupsPage.getContent();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/lectures")
    @ResponseBody
    public ResponseEntity<List<Lecture>> getLectures(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Lecture> lecturesPage =
                lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Lecture> lectures = lecturesPage.getContent();
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/teachers")
    @ResponseBody
    public ResponseEntity<List<Teacher>> getTeachers(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Teacher> teachersPage =
                teacherService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Teacher> teachers = teachersPage.getContent();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<List<Course>> getCourses(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Course> coursesPage =
                courseService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Course> courses = coursesPage.getContent();
        return ResponseEntity.ok(courses);
    }
}
