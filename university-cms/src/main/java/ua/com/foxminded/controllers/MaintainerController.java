package ua.com.foxminded.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.service.MaintainerService;

@RestController
public class MaintainerController {

    private static final int DEFAULT_AMOUNT_TO_VIEW_ENTITY = 10;
    private final MaintainerService maintainerService;

    public MaintainerController(MaintainerService maintainerService) {
        this.maintainerService = maintainerService;
    }

    @GetMapping("/showMaintainer/{id}")
    public String showMaintainer(@PathVariable Long id, Model model) {
        Maintainer maintainer = maintainerService.findById(id);
        model.addAttribute("maintainer", maintainer);
        return "maintainer";
    }

    @GetMapping("/listMaintainers")
    public String listMaintainers(@RequestParam(defaultValue = "0") int pageNumber, Model model) {
        Page<Maintainer> maintainerPage = maintainerService.findAll(PageRequest.of(pageNumber, DEFAULT_AMOUNT_TO_VIEW_ENTITY));
        model.addAttribute("maintainers", maintainerPage.getContent());
        model.addAttribute("pageNumber", maintainerPage.getNumber());
        model.addAttribute("totalPages", maintainerPage.getTotalPages());
        return "list-maintainers";
    }

    @GetMapping("/createFormMaintainer")
    public String showCreateForm(Model model) {
        model.addAttribute("maintainer", new Maintainer());
        return "create-form-maintainer";
    }

    @PostMapping("/createMaintainer")
    public String createMaintainer(@ModelAttribute Maintainer maintainer, @RequestParam("imageFile") MultipartFile imageFile) {
        maintainerService.save(maintainer, imageFile);
        return "create-form-maintainer-successful";
    }

    @GetMapping("/updateFormMaintainer/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Maintainer maintainer = maintainerService.findById(id);
        model.addAttribute("maintainer", maintainer);
        return "update-form-maintainer";
    }

    @PutMapping("/updateMaintainer/{id}")
    public String updateMaintainer(@PathVariable Long id, @ModelAttribute Maintainer maintainer, @RequestParam("imageFile") MultipartFile imageFile) {
        maintainerService.update(id, maintainer, imageFile);
        return "update-form-maintainer-successful";
    }

    @DeleteMapping("/deleteMaintainer")
    public String deleteMaintainer(Long id) {
        maintainerService.delete(id);
        return "delete-form-maintainer-successful";
    }
}

