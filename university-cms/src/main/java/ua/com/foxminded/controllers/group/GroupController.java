package ua.com.foxminded.controllers.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.StudentService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/general/group")
public class GroupController {
  private final GroupService groupService;
  private final LectureService lectureService;
  private final StudentService studentService;

  public GroupController(
          GroupService groupService, LectureService lectureService, StudentService studentService) {
    this.groupService = groupService;
    this.lectureService = lectureService;
    this.studentService = studentService;
  }

  @GetMapping("/showGroup")
  public String showGroup(@RequestParam Long id, Model model) {
    Group group = groupService.findById(id);
    model.addAttribute("group", group);
    return "group";
  }

  @GetMapping("/showStudent/{id}")
  public String showStudent(@PathVariable("id") Long id, Model model) {
    Student student = studentService.findById(id);
    model.addAttribute("student", student);
    return "group";
  }

  @GetMapping("/listGroups")
  public String listGroups(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
    fillModelWithDataGroups(pageNumber, model);
    return "manage-group";
  }

  @GetMapping("/listGroupsToStudent")
  public String listGroupsToStudent(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
    fillModelWithDataGroups(pageNumber, model);
    return "create-form-student";
  }

  @GetMapping("/listGroupsByLecture/{lectureId}")
  public String listGroupsToLecture(
          @PathVariable Long lectureId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
    Page<Group> pageGroups =
            groupService.findAllByLecture(
                    lectureId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    Lecture lecture = lectureService.findById(lectureId);
    model.addAttribute("lecture", lecture);
    model.addAttribute("groups", pageGroups.getContent());
    model.addAttribute("pageNumber", pageGroups.getNumber());
    model.addAttribute("totalPages", pageGroups.getTotalPages());
    return "lecture";
  }

  private void fillModelWithDataGroups(int pageNumber, Model model) {
    Page<Group> pageGroups =
            groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    model.addAttribute("groups", pageGroups.getContent());
    model.addAttribute("pageNumber", pageGroups.getNumber());
    model.addAttribute("totalPages", pageGroups.getTotalPages());
  }
}
