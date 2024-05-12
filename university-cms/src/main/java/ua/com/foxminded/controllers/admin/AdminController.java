package ua.com.foxminded.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.security.AuthenticationResponse;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.AdministratorService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;
import static ua.com.foxminded.utill.UtilController.extractToken;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdministratorService administratorService;
    private final JwtService jwtService;

    public AdminController(AdministratorService administratorService,
                           JwtService jwtService) {
        this.administratorService = administratorService;
        this.jwtService = jwtService;
    }

    //General methods

    @GetMapping("/showAdmin")
    public String showAdministrator(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
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
    public String showUpdateForm(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        AdministratorDTO administratorDTO = administratorService.findByIdDTO(tokenId);
        administratorDTO.setId(tokenId);
        model.addAttribute("administrator", administratorDTO);
        return "update-form-administrator";
    }

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(@PathVariable Long id, @ModelAttribute @Valid AdministratorDTO administratorDTO, Model model) {
        administratorService.update(id, administratorDTO);
        model.addAttribute("adminId", id);
        return "update-form-administrator-successful";
    }

    @PostMapping("/deleteAdmin")
    public String deleteAdmin(@RequestHeader("Authorization") String authToken) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        administratorService.delete(tokenId);
        return "delete-form-administrator-successful";
    }

    //Manage methods

    @GetMapping("/manage")
    public String manageAdmin() {
        return "manage-administrator";
    }

    @PostMapping("/manage/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @ModelAttribute @Valid AdministratorDTO administratorDTO
    ) {
        String token = jwtService.generateToken(administratorService.save(administratorDTO));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/manage/showAdmin/{id}")
    public String showAdmin(@PathVariable Long id, Model model) {
        Administrator administrator = administratorService.findById(id);
        model.addAttribute("administrator", administrator);
        return "admin";
    }

    @GetMapping("/manage/createFormAdmin")
    public String createFormAdmin(Model model) {
        model.addAttribute("administrator", new AdministratorDTO());
        return "create-form-administrator-manage";
    }

    @PostMapping("/manage/createAdmin")
    public String createAdmin(@ModelAttribute @Valid AdministratorDTO administratorDTO) {
        administratorService.save(administratorDTO);
        return "create-form-administrator-successful";
    }

    @GetMapping("/manage/updateFormAdmin/{id}")
    public String updateFormAdmin(@PathVariable("id") Long id, Model model) {
        AdministratorDTO administratorDTO = administratorService.findByIdDTO(id);
        model.addAttribute("administrator", administratorDTO);
        return "update-form-administrator-manage";
    }

    @PostMapping("/manage/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable("id") Long id) {
        administratorService.delete(id);
        return "delete-form-administrator-successful";
    }

    @GetMapping("/manage/listAdmins")
    public String listAdministrators(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Administrator> adminPage = administratorService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("administrators", adminPage.getContent());
        model.addAttribute("pageNumber", adminPage.getNumber());
        model.addAttribute("totalPages", adminPage.getTotalPages());
        return "manage-administrator";
    }
}
