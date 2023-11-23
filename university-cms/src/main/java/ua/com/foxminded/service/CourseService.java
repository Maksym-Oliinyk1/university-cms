package ua.com.foxminded.service;

import ua.com.foxminded.entity.Course;

public interface CourseService extends BaseService<Course>{
    void attachLectureToCourse(Long lectureId, Long courseId);
    void detachLectureFromCourse(Long lectureId, Long courseId);
}
