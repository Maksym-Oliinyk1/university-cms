package ua.com.foxminded.service;

import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TeacherService extends UserService<TeacherDTO> {
    void attachLectureToTeacher(UUID lectureId, UUID teacherId);

    void detachLectureFromTeacher(UUID lectureId, UUID teacherId);

  List<Lecture> showLecturesBetweenDates(
          UUID teacherId, LocalDateTime firstDate, LocalDateTime secondDate);
}
