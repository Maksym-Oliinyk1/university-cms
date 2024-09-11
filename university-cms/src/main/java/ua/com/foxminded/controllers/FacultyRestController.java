package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@RestController
public class FacultyRestController {
    private final FacultyService facultyService;

    public FacultyRestController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/getListFaculties")
    @ResponseBody
    public ResponseEntity<List<Faculty>> getFaculties(
            @RequestParam(defaultValue = "0") int pageNumber) {
        Page<Faculty> facultyPage =
                facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Faculty> faculties = facultyPage.getContent();
        return ResponseEntity.ok(faculties);
    }
}
