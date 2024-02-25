package ua.com.foxminded.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.UserMapper;

@Controller
public class AdminController {
    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final AdministratorService administratorService;
    private final UserMapper userMapper;

    public AdminController(AdministratorService administratorService, UserMapper userMapper) {
        this.administratorService = administratorService;
        this.userMapper = userMapper;
    }

    @GetMapping("/adminAuthorization")
    public String adminAuthorization() {
        return "mock-admin-authorization";
    }

    @GetMapping("/manageAdmin")
    public String manageAdmin() {
        return "manage-administrator";
    }

    @GetMapping("/showAdmin")
    public String showAdministrator(@RequestParam("id") Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        model.addAttribute("administrator", administrator);
        return "admin";
    }

    @GetMapping("/listAdmins")
    public String listAdministrators(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Administrator> adminPage = administratorService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("administrators", adminPage.getContent());
        model.addAttribute("pageNumber", adminPage.getNumber());
        model.addAttribute("totalPages", adminPage.getTotalPages());
        return "manage-administrator";
    }

    @GetMapping("/createFormAdmin")
    public String showCreateForm(Model model) {
        model.addAttribute("administrator", new AdministratorDTO());
        return "create-form-administrator";
    }

    @PostMapping("/createAdmin")
    public String createAdmin(@ModelAttribute @Valid AdministratorDTO administratorDTO) {
        administratorService.save(administratorDTO);
        return "create-form-administrator-successful";
    }

    @GetMapping("/updateFormAdmin/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        AdministratorDTO administratorDTO = userMapper.mapToDto(administrator);
        administratorDTO.setId(id);
        model.addAttribute("administrator", administratorDTO);
        return "update-form-administrator";
    }

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(@PathVariable Long id, @ModelAttribute @Valid AdministratorDTO administratorDTO, Model model) {
        administratorService.update(id, administratorDTO);
        model.addAttribute("adminId", id);
        return "update-form-administrator-successful";
    }

    @PostMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        administratorService.delete(id);
        return "delete-form-administrator-successful";
    }
}
