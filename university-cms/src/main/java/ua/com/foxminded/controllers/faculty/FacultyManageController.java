package ua.com.foxminded.controllers.faculty;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

@Controller
@RequestMapping("/manage/faculty")
public class FacultyManageController {

  private final FacultyService facultyService;

  public FacultyManageController(FacultyService facultyService) {
    this.facultyService = facultyService;
  }

  @GetMapping("")
  public String manageFaculty() {
    return "manage-faculty";
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

  @PostMapping("/updateFaculty/{id}")
  public String updateFaculty(@PathVariable Long id, @ModelAttribute Faculty faculty) {
    facultyService.update(id, faculty);
    return "update-form-faculty-successful";
  }

  @PostMapping("/deleteFaculty/{id}")
  public String deleteFaculty(@PathVariable Long id) {
    facultyService.delete(id);
    return "delete-form-faculty-successful";
  }
}
