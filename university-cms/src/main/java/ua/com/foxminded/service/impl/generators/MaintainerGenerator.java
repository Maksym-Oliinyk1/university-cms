package ua.com.foxminded.service.impl.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.service.MaintainerService;

@Service
public class MaintainerGenerator extends DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MaintainerGenerator.class);

    private static final int AMOUNT_OF_MAINTAINERS = 10;

    private final MaintainerService maintainerService;

    public MaintainerGenerator(MaintainerService maintainerService) {
        this.maintainerService = maintainerService;
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
            logger.info(
                    "Created maintainer: {} {}", maintainerDTO.getFirstName(), maintainerDTO.getLastName());
            maintainerService.save(maintainerDTO);
        }
    }
}
