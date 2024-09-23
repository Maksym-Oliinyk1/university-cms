package ua.com.foxminded.controllers.maintainer;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.security.JwtService;
import ua.com.foxminded.service.MaintainerService;

@Controller
@RequestMapping("/general/maintainer")
public class MaintainerGeneralController {

  private final MaintainerService maintainerService;
  private final JwtService jwtService;
  private final AuthenticationService authenticationService;

  public MaintainerGeneralController(
          MaintainerService maintainerService,
          JwtService jwtService,
          AuthenticationService authenticationService) {
    this.maintainerService = maintainerService;
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/showMaintainer")
  public String showAdministrator(@RequestParam("token") String token, Model model) {
    Long tokenId = jwtService.extractUserId(token);
    Maintainer maintainer = maintainerService.findById(tokenId);
    model.addAttribute("maintainer", maintainer);
    return "maintainer";
  }

  @GetMapping("/updateFormMaintainer")
  public String showUpdateForm(@RequestParam("token") String token, Model model) {
    Long tokenId = jwtService.extractUserId(token);
    MaintainerDTO maintainerDTO = maintainerService.findByIdDTO(tokenId);
    maintainerDTO.setId(tokenId);
    model.addAttribute("maintainer", maintainerDTO);
    return "update-form-maintainer";
  }

  @PostMapping("/updateMaintainer/{id}")
  public String updateMaintainer(
          @PathVariable Long id, @ModelAttribute @Valid MaintainerDTO maintainerDTO, Model model) {
    authenticationService.updateMaintainer(id, maintainerDTO);
    model.addAttribute("maintainerId", id);
    return "update-form-maintainer-successful";
  }

  @GetMapping("/createFormMaintainer")
  public String showCreateForm(Model model) {
    model.addAttribute("maintainer", new MaintainerDTO());
    return "create-form-maintainer";
  }

  @PostMapping("/deleteMaintainer")
  public String deleteMaintainer(@RequestParam("token") String token) {
    Long tokenId = jwtService.extractUserId(token);
    maintainerService.delete(tokenId);
    return "delete-form-maintainer-successful";
  }
}
