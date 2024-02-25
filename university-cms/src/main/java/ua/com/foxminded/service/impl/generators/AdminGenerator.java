package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.service.AdministratorService;

@Service
public class AdminGenerator extends DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AdminGenerator.class);

    private static final int AMOUNT_OF_ADMINS = 15;

    private final AdministratorService adminService;


    public AdminGenerator(AdministratorService adminService) {
        this.adminService = adminService;
    }

    public void generateAdministratorsIfEmpty() {
        if (adminService.count() == 0) {
            generateAdmins();
        }
    }

    private void generateAdmins() {
        for (int i = 0; i < AMOUNT_OF_ADMINS; i++) {
            AdministratorDTO administratorDTO = new AdministratorDTO();
            fillUserFields(administratorDTO);
            logger.info("Created admin: {} {}", administratorDTO.getFirstName(), administratorDTO.getLastName());
            adminService.save(administratorDTO);
        }
    }
}

