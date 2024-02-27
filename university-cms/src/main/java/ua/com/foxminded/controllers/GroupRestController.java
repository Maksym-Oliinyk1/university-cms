package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;


@RestController
public class GroupRestController {
    private final GroupService groupService;

    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/getListGroups")
    @ResponseBody
    public ResponseEntity<List<Group>> getGroups(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Group> groupsPage = groupService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Group> groups = groupsPage.getContent();
        return ResponseEntity.ok(groups);
    }
}
