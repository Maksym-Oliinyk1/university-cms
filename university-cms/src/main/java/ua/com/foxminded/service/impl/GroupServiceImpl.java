package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.GroupService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ua.com.foxminded.utill.NameValidator.isValidNameForGroup;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    private static final Long INVALID_NUMBER_OF_LECTURES = 0L;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void create(Group group) {
        if (isValidNameForGroup(group.getName())) {
            groupRepository.save(group);
            logger.info("Created group {}", group.getName());
        } else {
            throw new RuntimeException("Invalid name for group");
        }
    }

    @Override
    public void update(Group group) {
        if (isValidNameForGroup(group.getName())) {
            groupRepository.save(group);
            logger.info("Updated group {}", group.getName());
        } else {
            throw new RuntimeException("Invalid name for group");
        }
    }

    @Override
    public void delete(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            logger.info("The group was deleted by id {}", id);
        } else {
            throw new RuntimeException("There is no such group");
        }
    }

    @Override
    public Group findById(Long id) {
        if (groupRepository.existsById(id)) {
            logger.info("The group was found by id {}", id);
            return groupRepository.findById(id).get();
        } else {
            throw new RuntimeException("There is no such group");
        }
    }

    @Override
    public List<Group> findAll() {
        return (List<Group>) groupRepository.findAll();
    }

    @Override
    @Transactional
    public void attachStudentToGroup(Long studentId, Long groupId) {
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(EntityNotFoundException::new);
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(EntityNotFoundException::new);

        group.getStudents().add(student);
        student.setGroup(group);
        groupRepository.save(group);
        studentRepository.save(student);
        logger.info("Student was added to the course");
    }

    @Override
    @Transactional
    public void detachStudentFromGroup(Long studentId, Long groupId) {
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(EntityNotFoundException::new);
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(EntityNotFoundException::new);

        group.getStudents().remove(student);
        student.setGroup(null);
        groupRepository.save(group);
        studentRepository.save(student);
        logger.info("Student was removed from the course");
    }

    @Override
    public List<Lecture> showLecturesPerWeek(Long groupId) {
        if (groupRepository.existsById(groupId)) {
            if ((groupRepository.countLecturesByGroup(groupId).equals(INVALID_NUMBER_OF_LECTURES))) {
                throw new RuntimeException("There is no any lecture");
            } else {
                LocalDateTime localDateTimeNextWeek = LocalDateTime.now().plusWeeks(1);
                return groupRepository.findLecturesByDateBetween(groupId, LocalDateTime.now(), localDateTimeNextWeek);
            }
        } else {
            throw new RuntimeException("The group was not found");
        }
    }

    @Override
    public List<Lecture> showLecturesPerMonth(Long groupId) {
        if (groupRepository.existsById(groupId)) {
            if ((groupRepository.countLecturesByGroup(groupId).equals(INVALID_NUMBER_OF_LECTURES))) {
                throw new RuntimeException("There is no any lecture");
            } else {
                LocalDateTime localDateTimeNextMonth = LocalDateTime.now().plusMonths(1);
                return groupRepository.findLecturesByDateBetween(groupId, LocalDateTime.now(), localDateTimeNextMonth);
            }
        } else {
            throw new RuntimeException("The group was not found");
        }
    }

    @Override
    public List<Lecture> showLecturesBetweenDates(Long groupId, LocalDateTime firstDate, LocalDateTime secondDate) {
        if (groupRepository.existsById(groupId)) {
            if ((groupRepository.countLecturesByGroup(groupId).equals(INVALID_NUMBER_OF_LECTURES))) {
                throw new RuntimeException("There is no any lecture");
            } else {
                return groupRepository.findLecturesByDateBetween(groupId, firstDate, secondDate);
            }
        } else {
            throw new RuntimeException("The group was not found");
        }
    }
}
