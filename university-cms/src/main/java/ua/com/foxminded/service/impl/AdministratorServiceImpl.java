package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.AdministratorService;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);

    private final AdministratorRepository administratorRepository;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public void create(Administrator administrator) {
        if (isValidName(administrator.getFirstName()) && isValidName(administrator.getLastName())) {
            administratorRepository.save(administrator);
            logger.info("Created administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
        } else {
            logger.error("Invalid name for administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
            throw new RuntimeException("Invalid name for administrator");
        }
    }

    @Override
    public void update(Administrator administrator) {
        if (isValidName(administrator.getFirstName()) && isValidName(administrator.getLastName())) {
            administratorRepository.save(administrator);
            logger.info("Updated administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
        } else {
            logger.error("Invalid name for administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
            throw new RuntimeException("Invalid name for administrator");
        }
    }

    @Override
    public void delete(Long id) {
        if (administratorRepository.existsById(id)) {
            administratorRepository.deleteById(id);
            logger.info("Administrator deleted by id: {}", id);
        } else {
            logger.error("Administrator was not found by id: {}", id);
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
            logger.error("The administrator was not found by id: {}", id);
            throw new RuntimeException("There is no such administrator");
        }
    }

    @Override
    public List<Administrator> findAll() {
        return (List<Administrator>) administratorRepository.findAll();
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z]+");
    }
}
