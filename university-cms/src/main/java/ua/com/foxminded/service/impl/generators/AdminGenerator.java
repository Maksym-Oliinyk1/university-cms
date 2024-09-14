package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.AdministratorService;

@Service
public class AdminGenerator extends DataGenerator {

  private static final Logger logger = LoggerFactory.getLogger(AdminGenerator.class);

  private static final int AMOUNT_OF_ADMINS = 15;

  private final AdministratorService adminService;
  private final AuthenticationService authenticationService;

  public AdminGenerator(
          AdministratorService adminService, AuthenticationService authenticationService) {
    this.adminService = adminService;
    this.authenticationService = authenticationService;
  }

  public void generateIfEmpty() {
    if (adminService.count() == 0) {
      generateAdmins();
    }
  }

  @Override
  public int getOrder() {
    return 2;
  }

  private void generateAdmins() {
    for (int i = 0; i < AMOUNT_OF_ADMINS; i++) {
      AdministratorDTO administratorDTO = new AdministratorDTO();
      fillUserFields(administratorDTO);
      administratorDTO.setAuthority(Authorities.ADMINISTRATOR);
      logger.info(
              "Created admin: {} {}", administratorDTO.getFirstName(), administratorDTO.getLastName());
      authenticationService.registerAdmin(administratorDTO);
    }
  }
}
