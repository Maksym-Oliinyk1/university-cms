package ua.com.foxminded.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.repository.StudentRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.StudentService;

import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidEmail;
import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_ROLE = "STUDENT";
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final ImageService imageService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ImageService imageService) {
        this.studentRepository = studentRepository;
        this.imageService = imageService;
    }

    @Override
    public void save(Student student, MultipartFile imageFile) {
        if (isValidNameForUser(student.getFirstName())
                && isValidNameForUser(student.getLastName())
                && isValidEmail(student.getEmail())) {
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(student);
                studentRepository.save(student);
            } else {
                student = studentRepository.save(student);
                String imageName = imageService.saveUserImage(STUDENT_ROLE, student.getId(), imageFile);
                student.setImageName(imageName);
                studentRepository.save(student);
            }
            logger.info("Saved student: {} {}", student.getFirstName(), student.getLastName());
        } else {
            throw new RuntimeException("Invalid name for student");
        }
    }

    @Override
    public void update(Long id, Student updatedStudent, MultipartFile imageFile) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        if (!isValidNameForUser(updatedStudent.getFirstName()) ||
                !isValidNameForUser(updatedStudent.getLastName()) ||
                !isValidEmail(updatedStudent.getEmail())) {
            throw new RuntimeException("Invalid data for student");
        }
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setAge(updatedStudent.getAge());
        existingStudent.setEmail(updatedStudent.getEmail());

        if (imageFile == null || imageFile.isEmpty()) {
            imageService.setDefaultImageForUser(existingStudent);
        } else {
            String imageName = imageService.saveUserImage(STUDENT_ROLE, id, imageFile);
            existingStudent.setImageName(imageName);
        }
        studentRepository.save(existingStudent);
        logger.info("Student updated by id: {}", id);
    }


    @Override
    public void delete(Long id) {
        imageService.deleteUserImage(STUDENT_ROLE, id);
        studentRepository.deleteById(id);
        logger.info("Student was deleted by id: {}", id);
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
    public Page<Student> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find students from {} to {}", from, to);
        return studentRepository.findAll(pageable);
    }
}
