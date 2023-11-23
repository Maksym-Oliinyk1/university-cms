package ua.com.foxminded.service;

import ua.com.foxminded.entity.Faculty;

public interface FacultyService extends BaseService<Faculty> {
    void attachCourseToFaculty(Long courseId, Long facultyId);
    void detachCourseFromFaculty(Long courseId, Long facultyId);
}
