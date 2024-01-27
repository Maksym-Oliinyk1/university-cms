package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.MaintainerService;

import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidEmail;
import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class MaintainerServiceImpl implements MaintainerService {
    private static final Logger logger = LoggerFactory.getLogger(MaintainerServiceImpl.class);

    private final MaintainerRepository maintainerRepository;
    private final ImageService imageService;

    public MaintainerServiceImpl(MaintainerRepository maintainerRepository, ImageService imageService) {
        this.maintainerRepository = maintainerRepository;
        this.imageService = imageService;
    }


    @Override
    public void save(Maintainer maintainer, MultipartFile imageFile) {
        if (isValidNameForUser(maintainer.getFirstName())
                && isValidNameForUser(maintainer.getLastName())
                && isValidEmail(maintainer.getEmail())) {
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(maintainer);
            } else {
                String imageName = imageService.saveUserImage(maintainer.getId(), imageFile);
                maintainer.setImageName(imageName);
            }
            maintainerRepository.save(maintainer);
            logger.info("Saved maintainer: {} {}", maintainer.getFirstName(), maintainer.getLastName());
        } else {
            throw new RuntimeException("Invalid name for maintainer");
        }
    }

    @Override
    public void update(Long id, Maintainer updatedMaintainer, MultipartFile imageFile) {
        Optional<Maintainer> optionalMaintainer = maintainerRepository.findById(id);
        if (optionalMaintainer.isPresent()) {
            Maintainer existingMaintainer = optionalMaintainer.get();
            existingMaintainer.setFirstName(updatedMaintainer.getFirstName());
            existingMaintainer.setLastName(updatedMaintainer.getLastName());
            existingMaintainer.setAge(updatedMaintainer.getAge());
            existingMaintainer.setEmail(updatedMaintainer.getEmail());
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(existingMaintainer);
            } else {
                String imageName = imageService.saveUserImage(id, imageFile);
                existingMaintainer.setImageName(imageName);
            }
            maintainerRepository.save(existingMaintainer);
            logger.info("Maintainer updated by id: {}", id);
        } else {
            throw new RuntimeException("There is no such maintainer");
        }
    }

    @Override
    public void delete(Long id) {
        imageService.deleteUserImage(id);
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
}
