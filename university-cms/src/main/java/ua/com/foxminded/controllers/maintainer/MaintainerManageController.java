package ua.com.foxminded.controllers.maintainer;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.MaintainerService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
@RequestMapping("/maintainer/manage")
public class MaintainerManageController {

    private final MaintainerService maintainerService;
    private final AuthenticationService authenticationService;

    public MaintainerManageController(
            MaintainerService maintainerService, AuthenticationService authenticationService) {
        this.maintainerService = maintainerService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String manageMaintainer() {
        return "manage-maintainer";
    }

    @GetMapping("/showMaintainer/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Maintainer maintainer = maintainerService.findById(id);
        model.addAttribute("maintainer", maintainer);
        return "maintainer";
    }

    @GetMapping("/createFormMaintainer")
    public String createFormMaintainer(Model model) {
        model.addAttribute("maintainer", new MaintainerDTO());
        return "create-form-maintainer-manage";
    }

    @PostMapping("/createMaintainer")
    public String createStudent(@ModelAttribute @Valid MaintainerDTO maintainerDTO) {
        authenticationService.registerMaintainer(maintainerDTO);
        return "create-form-maintainer-successful";
    }

    @GetMapping("/updateFormMaintainer/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        MaintainerDTO maintainerDTO = maintainerService.findByIdDTO(id);
        maintainerDTO.setId(id);
        model.addAttribute("maintainer", maintainerDTO);
        return "update-form-maintainer-manage";
    }

    @PostMapping("/updateMaintainer/{id}")
    public String updateStudent(
            @PathVariable("id") Long id, @ModelAttribute @Valid MaintainerDTO maintainerDTO) {
        authenticationService.updateMaintainer(id, maintainerDTO);
        return "update-form-maintainer-successful";
    }

    @GetMapping("/listMaintainers")
    public String listMaintainers(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Maintainer> maintainerPage =
                maintainerService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("maintainers", maintainerPage.getContent());
        model.addAttribute("pageNumber", maintainerPage.getNumber());
        model.addAttribute("totalPages", maintainerPage.getTotalPages());
        return "manage-maintainer";
    }

    @PostMapping("/deleteMaintainer/{id}")
    public String deleteMaintainer(@PathVariable("id") Long id) {
        maintainerService.delete(id);
        return "delete-form-maintainer-successful";
    }
}
