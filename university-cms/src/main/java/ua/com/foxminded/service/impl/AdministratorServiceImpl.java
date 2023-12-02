package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.AdministratorService;

import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);

    private final AdministratorRepository administratorRepository;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public void save(Administrator administrator) {
        if (isValidNameForUser(administrator.getFirstName()) && isValidNameForUser(administrator.getLastName())) {
            administratorRepository.save(administrator);
            logger.info("Saved administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
        } else {
            throw new RuntimeException("Invalid name for administrator");
        }
    }

    @Override
    public void delete(Long id) {
        if (administratorRepository.existsById(id)) {
            administratorRepository.deleteById(id);
            logger.info("Administrator deleted by id: {}", id);
        } else {
            throw new RuntimeException("There is no such administrator");
        }
    }

    @Override
    public Administrator findById(Long id) {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
        if (optionalAdministrator.isPresent()) {
            logger.info("The administrator was found by id: id");
            return optionalAdministrator.get();
        } else {
            throw new RuntimeException("There is no such administrator");
        }
    }

    @Override
    public List<Administrator> findAll() {
        logger.info("Find all administrators");
        return (List<Administrator>) administratorRepository.findAll();
    }
}
