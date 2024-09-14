package ua.com.foxminded.controllers.group;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

@Controller
@RequestMapping("/manage/group")
public class GroupManageController {

    private final GroupService groupService;

    public GroupManageController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("")
    public String manageGroup() {
        return "manage-group";
    }

    @GetMapping("/createFormGroup")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new Group());
        return "create-form-group";
    }

    @PostMapping("/createGroup")
    public String createGroup(@ModelAttribute @Valid Group group) {
        groupService.save(group);
        return "create-form-group-successful";
    }

    @GetMapping("/updateFormGroup/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "update-form-group";
    }

    @PostMapping("/updateGroup/{id}")
    public String updateGroup(
            @PathVariable Long id, @ModelAttribute @Valid Group group, Model model) {
        groupService.update(id, group);
        model.addAttribute("groupId", id);
        return "update-form-group-successful";
    }

    @PostMapping("/deleteGroup/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return "delete-form-group-successful";
    }

    @PostMapping("/attachStudentToGroup")
    public String attachStudentToGroup(@RequestParam Long studentId, @RequestParam Long groupId) {
        groupService.attachStudentToGroup(studentId, groupId);
        return "manage-student";
    }

    @PostMapping("/detachStudentFromGroup")
    public String detachStudentFromGroup(@RequestParam Long studentId, @RequestParam Long groupId) {
        groupService.detachStudentFromGroup(studentId, groupId);
        return "manage-group";
    }
}
