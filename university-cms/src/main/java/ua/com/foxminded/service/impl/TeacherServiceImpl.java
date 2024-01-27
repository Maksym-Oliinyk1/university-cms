package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.TeacherService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.utill.NameValidator.isValidEmail;
import static ua.com.foxminded.utill.NameValidator.isValidNameForUser;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final ImageService imageService;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, LectureRepository lectureRepository, ImageService imageService) {
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
        this.imageService = imageService;
    }

    @Override
    public void save(Teacher teacher, MultipartFile imageFile) {
        if (isValidNameForUser(teacher.getFirstName())
                && isValidNameForUser(teacher.getLastName())
                && isValidEmail(teacher.getEmail())) {
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(teacher);
            } else {
                String imageName = imageService.saveUserImage(teacher.getId(), imageFile);
                teacher.setImageName(imageName);
            }
            teacherRepository.save(teacher);
            logger.info("Saved teacher: {} {}", teacher.getFirstName(), teacher.getLastName());
        } else {
            throw new RuntimeException("Invalid name for teacher");
        }
    }

    @Override
    @Transactional
    public void update(Long id, Teacher updatedTeacher, MultipartFile imageFile) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher existingTeacher = optionalTeacher.get();
            existingTeacher.setFirstName(updatedTeacher.getFirstName());
            existingTeacher.setLastName(updatedTeacher.getLastName());
            existingTeacher.setAcademicDegree(updatedTeacher.getAcademicDegree());
            existingTeacher.setAge(updatedTeacher.getAge());
            existingTeacher.setEmail(updatedTeacher.getEmail());
            if (imageFile == null || imageFile.isEmpty()) {
                imageService.setDefaultImageForUser(existingTeacher);
            } else {
                String imageName = imageService.saveUserImage(id, imageFile);
                existingTeacher.setImageName(imageName);
            }
            teacherRepository.save(existingTeacher);
            logger.info("Teacher updated by id: {}", id);
        } else {
            throw new RuntimeException("There is no such teacher");
        }
    }

    @Override
    public void delete(Long id) {
        imageService.deleteUserImage(id);
        teacherRepository.deleteById(id);
        logger.info("Teacher was deleted by id: {}", id);
    }

    @Override
    public Teacher findById(Long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            logger.info("The teacher was found by id: {}", id);
            return optionalTeacher.get();
        } else {
            throw new RuntimeException("There is no such teacher");
        }
    }

    @Override
    public Page<Teacher> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find teachers from {} to {}", from, to);
        return teacherRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void attachLectureToTeacher(Long lectureId, Long teacherId) {
        Teacher teacher = teacherRepository
                .findById(teacherId)
                .orElseThrow(EntityNotFoundException::new);
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);

        teacher.getLectures().add(lecture);
        lecture.setTeacher(teacher);
        teacherRepository.save(teacher);
        lectureRepository.save(lecture);
        logger.info("The lecture was added to the teacher");
    }

    @Override
    @Transactional
    public void detachLectureFromTeacher(Long lectureId, Long teacherId) {
        Teacher teacher = teacherRepository
                .findById(teacherId)
                .orElseThrow(EntityNotFoundException::new);
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);

        teacher.getLectures().remove(lecture);
        lecture.setTeacher(null);
        teacherRepository.save(teacher);
        lectureRepository.save(lecture);
        logger.info("The lecture was removed from the teacher");
    }


    @Override
    public List<Lecture> showLecturesBetweenDates(Long teacherId, LocalDateTime firstDate, LocalDateTime secondDate) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new RuntimeException("The teacher was not found");
        }
        return teacherRepository.findLecturesByDateBetween(teacherId, firstDate, secondDate);
    }
}
