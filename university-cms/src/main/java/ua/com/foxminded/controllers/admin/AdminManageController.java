package ua.com.foxminded.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.AdministratorService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/maintainer/manage/admin")
public class AdminManageController {

    private final AdministratorService administratorService;
    private final AuthenticationService authenticationService;

    public AdminManageController(
            AdministratorService administratorService, AuthenticationService authenticationService) {
        this.administratorService = administratorService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String manageAdmin() {
        return "manage-administrator";
    }

    @GetMapping("/showAdmin/{id}")
    public String showAdmin(@PathVariable Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        model.addAttribute("administrator", administrator);
        return "admin";
    }

    @GetMapping("/createFormAdmin")
    public String createFormAdmin(Model model) {
        model.addAttribute("administrator", new AdministratorDTO());
        return "create-form-administrator-manage";
    }

    @PostMapping("/createAdmin")
    public String createAdmin(@ModelAttribute @Valid AdministratorDTO administratorDTO) {
        authenticationService.registerAdmin(administratorDTO);
        return "create-form-administrator-successful";
    }

    @GetMapping("/updateFormAdmin/{id}")
    public String updateFormAdmin(@PathVariable("id") Long id, Model model) {
        AdministratorDTO administratorDTO = administratorService.findByIdDTO(id);
        model.addAttribute("administrator", administratorDTO);
        return "update-form-administrator-manage";
    }

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(
            @PathVariable("id") Long id, @ModelAttribute @Valid AdministratorDTO administratorDTO) {
        authenticationService.updateAdmin(id, administratorDTO);
        return "update-form-administrator-successful";
    }

    @PostMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable("id") Long id) {
        administratorService.delete(id);
        return "delete-form-administrator-successful";
    }

    @GetMapping("/listAdmins")
    public String listAdministrators(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Administrator> adminPage =
                administratorService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("administrators", adminPage.getContent());
        model.addAttribute("pageNumber", adminPage.getNumber());
        model.addAttribute("totalPages", adminPage.getTotalPages());
        return "manage-administrator";
    }
}
