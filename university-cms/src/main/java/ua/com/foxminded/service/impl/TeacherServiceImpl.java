package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.TeacherService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;

    private static final Long INVALID_NUMBER_OF_LECTURES = 0L;


    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, LectureRepository lectureRepository) {
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void create(Teacher teacher) {
        if (isValidName(teacher.getFirstName()) && isValidName(teacher.getLastName())) {
            teacherRepository.save(teacher);
            logger.info("Created teacher: {} {}", teacher.getFirstName(), teacher.getLastName());
        } else {
            throw new RuntimeException("Invalid name for teacher");
        }
    }

    @Override
    public void update(Teacher teacher) {
        if (isValidName(teacher.getFirstName()) && isValidName(teacher.getLastName())) {
            teacherRepository.save(teacher);
            logger.info("Updated teacher: {} {}", teacher.getFirstName(), teacher.getLastName());
        } else {
            throw new RuntimeException("Invalid name for teacher");
        }
    }

    @Override
    public void delete(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            logger.info("Teacher deleted by id: {}", id);
        } else {
            throw new RuntimeException("Teacher was not found by id");
        }
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
    public List<Teacher> findAll() {
        return (List<Teacher>) teacherRepository.findAll();
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
    public List<Lecture> showLecturesPerWeek(Long teacherId) {
        if (teacherRepository.existsById(teacherId)) {
            if (teacherRepository.countLecturesByTeacher(teacherId).equals(INVALID_NUMBER_OF_LECTURES)) {
                throw new RuntimeException("There is no any lecture");
            } else {
                LocalDateTime localDateTimeNextWeek = LocalDateTime.now().plusWeeks(1);
                return teacherRepository.findLecturesByDateBetween(teacherId, LocalDateTime.now(), localDateTimeNextWeek);
            }
        } else {
            throw new RuntimeException("The teacher was not found");
        }
    }

    @Override
    public List<Lecture> showLecturesPerMonth(Long teacherId) {
        {
            if (teacherRepository.existsById(teacherId)) {
                if (teacherRepository.countLecturesByTeacher(teacherId).equals(INVALID_NUMBER_OF_LECTURES)) {
                    throw new RuntimeException("There is no any lecture");
                } else {
                    LocalDateTime localDateTimeNextWeek = LocalDateTime.now().plusMonths(1);
                    return teacherRepository.findLecturesByDateBetween(teacherId, LocalDateTime.now(), localDateTimeNextWeek);
                }
            } else {
                throw new RuntimeException("The teacher was not found");
            }
        }
    }

    @Override
    public List<Lecture> showLecturesBetweenDates(Long teacherId, LocalDateTime firstDate, LocalDateTime secondDate) {
        if (teacherRepository.existsById(teacherId)) {
            if (teacherRepository.countLecturesByTeacher(teacherId).equals(INVALID_NUMBER_OF_LECTURES)) {
                throw new RuntimeException("There is no any lecture");
            } else {
                return teacherRepository.findLecturesByDateBetween(teacherId, firstDate, secondDate);
            }
        } else {
            throw new RuntimeException("The teacher was not found");
        }
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z]+");
    }

}
