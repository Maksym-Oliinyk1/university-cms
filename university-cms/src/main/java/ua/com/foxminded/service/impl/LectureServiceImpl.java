package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.service.LectureService;

import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {

    private static final String FILTER_ON_ACCEPTABLE_NAME_FORM = "^[A-Z]{2}-\\d{2}$";

    private static final String FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS = ".{100,}";

    private static final Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);

    private final LectureRepository lectureRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, GroupRepository groupRepository) {
        this.lectureRepository = lectureRepository;
        this.groupRepository = groupRepository;
    }


    @Override
    public void create(Lecture lecture) {
        if (isValidName(lecture.getName())) {
            if (isValidDescription(lecture.getDescription())) {
                lectureRepository.save(lecture);
                logger.info("Created lecture: {}", lecture.getName());
            } else {
                logger.error("Invalid description for lecture: {}", lecture.getDescription());
                throw new RuntimeException("Invalid description for lecture");
            }
        } else {
            logger.error("Invalid name for lecture: {}", lecture.getName());
            throw new RuntimeException("Invalid name for lecture");
        }
    }

    @Override
    public void update(Lecture lecture) {
        if (isValidName(lecture.getName())) {
            if (isValidDescription(lecture.getDescription())) {
                lectureRepository.save(lecture);
            } else {
                logger.error("Invalid description for lecture: {}", lecture.getDescription());
                throw new RuntimeException("Invalid description for lecture");
            }
        } else {
            logger.error("Invalid name for lecture: {}", lecture.getName());
            throw new RuntimeException("Invalid name for lecture");
        }
    }

    @Override
    public void delete(Long id) {
        if (lectureRepository.existsById(id)) {
            lectureRepository.deleteById(id);
            logger.info("The lecture was deleted by id {}", id);
        } else {
            logger.error("The lecture was not found by id {}", id);
            throw new RuntimeException("There is no such lecture");
        }
    }

    @Override
    public Lecture findById(Long id) {
        Optional<Lecture> optionalLecture = lectureRepository.findById(id);
        if (optionalLecture.isPresent()) {
            logger.info("The lecture was found by id {}", id);
            return optionalLecture.get();
        } else {
            logger.error("The lecture was not found by id {}", id);
            throw new RuntimeException("There is no such lecture");
        }
    }

    @Override
    public List<Lecture> findAll() {
        return (List<Lecture>) lectureRepository.findAll();
    }

    @Override
    @Transactional
    public void attachGroupToLecture(Long groupId, Long lectureId) {
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(EntityNotFoundException::new);

        lecture.getGroups().add(group);
        group.getLectures().add(lecture);
        lectureRepository.save(lecture);
        groupRepository.save(group);
        logger.info("Group added to the lecture");
    }

    @Override
    @Transactional
    public void detachGroupFromLecture(Long groupId, Long lectureId) {
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(EntityNotFoundException::new);

        lecture.getGroups().remove(group);
        group.getLectures().remove(lecture);
        lectureRepository.save(lecture);
        groupRepository.save(group);
        logger.info("Group removed from the lecture");
    }

    private boolean isValidName(String name) {
        return name.matches(FILTER_ON_ACCEPTABLE_NAME_FORM);
    }

    private boolean isValidDescription(String description) {
        return description.matches(FILTER_ON_ACCEPTABLE_AMOUNT_OF_DESCRIPTION_SYMBOLS);
    }
}
