package ua.com.foxminded.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.controllers.AdminController;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.service.AdministratorService;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController {

  private final AdministratorService administratorService;

  public AdminControllerImpl(AdministratorService administratorService) {
    this.administratorService = administratorService;
  }

  @PostMapping
  public ResponseEntity<AdministratorDTO> save(
          @RequestBody @Valid AdministratorDTO administratorDTO) {
    AdministratorDTO savedAdministrator = administratorService.save(administratorDTO);
    return new ResponseEntity<>(savedAdministrator, HttpStatus.CREATED);
  }

  @PutMapping("/{adminId}")
  public ResponseEntity<AdministratorDTO> update(
          @RequestBody @Valid AdministratorDTO administratorDTO, @PathVariable UUID adminId) {
    AdministratorDTO updatedAdminDto = administratorService.update(adminId, administratorDTO);
    return new ResponseEntity<>(updatedAdminDto, HttpStatus.OK);
  }

  @GetMapping("/{adminId}")
  public ResponseEntity<AdministratorDTO> get(@PathVariable UUID adminId) {
    AdministratorDTO admin = administratorService.findById(adminId);
    return new ResponseEntity<>(admin, HttpStatus.OK);
  }

  @DeleteMapping("/{adminId}")
  public ResponseEntity<Void> delete(@PathVariable UUID adminId) {
    administratorService.delete(adminId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
