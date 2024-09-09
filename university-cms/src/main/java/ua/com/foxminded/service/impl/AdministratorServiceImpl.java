package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.AdministratorService;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserEmailService;
import ua.com.foxminded.service.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {
  private static final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);
  private final String ADMIN_ROLE = "ADMIN";
  private final AdministratorRepository administratorRepository;
  private final ImageService imageService;
  private final UserEmailService userEmailService;
  private final UserMapper userMapper;

  public AdministratorServiceImpl(
          AdministratorRepository administratorRepository,
          ImageService imageService,
          UserEmailService userEmailService,
          UserMapper userMapper) {
    this.administratorRepository = administratorRepository;
    this.imageService = imageService;
    this.userEmailService = userEmailService;
    this.userMapper = userMapper;
  }

  @Override
  public AdministratorDTO save(AdministratorDTO administratorDTO) {
    if (!isEmailFree(administratorDTO.getEmail())) {
      throw new RuntimeException();
    }
    if (administratorDTO.getImage() == null || administratorDTO.getImage().isEmpty()) {
      Administrator administrator = userMapper.mapFromDto(administratorDTO);
      administrator.setImageName(
              imageService.getDefaultIUserImage(administrator.getGender(), ADMIN_ROLE));
      logger.info(
              "Saved administrator: {} {}",
              administratorDTO.getFirstName(),
              administratorDTO.getLastName());
      return userMapper.mapToDto(administratorRepository.save(administrator));
    } else {
      Administrator administrator = userMapper.mapFromDto(administratorDTO);
      administrator = administratorRepository.save(administrator);
      String imageName = imageService.saveUserImage(ADMIN_ROLE, administratorDTO.getImage());
      administrator.setImageName(imageName);
      logger.info(
              "Saved administrator: {} {}",
              administratorDTO.getFirstName(),
              administratorDTO.getLastName());
      return userMapper.mapToDto(administratorRepository.save(administrator));
    }
  }

  @Override
  public AdministratorDTO update(UUID id, AdministratorDTO administratorDTO) {
    if (!isEmailFree(administratorDTO.getEmail())) {
      throw new RuntimeException();
    }
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
      String imageName = imageService.saveUserImage(ADMIN_ROLE, administratorDTO.getImage());
      existingAdministrator.setImageName(imageName);
    }
    logger.info("Administrator updated by id: {}", id);
    return userMapper.mapToDto(administratorRepository.save(existingAdministrator));
  }

  @Override
  public void delete(UUID id) {
    AdministratorDTO administratorDto = findById(id);
    Administrator administrator = userMapper.mapFromDto(administratorDto);
    imageService.deleteUserImage(administrator.getImageName());
    administratorRepository.deleteById(id);
    logger.info("Admin was deleted by id: {}", id);
  }

  @Override
  public AdministratorDTO findByIdDTO(UUID id) {
    Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
    if (optionalAdministrator.isPresent()) {
      logger.info("The administrator was found by id: {}", id);
      return userMapper.mapToDto(optionalAdministrator.get());
    } else {
      throw new RuntimeException("There is no such administrator");
    }
  }

  @Override
  public AdministratorDTO findByEmail(String email) {
    Optional<Administrator> optionalAdministrator = administratorRepository.findByEmail(email);
    if (optionalAdministrator.isPresent()) {
      return userMapper.mapToDto(optionalAdministrator.get());
    }
    throw new RuntimeException("There is no such administrator");
  }

  @Override
  public AdministratorDTO findById(UUID id) {
    Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
    if (optionalAdministrator.isPresent()) {
      logger.info("The administrator was found by id: {}", id);
      return userMapper.mapToDto(optionalAdministrator.get());
    } else {
      throw new RuntimeException("There is no such administrator");
    }
  }

  @Override
  public Page<AdministratorDTO> findAll(Pageable pageable) {
    int pageNumber = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();
    int from = pageNumber * pageSize;
    int to = from + pageSize;
    logger.info("Find administrators from {} to {}", from, to);

    Page<Administrator> administratorsPage = administratorRepository.findAll(pageable);
    List<AdministratorDTO> administratorDtoList = administratorsPage.stream()
            .map(userMapper::mapToDto)
            .collect(Collectors.toList());

    return new PageImpl<>(administratorDtoList, pageable, administratorsPage.getTotalElements());
  }


  @Override
  public Long count() {
    return administratorRepository.count();
  }

  private boolean isEmailFree(String email) {
    return userEmailService.isUserExistByEmail(email);
  }
}
