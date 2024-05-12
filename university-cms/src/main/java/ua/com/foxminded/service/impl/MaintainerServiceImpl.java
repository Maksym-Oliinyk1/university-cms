package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.enums.Authorities;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.UserEmailService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

@Service
public class MaintainerServiceImpl implements MaintainerService {
    private static final Logger logger = LoggerFactory.getLogger(MaintainerServiceImpl.class);
    private final String MAINTAINER_ROLE = "MAINTAINER";
    private final MaintainerRepository maintainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserEmailService userEmailService;
    private final UserMapper userMapper;

    public MaintainerServiceImpl(MaintainerRepository maintainerRepository,
                                 PasswordEncoder passwordEncoder,
                                 ImageService imageService,
                                 UserEmailService userEmailService,
                                 UserMapper userMapper) {
        this.maintainerRepository = maintainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.userEmailService = userEmailService;
        this.userMapper = userMapper;
    }

    @Override
    public Maintainer save(MaintainerDTO maintainerDTO) {
        if (!isEmailFree(maintainerDTO.getEmail())) {
            throw new RuntimeException();
        }
        maintainerDTO.setPassword(passwordEncoder.encode(maintainerDTO.getPassword()));
        maintainerDTO.setAuthority(Authorities.MAINTAINER);
        if (maintainerDTO.getImage() == null || maintainerDTO.getImage().isEmpty()) {
            Maintainer maintainer = userMapper.mapFromDto(maintainerDTO);
            maintainer.setImageName(imageService.getDefaultIUserImage(maintainer.getGender(), MAINTAINER_ROLE));
            logger.info("Saved maintainer: {} {}", maintainerDTO.getFirstName(), maintainerDTO.getLastName());
            return maintainerRepository.save(maintainer);
        } else {
            Maintainer maintainer = userMapper.mapFromDto(maintainerDTO);
            maintainer = maintainerRepository.save(maintainer);
            String imageName = imageService.saveUserImage(MAINTAINER_ROLE, maintainer.getId(), maintainerDTO.getImage());
                maintainer.setImageName(imageName);
                logger.info("Saved maintainer: {} {}", maintainerDTO.getFirstName(), maintainerDTO.getLastName());
                return maintainerRepository.save(maintainer);
            }
    }

    @Override
    public Maintainer update(Long id, MaintainerDTO maintainerDTO) {
        if (!isEmailFree(maintainerDTO.getEmail())) {
            throw new RuntimeException();
        }
        maintainerDTO.setPassword(passwordEncoder.encode(maintainerDTO.getPassword()));
        maintainerDTO.setAuthority(Authorities.MAINTAINER);
        Maintainer existingMaintainer = maintainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintainer not found with id: " + id));
        existingMaintainer.setFirstName(maintainerDTO.getFirstName());
        existingMaintainer.setLastName(maintainerDTO.getLastName());
        existingMaintainer.setGender(maintainerDTO.getGender());
        existingMaintainer.setBirthDate(maintainerDTO.getBirthDate());
        existingMaintainer.setEmail(maintainerDTO.getEmail());
        existingMaintainer.setPassword(passwordEncoder.encode(maintainerDTO.getPassword()));
        if (maintainerDTO.getImage() == null || maintainerDTO.getImage().isEmpty()) {
            imageService.deleteUserImage(existingMaintainer.getImageName());
            existingMaintainer.setImageName(imageService.getDefaultIUserImage(maintainerDTO.getGender(), MAINTAINER_ROLE));
        } else {
            imageService.deleteUserImage(existingMaintainer.getImageName());
            String imageName = imageService.saveUserImage(MAINTAINER_ROLE, id, maintainerDTO.getImage());
            existingMaintainer.setImageName(imageName);
        }
        logger.info("Maintainer updated by id: {}", id);
        return maintainerRepository.save(existingMaintainer);
    }

    @Override
    public MaintainerDTO findByIdDTO(Long id) {
        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(id);
        if (optionalMaintainer.isPresent()) {
            logger.info("The maintainer was found by id: {}", id);
            return userMapper.mapToDto(optionalMaintainer.get());
        } else {
            throw new RuntimeException("There is no such maintainer");
        }
    }

    @Override
    public Optional<Maintainer> findByEmail(String email) {
        return maintainerRepository.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        Maintainer maintainer = findById(id);
        imageService.deleteUserImage(maintainer.getImageName());
        maintainerRepository.deleteById(id);
        logger.info("Maintainer was deleted by id: {}", id);
    }

    @Override
    public Maintainer findById(Long id) {
        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(id);
        if (optionalMaintainer.isPresent()) {
            logger.info("The maintainer was found by id: {}", id);
            return optionalMaintainer.get();
        } else {
            throw new RuntimeException("There is no such maintainer");
        }
    }

    @Override
    public Page<Maintainer> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find maintainers from {} to {}", from, to);
        return maintainerRepository.findAll(pageable);
    }

    @Override
    public Long count() {
        return maintainerRepository.count();
    }

    private boolean isEmailFree(String email) {
        return userEmailService.isUserExistByEmail(email);
    }
}
