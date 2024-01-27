package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.ImageService;

import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidEmail;
import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);

    private final AdministratorRepository administratorRepository;
    private final ImageService imageService;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository, ImageService imageService) {
        this.administratorRepository = administratorRepository;
        this.imageService = imageService;
    }

    @Override
    public void save(Administrator administrator, MultipartFile imageFile) {
        if (isValidNameForUser(administrator.getFirstName())
                && isValidNameForUser(administrator.getLastName())
                && isValidEmail(administrator.getEmail())) {
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(administrator);
                administratorRepository.save(administrator);
            } else {
                Administrator admin = administratorRepository.save(administrator);
                String imageName = imageService.saveUserImage(admin.getId(), imageFile);
                admin.setImageName(imageName);
                administratorRepository.save(admin);
            }
            logger.info("Saved administrator: {} {}", administrator.getFirstName(), administrator.getLastName());
        } else {
            throw new RuntimeException("Invalid data for administrator");
        }
    }

    public void update(Long id, Administrator updatedAdministrator, MultipartFile imageFile) {
        Administrator existingAdministrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
        if (!isValidNameForUser(updatedAdministrator.getFirstName()) ||
                !isValidNameForUser(updatedAdministrator.getLastName()) ||
                !isValidEmail(updatedAdministrator.getEmail())) {
            throw new RuntimeException("Invalid data for administrator");
        }
        existingAdministrator.setFirstName(updatedAdministrator.getFirstName());
        existingAdministrator.setLastName(updatedAdministrator.getLastName());
        existingAdministrator.setGender(updatedAdministrator.getGender());
        existingAdministrator.setAge(updatedAdministrator.getAge());
        existingAdministrator.setEmail(updatedAdministrator.getEmail());
        if (imageFile == null || imageFile.isEmpty()) {
            imageService.setDefaultImageForUser(existingAdministrator);
        } else {
            String imageName = imageService.saveUserImage(id, imageFile);
            existingAdministrator.setImageName(imageName);
        }
        administratorRepository.save(existingAdministrator);
        logger.info("Administrator updated by id: {}", id);
    }

    @Override
    public void delete(Long id) {
        imageService.deleteUserImage(id);
        administratorRepository.deleteById(id);
        logger.info("Admin was deleted by id: {}", id);
    }

    @Override
    public Administrator findById(Long id) {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
        if (optionalAdministrator.isPresent()) {
            logger.info("The administrator was found by id: {}", id);
            return optionalAdministrator.get();
        } else {
            throw new RuntimeException("There is no such administrator");
        }
    }

    @Override
    public Page<Administrator> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find administrators from {} to {}", from, to);
        return administratorRepository.findAll(pageable);
    }
}
