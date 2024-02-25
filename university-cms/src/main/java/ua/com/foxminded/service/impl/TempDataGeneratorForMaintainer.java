package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.service.MaintainerService;

@Service
public class TempDataGeneratorForMaintainer {
    private final MaintainerService maintainerService;

    @Autowired
    public TempDataGeneratorForMaintainer(MaintainerService maintainerService) {
        this.maintainerService = maintainerService;
    }

    @PostConstruct
    public void init() {
        Maintainer sampleMaintainer = new Maintainer();
        sampleMaintainer.setFirstName("John");
        sampleMaintainer.setLastName("Doe");
        sampleMaintainer.setGender(Gender.MALE);
        sampleMaintainer.setAge(30);
        sampleMaintainer.setEmail("john.doe@example.com");

        maintainerService.save(sampleMaintainer, null);

    }
}
