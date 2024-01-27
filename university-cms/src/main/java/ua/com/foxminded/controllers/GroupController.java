package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

@RestController
public class GroupController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/showGroup/{id}")
    public String showGroup(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "group";
    }

    @GetMapping("/listGroups")
    public String listGroups(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Group> pageGroups = groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("groups", pageGroups.getTotalPages());
        model.addAttribute("pageNumber", pageGroups.getNumber());
        model.addAttribute("totalPages", pageGroups.getTotalPages());
        return "list-groups";
    }

    @GetMapping("/createFormGroup")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new Group());
        return "create-form-group";
    }

    @PostMapping("/createGroup")
    public String createGroup(@ModelAttribute Group group) {
        groupService.save(group);
        return "create-form-group-successful";
    }

    @GetMapping("/updateFormGroup/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "update-form-group";
    }

    @PutMapping("/updateGroup/{id}")
    public String updateGroup(@PathVariable Long id, @ModelAttribute Group group) {
        groupService.update(id, group);
        return "update-form-group-successful";
    }

    @DeleteMapping("/deleteMapping/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return "delete-form-group-successful";
    }

    @PostMapping("admin/manage-student/attachStudentToGroup")
    public String attachStudentToGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.attachStudentToGroup(studentId, groupId);
        return "redirect:/admin/manage-student";
    }

    @PostMapping("admin/manage-group/detachStudentFromGroup")
    public String detachStudentFromGroup(
            @RequestParam Long studentId,
            @RequestParam Long groupId) {
        groupService.detachStudentFromGroup(studentId, groupId);
        return "redirect:/admin/manage-group";
    }
}
