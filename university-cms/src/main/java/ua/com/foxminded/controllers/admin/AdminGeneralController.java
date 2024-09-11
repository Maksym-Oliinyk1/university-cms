package ua.com.foxminded.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.AdministratorService;

@Controller
@RequestMapping("/admin")
public class AdminGeneralController {
    private final AdministratorService administratorService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AdminGeneralController(
            AdministratorService administratorService,
            JwtService jwtService,
            AuthenticationService authenticationService) {
        this.administratorService = administratorService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/showAdmin")
    public String showAdministrator(@RequestParam("token") String token, Model model) {
        Long tokenId = jwtService.extractUserId(token);
        Administrator administrator = administratorService.findById(tokenId);
        model.addAttribute("administrator", administrator);
        return "admin";
    }

    @GetMapping("/createFormAdmin")
    public String showCreateForm(Model model) {
        model.addAttribute("administrator", new AdministratorDTO());
        return "create-form-administrator";
    }

    @GetMapping("/updateFormAdmin")
    public String showUpdateForm(@RequestParam String token, Model model) {
        Long tokenId = jwtService.extractUserId(token);
        AdministratorDTO administratorDTO = administratorService.findByIdDTO(tokenId);
        administratorDTO.setId(tokenId);
        model.addAttribute("administrator", administratorDTO);
        return "update-form-administrator";
    }

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(
            @PathVariable Long id,
            @ModelAttribute @Valid AdministratorDTO administratorDTO,
            Model model) {
        authenticationService.updateAdmin(id, administratorDTO);
        model.addAttribute("adminId", id);
        return "update-form-administrator-successful";
    }

    @PostMapping("/deleteAdmin")
    public String deleteAdmin(@RequestParam("token") String token) {
        Long tokenId = jwtService.extractUserId(token);
        administratorService.delete(tokenId);
        return "delete-form-administrator-successful";
    }
}
