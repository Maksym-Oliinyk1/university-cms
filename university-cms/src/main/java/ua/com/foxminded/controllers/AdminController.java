package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.service.AdministratorService;

@Controller
public class AdminController {

    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;

    private final AdministratorService administratorService;

    public AdminController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/adminAuthorization")
    public String adminAuthorization() {
        return "mock-admin-authorization";
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
        return "list-admins";
    }

    @GetMapping("/createFormAdmin")
    public String showCreateForm(Model model) {
        model.addAttribute("administrator", new Administrator());
        return "create-form-administrator";
    }

    @PostMapping("/createAdmin")
    public String createAdmin(@ModelAttribute Administrator administrator, @RequestParam("imageFile") MultipartFile imageFile) {
        administratorService.save(administrator, imageFile);
        return "create-form-administrator-successful";
    }

    @GetMapping("/updateFormAdmin/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        model.addAttribute("administrator", administrator);
        return "update-form-administrator";
    }

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(@PathVariable Long id, @ModelAttribute Administrator administrator, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        administratorService.update(id, administrator, imageFile);
        model.addAttribute("adminId", id);
        return "update-form-administrator-successful";
    }

    @PutMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        administratorService.delete(id);
        return "delete-form-administrator-successful";
    }
}
