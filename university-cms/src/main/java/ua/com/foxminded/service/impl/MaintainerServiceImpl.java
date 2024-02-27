package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.MaintainerService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

@Service
public class MaintainerServiceImpl implements MaintainerService {
    private static final Logger logger = LoggerFactory.getLogger(MaintainerServiceImpl.class);
    private final String MAINTAINER_ROLE = "MAINTAINER";
    private final MaintainerRepository maintainerRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    public MaintainerServiceImpl(MaintainerRepository maintainerRepository, ImageService imageService, UserMapper userMapper) {
        this.maintainerRepository = maintainerRepository;
        this.imageService = imageService;
        this.userMapper = userMapper;
    }

    @Override
    public void save(MaintainerDTO maintainerDTO) {
        if (maintainerDTO.getImage() == null || maintainerDTO.getImage().isEmpty()) {
            Maintainer maintainer = userMapper.mapFromDto(maintainerDTO);
            maintainer.setImageName(imageService.getDefaultIUserImage(maintainer.getGender(), MAINTAINER_ROLE));
            maintainerRepository.save(maintainer);
        } else {
            Maintainer maintainer = userMapper.mapFromDto(maintainerDTO);
            maintainer = maintainerRepository.save(maintainer);
            String imageName = imageService.saveUserImage(MAINTAINER_ROLE, maintainer.getId(), maintainerDTO.getImage());
            maintainer.setImageName(imageName);
            maintainerRepository.save(maintainer);
        }
        logger.info("Saved maintainer: {} {}", maintainerDTO.getFirstName(), maintainerDTO.getLastName());
    }

    @Override
    public void update(Long id, MaintainerDTO maintainerDTO) {
        Maintainer existingMaintainer = maintainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintainer not found with id: " + id));
        existingMaintainer.setFirstName(maintainerDTO.getFirstName());
        existingMaintainer.setLastName(maintainerDTO.getLastName());
        existingMaintainer.setGender(maintainerDTO.getGender());
        existingMaintainer.setBirthDate(maintainerDTO.getBirthDate());
        existingMaintainer.setEmail(maintainerDTO.getEmail());
        if (maintainerDTO.getImage() == null || maintainerDTO.getImage().isEmpty()) {
            imageService.deleteUserImage(existingMaintainer.getImageName());
            existingMaintainer.setImageName(imageService.getDefaultIUserImage(maintainerDTO.getGender(), MAINTAINER_ROLE));
        } else {
            imageService.deleteUserImage(existingMaintainer.getImageName());
            String imageName = imageService.saveUserImage(MAINTAINER_ROLE, id, maintainerDTO.getImage());
            existingMaintainer.setImageName(imageName);
        }
        maintainerRepository.save(existingMaintainer);
        logger.info("Maintainer updated by id: {}", id);
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
}
