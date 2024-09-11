package ua.com.foxminded.controllers.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/general/faculty")
public class FacultyController {
  private final FacultyService facultyService;

  public FacultyController(FacultyService facultyService) {
    this.facultyService = facultyService;
  }

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

  private void fillModelWithData(int pageNumber, Model model) {
    Page<Faculty> pageFaculty =
            facultyService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
    model.addAttribute("faculties", pageFaculty.getContent());
    model.addAttribute("pageNumber", pageFaculty.getNumber());
    model.addAttribute("totalPages", pageFaculty.getTotalPages());
  }
}
