package ua.com.foxminded.controllers.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
public class StudentController {
  private final StudentService studentService;
  private final GroupService groupService;

  public StudentController(StudentService studentService, GroupService groupService) {
    this.studentService = studentService;
    this.groupService = groupService;
  }

  @GetMapping("/listStudentsByGroup/{groupId}")
  public String listStudentsByGroup(
          @PathVariable Long groupId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
    Page<Student> pageStudent =
            studentService.findAllStudentByGroup(
                    groupId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    Group group = groupService.findById(groupId);
    model.addAttribute("group", group);
    model.addAttribute("students", pageStudent.getContent());
    model.addAttribute("pageNumber", pageStudent.getNumber());
    model.addAttribute("totalPages", pageStudent.getTotalPages());
    return "group";
  }
}
