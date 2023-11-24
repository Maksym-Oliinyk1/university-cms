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

import static ua.com.foxminded.utill.NameValidator.isValidDescriptionForLecture;
import static ua.com.foxminded.utill.NameValidator.isValidName;

@Service
public class LectureServiceImpl implements LectureService {

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
            if (isValidDescriptionForLecture(lecture.getDescription())) {
                lectureRepository.save(lecture);
                logger.info("Created lecture: {}", lecture.getName());
            } else {
                logger.error("Invalid description for lecture: {}", lecture.getDescription());
                throw new RuntimeException("Invalid description for lecture");
            }
        } else {
            throw new RuntimeException("Invalid name for lecture");
        }
    }

    @Override
    public void update(Lecture lecture) {
        if (isValidName(lecture.getName())) {
            if (isValidDescriptionForLecture(lecture.getDescription())) {
                lectureRepository.save(lecture);
            } else {
                throw new RuntimeException("Invalid description for lecture");
            }
        } else {
            throw new RuntimeException("Invalid name for lecture");
        }
    }

    @Override
    public void delete(Long id) {
        if (lectureRepository.existsById(id)) {
            lectureRepository.deleteById(id);
            logger.info("The lecture was deleted by id {}", id);
        } else {
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
}
