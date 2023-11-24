package ua.com.foxminded.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.time.LocalDateTime;
import java.util.List;

public interface TeacherService extends BaseService<Teacher> {
    void attachLectureToTeacher(Long lectureId, Long teacherId);

    void detachLectureFromTeacher(Long lectureId, Long teacherId);

    List<Lecture> showLecturesPerWeek(Long teacherId);

    List<Lecture> showLecturesPerMonth(Long teacherId);

    List<Lecture> showLecturesBetweenDates(Long teacherId, LocalDateTime firstDate, LocalDateTime secondDate);
}
