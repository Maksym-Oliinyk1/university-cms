package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.security.AuthenticationService;
import ua.com.foxminded.service.MaintainerService;

@Service
public class MaintainerGenerator extends DataGenerator {

  private static final Logger logger = LoggerFactory.getLogger(MaintainerGenerator.class);

  private static final int AMOUNT_OF_MAINTAINERS = 10;

  private final MaintainerService maintainerService;
  private final AuthenticationService authenticationService;

  public MaintainerGenerator(
          MaintainerService maintainerService, AuthenticationService authenticationService) {
    this.maintainerService = maintainerService;
    this.authenticationService = authenticationService;
  }

  public void generateIfEmpty() {
    if (maintainerService.count() == 0) {
      generateMaintainers();
    }
  }

  @Override
  public int getOrder() {
    return 1;
  }

  private void generateMaintainers() {
    for (int i = 0; i < AMOUNT_OF_MAINTAINERS; i++) {
      MaintainerDTO maintainerDTO = new MaintainerDTO();
      fillUserFields(maintainerDTO);
      maintainerDTO.setAuthority(Authorities.MAINTAINER);
      logger.info(
              "Created maintainer: {} {}", maintainerDTO.getFirstName(), maintainerDTO.getLastName());
      authenticationService.registerMaintainer(maintainerDTO);
    }
  }
}
