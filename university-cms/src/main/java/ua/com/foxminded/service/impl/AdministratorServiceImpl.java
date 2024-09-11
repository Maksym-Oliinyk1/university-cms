package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);
    private final String ADMIN_ROLE = "ADMINISTRATOR";
    private final AdministratorRepository administratorRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    public AdministratorServiceImpl(
            AdministratorRepository administratorRepository,
            ImageService imageService,
            UserMapper userMapper) {
        this.administratorRepository = administratorRepository;
        this.imageService = imageService;
        this.userMapper = userMapper;
    }

    @Override
    public void save(AdministratorDTO administratorDTO) {
        if (administratorDTO.getImage() == null || administratorDTO.getImage().isEmpty()) {
            Administrator administrator = userMapper.mapFromDto(administratorDTO);
            administrator.setImageName(
                    imageService.getDefaultIUserImage(administrator.getGender(), ADMIN_ROLE));
            administratorRepository.save(administrator);
        } else {
            Administrator administrator = userMapper.mapFromDto(administratorDTO);
            administrator = administratorRepository.save(administrator);
            String imageName =
                    imageService.saveUserImage(
                            ADMIN_ROLE, administrator.getId(), administratorDTO.getImage());
            administrator.setImageName(imageName);
            administratorRepository.save(administrator);
        }
        logger.info(
                "Saved administrator: {} {}",
                administratorDTO.getFirstName(),
                administratorDTO.getLastName());
    }

    @Override
    public void update(Long id, AdministratorDTO administratorDTO) {
        Administrator existingAdministrator =
                administratorRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));

        existingAdministrator.setFirstName(administratorDTO.getFirstName());
        existingAdministrator.setLastName(administratorDTO.getLastName());
        existingAdministrator.setGender(administratorDTO.getGender());
        existingAdministrator.setBirthDate(administratorDTO.getBirthDate());
        existingAdministrator.setEmail(administratorDTO.getEmail());
        if (administratorDTO.getImage() == null || administratorDTO.getImage().isEmpty()) {
            imageService.deleteUserImage(existingAdministrator.getImageName());
            existingAdministrator.setImageName(
                    imageService.getDefaultIUserImage(administratorDTO.getGender(), ADMIN_ROLE));
        } else {
            imageService.deleteUserImage(existingAdministrator.getImageName());
            String imageName = imageService.saveUserImage(ADMIN_ROLE, id, administratorDTO.getImage());
            existingAdministrator.setImageName(imageName);
        }
        administratorRepository.save(existingAdministrator);
        logger.info("Administrator updated by id: {}", id);
    }

    @Override
    public void delete(Long id) {
        Administrator administrator = findById(id);
        imageService.deleteUserImage(administrator.getImageName());
        administratorRepository.deleteById(id);
        logger.info("Admin was deleted by id: {}", id);
    }

    @Override
    public AdministratorDTO findByIdDTO(Long id) {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
        if (optionalAdministrator.isPresent()) {
            logger.info("The administrator was found by id: {}", id);
            return userMapper.mapToDto(optionalAdministrator.get());
        } else {
            throw new RuntimeException("There is no such administrator");
        }
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

    @Override
    public Long count() {
        return administratorRepository.count();
    }
}
