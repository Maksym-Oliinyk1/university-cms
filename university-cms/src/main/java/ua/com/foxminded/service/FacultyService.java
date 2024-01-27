package ua.com.foxminded.service;

import ua.com.foxminded.entity.Faculty;

public interface FacultyService extends EntityService<Faculty> {
    void attachCourseToFaculty(Long courseId, Long facultyId);

    void detachCourseFromFaculty(Long courseId, Long facultyId);
}
