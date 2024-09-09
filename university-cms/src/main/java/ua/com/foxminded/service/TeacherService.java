package ua.com.foxminded.service;

import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.List;

public interface TeacherService extends UserService<Teacher, TeacherDTO> {
    void attachLectureToTeacher(Long lectureId, Long teacherId);

    void detachLectureFromTeacher(Long lectureId, Long teacherId);

    List<Lecture> showLecturesBetweenDates(
            Long teacherId, LocalDateTime firstDate, LocalDateTime secondDate);
}
