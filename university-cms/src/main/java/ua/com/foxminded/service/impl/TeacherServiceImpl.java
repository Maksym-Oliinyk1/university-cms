package ua.com.foxminded.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.LectureRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.TeacherService;
import ua.com.foxminded.service.UserEmailService;
import ua.com.foxminded.service.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    private static final String TEACHER_ROLE = "TEACHER";
    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final ImageService imageService;
    private final UserEmailService userEmailService;
    private final UserMapper userMapper;

    @Autowired
    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            LectureRepository lectureRepository,
            ImageService imageService,
            UserEmailService userEmailService,
            UserMapper userMapper) {
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
        this.imageService = imageService;
        this.userEmailService = userEmailService;
        this.userMapper = userMapper;
    }

    @Override
    public TeacherDTO save(TeacherDTO teacherDTO) {
        if (!isEmailFree(teacherDTO.getEmail())) {
            throw new RuntimeException();
        }
        if (teacherDTO.getImage() == null || teacherDTO.getImage().isEmpty()) {
            Teacher teacher = userMapper.mapFromDto(teacherDTO);
            teacher.setImageName(imageService.getDefaultIUserImage(teacherDTO.getGender(), TEACHER_ROLE));
            logger.info("Saved teacher: {} {}", teacherDTO.getFirstName(), teacherDTO.getLastName());
            return userMapper.mapToDto(teacherRepository.save(teacher));
        } else {
            Teacher teacher = userMapper.mapFromDto(teacherDTO);
            teacher = teacherRepository.save(teacher);
            String imageName = imageService.saveUserImage(TEACHER_ROLE, teacherDTO.getImage());
            teacher.setImageName(imageName);
            logger.info("Saved teacher: {} {}", teacherDTO.getFirstName(), teacherDTO.getLastName());
            return userMapper.mapToDto(teacherRepository.save(teacher));
        }
    }

    @Override
    @Transactional
    public TeacherDTO update(UUID id, TeacherDTO teacherDTO) {
        if (!isEmailFree(teacherDTO.getEmail())) {
            throw new RuntimeException();
        }
        Teacher existingTeacher =
                teacherRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        existingTeacher.setFirstName(teacherDTO.getFirstName());
        existingTeacher.setLastName(teacherDTO.getLastName());
        existingTeacher.setAcademicDegree(teacherDTO.getAcademicDegree());
        existingTeacher.setBirthDate(teacherDTO.getBirthDate());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setLectures(teacherDTO.getLectures());

        if (teacherDTO.getImage() == null || teacherDTO.getImage().isEmpty()) {
            imageService.deleteUserImage(existingTeacher.getImageName());
            existingTeacher.setImageName(
                    imageService.getDefaultIUserImage(teacherDTO.getGender(), TEACHER_ROLE));
        } else {
            imageService.deleteUserImage(existingTeacher.getImageName());
            String imageName = imageService.saveUserImage(TEACHER_ROLE, teacherDTO.getImage());
            existingTeacher.setImageName(imageName);
        }
        logger.info("Teacher updated by id: {}", id);
        return userMapper.mapToDto(teacherRepository.save(existingTeacher));
    }

    @Override
    public TeacherDTO findByIdDTO(UUID id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            logger.info("The teacher was found by id: {}", id);
            return userMapper.mapToDto(optionalTeacher.get());
        } else {
            throw new RuntimeException("There is no such teacher");
        }
    }

    @Override
    public TeacherDTO findByEmail(String email) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(email);
        if (optionalTeacher.isPresent()) {
            return userMapper.mapToDto(optionalTeacher.get());
        }
        throw new RuntimeException("There is no such teacher");
    }

    @Override
    public void delete(UUID id) {
        TeacherDTO teacherDto = findById(id);
        String imageName = userMapper.mapFromDto(teacherDto).getImageName();
        imageService.deleteUserImage(imageName);
        teacherRepository.deleteById(id);
        logger.info("Teacher was deleted by id: {}", id);
    }

    @Override
    public TeacherDTO findById(UUID id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            return userMapper.mapToDto(optionalTeacher.get());
        } else {
            throw new RuntimeException("There is no such teacher");
        }
    }

    @Override
    public Page<TeacherDTO> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        logger.info("Find teachers from {} to {}", from, to);

        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        List<TeacherDTO> teacherDTOList =
                teacherPage.stream().map(userMapper::mapToDto).collect(Collectors.toList());

        return new PageImpl<>(teacherDTOList, pageable, teacherPage.getTotalElements());
    }

    @Override
    public Long count() {
        return teacherRepository.count();
    }

    @Transactional
    public void attachLectureToTeacher(UUID lectureId, UUID teacherId) {
        Teacher teacher =
                teacherRepository.findById(teacherId).orElseThrow(EntityNotFoundException::new);
        Lecture lecture =
                lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

        teacher.getLectures().add(lecture);
        lecture.setTeacher(teacher);
        teacherRepository.save(teacher);
        lectureRepository.save(lecture);
        logger.info("The lecture was added to the teacher");
    }

    @Transactional
    public void detachLectureFromTeacher(UUID lectureId, UUID teacherId) {
        Teacher teacher =
                teacherRepository.findById(teacherId).orElseThrow(EntityNotFoundException::new);
        Lecture lecture =
                lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

        teacher.getLectures().remove(lecture);
        lecture.setTeacher(null);
        teacherRepository.save(teacher);
        lectureRepository.save(lecture);
        logger.info("The lecture was removed from the teacher");
    }

    public List<Lecture> showLecturesBetweenDates(
            UUID teacherId, LocalDateTime firstDate, LocalDateTime secondDate) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new RuntimeException("The teacher was not found");
        }
        return teacherRepository.findLecturesByDateBetween(teacherId, firstDate, secondDate);
    }

    private boolean isEmailFree(String email) {
        return userEmailService.isUserExistByEmail(email);
    }
}
