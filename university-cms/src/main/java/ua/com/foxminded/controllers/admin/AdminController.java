package ua.com.foxminded.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.service.AdministratorService;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final AdministratorService administratorService;

  public AdminController(AdministratorService administratorService) {
    this.administratorService = administratorService;
  }

  @PostMapping
  public ResponseEntity<AdministratorDTO> save(
          @RequestBody @Valid AdministratorDTO administratorDTO) {
    AdministratorDTO savedAdministrator = administratorService.save(administratorDTO);
    return new ResponseEntity<>(savedAdministrator, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<AdministratorDTO> update(
          @RequestBody @Valid AdministratorDTO administratorDTO, @PathVariable @Valid UUID adminId) {
    AdministratorDTO updatedAdminDto = administratorService.update(adminId, administratorDTO);
    return new ResponseEntity<>(updatedAdminDto, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<AdministratorDTO> get(@PathVariable @Valid UUID adminId) {
    AdministratorDTO admin = administratorService.findByIdDTO(adminId);
    return new ResponseEntity<>(admin, HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@PathVariable @Valid UUID adminId) {
    administratorService.delete(adminId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
