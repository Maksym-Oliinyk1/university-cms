package ua.com.foxminded.service;

import ua.com.foxminded.entity.Lecture;
import ua.com.foxminded.entity.Teacher;

import java.util.List;

public interface TeacherService extends BaseService<Teacher> {
    void attachLectureToTeacher(Long lectureId, Long teacherId);

    void detachLectureFromTeacher(Long lectureId, Long teacherId);

    List<Lecture> showLecturesPerWeek(Long teacherId);

    List<Lecture> showLecturesPerMonth(Long teacherId);
}

