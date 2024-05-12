package ua.com.foxminded.controllers.group;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.LectureService;
import ua.com.foxminded.service.StudentService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;


@Controller
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final LectureService lectureService;

    public GroupController(GroupService groupService, LectureService lectureService) {
        this.groupService = groupService;
        this.lectureService = lectureService;
    }

    //General methods

    @GetMapping("/showGroup")
    public String showGroup(@RequestParam Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
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
    public String listGroupsToLecture(@PathVariable Long lectureId, @RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Group> pageGroups = groupService.findAllByLecture(lectureId, PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        Lecture lecture = lectureService.findById(lectureId);
        model.addAttribute("lecture", lecture);
        model.addAttribute("groups", pageGroups.getContent());
        model.addAttribute("pageNumber", pageGroups.getNumber());
        model.addAttribute("totalPages", pageGroups.getTotalPages());
        return "lecture";
    }

    @GetMapping("/list/groups")
    @ResponseBody
    public ResponseEntity<List<Group>> getGroups(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Group> groupsPage = groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Group> groups = groupsPage.getContent();
        return ResponseEntity.ok(groups);
    }

    private void fillModelWithDataGroups(int pageNumber, Model model) {
        Page<Group> pageGroups = groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("groups", pageGroups.getContent());
        model.addAttribute("pageNumber", pageGroups.getNumber());
        model.addAttribute("totalPages", pageGroups.getTotalPages());
    }

    //Manage methods

    @GetMapping("/manage")
    public String manageGroup() {
        return "manage-group";
    }

    @GetMapping("/manage/createFormGroup")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new Group());
        return "create-form-group";
    }

    @PostMapping("/manage/createGroup")
    public String createGroup(@ModelAttribute @Valid Group group) {
        groupService.save(group);
        return "create-form-group-successful";
    }

    @GetMapping("/manage/updateFormGroup/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "update-form-group";
    }

    @PostMapping("/manage/updateGroup/{id}")
    public String updateGroup(@PathVariable Long id, @ModelAttribute @Valid Group group, Model model) {
        groupService.update(id, group);
        model.addAttribute("groupId", id);
        return "update-form-group-successful";
    }

    @PostMapping("/manage/deleteGroup/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return "delete-form-group-successful";
    }

    @PostMapping("/manage/attachStudentToGroup")
    public String attachStudentToGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.attachStudentToGroup(studentId, groupId);
        return "manage-student";
    }

    @PostMapping("/manage/detachStudentFromGroup")
    public String detachStudentFromGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.detachStudentFromGroup(studentId, groupId);
        return "manage-group";
    }

}
