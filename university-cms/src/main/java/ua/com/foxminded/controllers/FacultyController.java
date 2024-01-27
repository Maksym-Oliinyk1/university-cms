package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

@RestController
public class FacultyController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping("/showFaculty/{id}")
    public String showFaculty(@PathVariable Long id, Model model) {
        Faculty faculty = facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "faculty";
    }

    @GetMapping("/listFaculties")
    public String listFaculties(@RequestParam int pageNumber, Model model) {
        Page<Faculty> pageFaculty = facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("faculties", pageFaculty.getContent());
        model.addAttribute("pageNumber", pageFaculty.getNumber());
        model.addAttribute("totalPages", pageFaculty.getTotalPages());
        return "list-faculties";
    }

    @GetMapping("/createFormFaculty")
    public String showCreateForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "create-form-faculty";
    }

    @PostMapping("/createFaculty")
    public String createFaculty(@ModelAttribute Faculty faculty) {
        facultyService.save(faculty);
        return "create-form-faculty-successful";
    }

    @GetMapping("/updateFormFaculty/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Faculty faculty = facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "update-form-faculty";
    }

    @PutMapping("/updateFaculty/{id}")
    public String updateFaculty(@PathVariable Long id, @ModelAttribute Faculty faculty) {
        facultyService.update(id, faculty);
        return "update-form-faculty-successful";
    }

    @DeleteMapping("/deleteFaculty/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return "delete-form-successful";
    }

    @PostMapping("/attachCourseToFaculty")
    public ResponseEntity<String> attachCourseToFaculty(
            @RequestParam Long courseId,
            @RequestParam Long facultyId) {
        facultyService.attachCourseToFaculty(courseId, facultyId);
        return ResponseEntity.ok("Course attached to faculty successfully");
    }

    @PostMapping("/detachCourseFromFaculty")
    public ResponseEntity<String> detachCourseFromFaculty(
            @RequestParam Long courseId,
            @RequestParam Long facultyId) {
        facultyService.detachCourseFromFaculty(courseId, facultyId);
        return ResponseEntity.ok("Course detached from faculty successfully");
    }
}
