package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.GroupService;

import java.time.LocalDateTime;
import java.util.List;

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
    public void save(Group group) {
        if (isValidNameForGroup(group.getName())) {
            groupRepository.save(group);
            logger.info("Saved group {}", group.getName());
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
        logger.info("Find all groups");
        return (List<Group>) groupRepository.findAll();
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find groups from {} to {}", from, to);
        return groupRepository.findAll(pageable);
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
    public List<Lecture> showLecturesBetweenDates(Long groupId, LocalDateTime firstDate, LocalDateTime secondDate) {
        if (!groupRepository.existsById(groupId)) {
            throw new RuntimeException("The group was not found");
        }
        return groupRepository.findLecturesByDateBetween(groupId, firstDate, secondDate);
    }
}

