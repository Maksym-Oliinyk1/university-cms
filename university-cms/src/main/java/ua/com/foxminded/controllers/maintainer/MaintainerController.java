package ua.com.foxminded.controllers.maintainer;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.security.AuthenticationResponse;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.MaintainerService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;
import static ua.com.foxminded.utill.UtilController.extractToken;

@Controller
@RequestMapping("/maintainer")
public class MaintainerController {

    private final MaintainerService maintainerService;
    private final JwtService jwtService;

    public MaintainerController(MaintainerService maintainerService,
                                JwtService jwtService) {
        this.maintainerService = maintainerService;
        this.jwtService = jwtService;
    }

    //General methods

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerMaintainer(
            @ModelAttribute @Valid MaintainerDTO maintainerDTO
    ) {
        String token = jwtService.generateToken(maintainerService.save(maintainerDTO));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/showMaintainer")
    public String showAdministrator(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        Maintainer maintainer = maintainerService.findById(tokenId);
        model.addAttribute("maintainer", maintainer);
        return "maintainer";
    }

    @GetMapping("/updateFormMaintainer")
    public String showUpdateForm(@RequestHeader("Authorization") String authToken, Model model) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        MaintainerDTO maintainerDTO = maintainerService.findByIdDTO(tokenId);
        maintainerDTO.setId(tokenId);
        model.addAttribute("maintainer", maintainerDTO);
        return "update-form-maintainer";
    }

    @PostMapping("/updateMaintainer/{id}")
    public String updateMaintainer(@PathVariable Long id, @ModelAttribute @Valid MaintainerDTO maintainerDTO, Model model) {
        maintainerService.update(id, maintainerDTO);
        model.addAttribute("maintainerId", id);
        return "update-form-maintainer-successful";
    }

    @GetMapping("/createFormMaintainer")
    public String showCreateForm(Model model) {
        model.addAttribute("maintainer", new MaintainerDTO());
        return "create-form-maintainer";
    }


    @PostMapping("/deleteMaintainer")
    public String deleteMaintainer(@RequestHeader("Authorization") String authToken) {
        String token = extractToken(authToken);
        Long tokenId = jwtService.extractUserId(token);
        maintainerService.delete(tokenId);
        return "delete-form-maintainer-successful";
    }

    //Manage methods

    @GetMapping("/manage")
    public String manageMaintainer() {
        return "manage-maintainer";
    }

    @GetMapping("/manage/showMaintainer/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Maintainer maintainer = maintainerService.findById(id);
        model.addAttribute("maintainer", maintainer);
        return "maintainer";
    }

    @GetMapping("/manage/createFormMaintainer")
    public String createFormMaintainer(Model model) {
        model.addAttribute("maintainer", new MaintainerDTO());
        return "create-form-maintainer-manage";
    }

    @PostMapping("/manage/createMaintainer")
    public String createStudent(@ModelAttribute @Valid MaintainerDTO maintainerDTO) {
        maintainerService.save(maintainerDTO);
        return "create-form-maintainer-successful";
    }

    @GetMapping("/manage/updateFormMaintainer/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        MaintainerDTO maintainerDTO = maintainerService.findByIdDTO(id);
        maintainerDTO.setId(id);
        model.addAttribute("maintainer", maintainerDTO);
        return "update-form-maintainer-manage";
    }

    @GetMapping("/manage/listMaintainers")
    public String listMaintainers(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Maintainer> maintainerPage = maintainerService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("maintainers", maintainerPage.getContent());
        model.addAttribute("pageNumber", maintainerPage.getNumber());
        model.addAttribute("totalPages", maintainerPage.getTotalPages());
        return "manage-maintainer";
    }

    @PostMapping("/manage/deleteMaintainer/{id}")
    public String deleteMaintainer(@PathVariable("id") Long id) {
        maintainerService.delete(id);
        return "delete-form-maintainer-successful";
    }

}
