package ua.com.foxminded.controllers.lecture;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Course;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.service.CourseService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/lecture")
public class LectureController {
    private final LectureService lectureService;
    private final CourseService courseService;
    private final GroupService groupService;

    public LectureController(
            LectureService lectureService, CourseService courseService, GroupService groupService) {
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.groupService = groupService;
    }

    // General methods

    @GetMapping("/showLecture")
    public String showLecture(@RequestParam("id") Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "lecture";
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

    @GetMapping("/list/lectures")
    @ResponseBody
    public ResponseEntity<List<Lecture>> getLectures(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Lecture> lecturesPage =
                lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Lecture> lectures = lecturesPage.getContent();
        return ResponseEntity.ok(lectures);
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

    // Manage methods

    @GetMapping("/manage")
    public String manageLecture() {
        return "manage-lecture";
    }

    @GetMapping("/manage/createFormLecture")
    public String showCreateForm(Model model) {
        model.addAttribute("lecture", new Lecture());
        return "create-form-lecture";
    }

    @GetMapping("/manage/listLectures")
    public String listLectures(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Lecture> pageLecture =
                lectureService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("lectures", pageLecture.getContent());
        model.addAttribute("pageNumber", pageLecture.getNumber());
        model.addAttribute("totalPages", pageLecture.getTotalPages());
        return "manage-lecture";
    }

    @PostMapping("/manage/createLecture")
    public String createLecture(@ModelAttribute @Valid Lecture lecture) {
        lectureService.save(lecture);
        return "create-form-lecture-successful";
    }

    @GetMapping("/manage/updateFormLecture/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("lecture", lecture);
        return "update-form-lecture";
    }

    @PostMapping("/manage/updateLecture/{id}")
    public String updateLecture(
            @PathVariable Long id, @ModelAttribute @Valid Lecture lecture, Model model) {
        lectureService.update(id, lecture);
        model.addAttribute("lectureId", id);
        return "update-form-lecture-successful";
    }

    @PostMapping("/manage/deleteLecture/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.delete(id);
        return "delete-form-lecture-successful";
    }

    @PostMapping("/manage/attachGroupToLecture")
    public ResponseEntity<String> attachGroupToLecture(
            @RequestParam Long groupId, @RequestParam Long lectureId) {
        lectureService.attachGroupToLecture(groupId, lectureId);
        return ResponseEntity.ok("Group attached to lecture successfully");
    }

    @PostMapping("/manage/detachGroupFromLecture")
    public ResponseEntity<String> detachGroupFromLecture(
            @RequestParam Long groupId, @RequestParam Long lectureId) {
        lectureService.detachGroupFromLecture(groupId, lectureId);
        return ResponseEntity.ok("Group detached from lecture successfully");
    }
}
