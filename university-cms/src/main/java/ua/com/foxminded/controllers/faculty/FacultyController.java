package ua.com.foxminded.controllers.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

import java.util.List;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    //General methods

    @GetMapping("/showFaculty")
    public String showFaculty(@RequestParam("id") Long id, Model model) {
        Faculty faculty = facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "faculty";
    }

    @GetMapping("/listFaculties")
    public String listFaculties(@RequestParam int pageNumber, Model model) {
        fillModelWithData(pageNumber, model);
        return "manage-faculty";
    }

    @GetMapping("/listFacultiesToCourse")
    public String listFacultiesToCourse(@RequestParam int pageNumber, Model model) {
        fillModelWithData(pageNumber, model);
        return "create-form-course";
    }

    @GetMapping("/listFacultiesToCourseUpdate")
    public String listFacultiesToCourseUpdate(@RequestParam int pageNumber, Model model) {
        fillModelWithData(pageNumber, model);
        return "update-form-course";
    }

    @GetMapping("/list/faculties")
    @ResponseBody
    public ResponseEntity<List<Faculty>> getFaculties(@RequestParam(defaultValue = "0") int pageNumber) {
        Page<Faculty> facultyPage = facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        List<Faculty> faculties = facultyPage.getContent();
        return ResponseEntity.ok(faculties);
    }

    private void fillModelWithData(int pageNumber, Model model) {
        Page<Faculty> pageFaculty = facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("faculties", pageFaculty.getContent());
        model.addAttribute("pageNumber", pageFaculty.getNumber());
        model.addAttribute("totalPages", pageFaculty.getTotalPages());
    }

    //Manage methods

    @GetMapping("/manage")
    public String manageFaculty() {
        return "manage-faculty";
    }

    @GetMapping("/manage/createFormFaculty")
    public String showCreateForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "create-form-faculty";
    }

    @PostMapping("/manage/createFaculty")
    public String createFaculty(@ModelAttribute Faculty faculty) {
        facultyService.save(faculty);
        return "create-form-faculty-successful";
    }

    @GetMapping("/manage/updateFormFaculty/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Faculty faculty = facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "update-form-faculty";
    }

    @PostMapping("/manage/updateFaculty/{id}")
    public String updateFaculty(@PathVariable Long id, @ModelAttribute Faculty faculty) {
        facultyService.update(id, faculty);
        return "update-form-faculty-successful";
    }

    @PostMapping("/manage/deleteFaculty/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return "delete-form-faculty-successful";
    }
}
