package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.StudentService;

import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void create(Student student) {
        if (isValidNameForUser(student.getFirstName()) && isValidNameForUser(student.getLastName())) {
            studentRepository.save(student);
            logger.info("Created student: {} {}", student.getFirstName(), student.getLastName());
        } else {
            throw new RuntimeException("Invalid name for student");
        }
    }

    @Override
    public void update(Student student) {
        if (isValidNameForUser(student.getFirstName()) && isValidNameForUser(student.getLastName())) {
            studentRepository.save(student);
            logger.info("Updated student: {} {}", student.getFirstName(), student.getLastName());
        } else {
            throw new RuntimeException("Invalid name for student");
        }
    }

    @Override
    public void delete(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Student was deleted by id: {}", id);
        } else {
            throw new RuntimeException("There is no such student");
        }
    }

    @Override
    public Student findById(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            logger.info("Student was found by id: {}", id);
            return optionalStudent.get();
        } else {
            throw new RuntimeException("There is no such student");
        }
    }

    @Override
    public List<Student> findAll() {
        return (List<Student>) studentRepository.findAll();
    }
}
