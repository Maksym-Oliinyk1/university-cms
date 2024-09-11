package ua.com.foxminded.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.service.MaintainerService;

import static ua.com.foxminded.utill.UtilController.DEFAULT_AMOUNT_TO_VIEW_ENTITY;

@Controller
public class MaintainerController {

    private final MaintainerService maintainerService;

    public MaintainerController(MaintainerService maintainerService) {
        this.maintainerService = maintainerService;
    }

    @GetMapping("/maintainerAuthorization")
    public String maintainerAuthorization() {
        return "mock-maintainer-authorization";
    }

    @GetMapping("/showMaintainer")
    public String showAdministrator(@RequestParam("id") Long id, Model model) {
        Maintainer maintainer = maintainerService.findById(id);
        model.addAttribute("maintainer", maintainer);
        return "maintainer";
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

    @GetMapping("/createFormMaintainer")
    public String showCreateForm(Model model) {
        model.addAttribute("maintainer", new MaintainerDTO());
        return "create-form-maintainer";
    }

    @PostMapping("/createMaintainer")
    public String createMaintainer(@ModelAttribute @Valid MaintainerDTO maintainerDTO) {
        maintainerService.save(maintainerDTO);
        return "create-form-maintainer-successful";
    }

    @GetMapping("/updateFormMaintainer/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        MaintainerDTO maintainerDTO = maintainerService.findByIdDTO(id);
        maintainerDTO.setId(id);
        model.addAttribute("maintainer", maintainerDTO);
        return "update-form-maintainer";
    }

    @PostMapping("/updateMaintainer/{id}")
    public String updateMaintainer(
            @PathVariable Long id, @ModelAttribute @Valid MaintainerDTO maintainerDTO, Model model) {
        maintainerService.update(id, maintainerDTO);
        model.addAttribute("maintainerId", id);
        return "update-form-maintainer-successful";
    }

    @PostMapping("/deleteMaintainer")
    public String deleteMaintainer(Long id) {
        maintainerService.delete(id);
        return "delete-form-maintainer-successful";
    }
}
