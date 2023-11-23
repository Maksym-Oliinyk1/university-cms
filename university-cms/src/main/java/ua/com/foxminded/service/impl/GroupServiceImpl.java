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
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.GroupService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private static final String FILTER_ON_ACCEPTABLE_NAME_FORM = "^[A-Z]{2}-\\d{2}$";

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void create(Group group) {
        if (isValidName(group.getName())) {
            groupRepository.save(group);
            logger.info("Created group {}", group.getName());
        } else {
            logger.error("Invalid name for group {}", group.getName());
            throw new RuntimeException("Invalid name for group");
        }
    }

    @Override
    public void update(Group group) {
        if (isValidName(group.getName())) {
            groupRepository.save(group);
            logger.info("Updated group {}", group.getName());
        } else {
            logger.error("Invalid name for group {}", group.getName());
            throw new RuntimeException("Invalid name for group");
        }
    }

    @Override
    public void delete(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            logger.info("The group was deleted by id {}", id);
        } else {
            logger.error("The group was not found by id {}", id);
            throw new RuntimeException("There is no such group");
        }
    }

    @Override
    public Group findById(Long id) {
        if (groupRepository.existsById(id)) {
            logger.info("The group was found by id {}", id);
            return groupRepository.findById(id).get();
        } else {
            logger.info("The group was not found by id {}", id);
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
            Group group = groupRepository.findById(groupId).get();
            if (group.getLectures().isEmpty()) {
                logger.info("There is no any lecture for this group");
                throw new RuntimeException("There is no any lecture");
            } else {
                List<Lecture> lectures = group.getLectures();
                LocalDateTime localDateTimeNextWeek = LocalDateTime.now().plusWeeks(1);
                return filterLecturesForGivenTime(lectures, localDateTimeNextWeek);
            }
        } else {
            logger.error("The group was not found by id: {}", groupId);
            throw new RuntimeException("The group was not found");
        }
    }

    @Override
    public List<Lecture> showLecturesPerMonth(Long groupId) {
        if (groupRepository.existsById(groupId)) {
            Group group = groupRepository.findById(groupId).get();
            if (group.getLectures().isEmpty()) {
                logger.info("There is no any lecture for this group");
                throw new RuntimeException("There is no any lecture");
            } else {
                List<Lecture> lectures = group.getLectures();
                LocalDateTime localDateTimeNextWeek = LocalDateTime.now().plusMonths(1);
                return filterLecturesForGivenTime(lectures, localDateTimeNextWeek);
            }
        } else {
            logger.error("The group was not found by id: {}", groupId);
            throw new RuntimeException("The group was not found");
        }
    }

    private List<Lecture> filterLecturesForGivenTime(List<Lecture> lectures, LocalDateTime givenLocalDateTime) {
        return lectures.stream()
                .filter(lecture ->
                        lecture.getDate()
                                .isAfter(LocalDateTime.now()) &&
                                lecture.getDate()
                                        .isBefore(givenLocalDateTime))
                .sorted(Comparator.comparing(Lecture::getDate))
                .toList();
    }

    private boolean isValidName(String name) {
        return name.matches(FILTER_ON_ACCEPTABLE_NAME_FORM);
    }
}
