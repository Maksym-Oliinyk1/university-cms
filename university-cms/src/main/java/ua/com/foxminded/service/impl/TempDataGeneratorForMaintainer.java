package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.UserMapperImpl;

import java.time.LocalDate;

@Service
public class TempDataGeneratorForMaintainer {
    private final MaintainerService maintainerService;
    private final UserMapperImpl userMapperImpl;

    @Autowired
    public TempDataGeneratorForMaintainer(
            MaintainerService maintainerService, UserMapperImpl userMapperImpl) {
        this.maintainerService = maintainerService;
        this.userMapperImpl = userMapperImpl;
    }

    @PostConstruct
    public void init() {
        Maintainer sampleMaintainer = new Maintainer();
        sampleMaintainer.setFirstName("John");
        sampleMaintainer.setLastName("Doe");
        sampleMaintainer.setGender(Gender.MALE);
        sampleMaintainer.setBirthDate(LocalDate.now());
        sampleMaintainer.setEmail("john.doe@example.com");

        maintainerService.save(userMapperImpl.mapToDto(sampleMaintainer));
    }
}
