package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ua.com.foxminded.service.DataGeneratorService;
import ua.com.foxminded.service.impl.generators.*;


@Service
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final AdminGenerator adminGenerator;
    private final MaintainerGenerator maintainerGenerator;
    private final StudentGenerator studentGenerator;
    private final TeacherGenerator teacherGenerator;
    private final CourseGenerator courseGenerator;
    private final GroupGenerator groupGenerator;
    private final LectureGenerator lectureGenerator;
    private final FacultyGenerator facultyGenerator;


    public DataGeneratorServiceImpl(AdminGenerator adminGenerator,
                                    MaintainerGenerator maintainerGenerator,
                                    StudentGenerator studentGenerator,
                                    TeacherGenerator teacherGenerator,
                                    CourseGenerator courseGenerator,
                                    GroupGenerator groupGenerator,
                                    LectureGenerator lectureGenerator,
                                    FacultyGenerator facultyGenerator) {
        this.courseGenerator = courseGenerator;
        this.groupGenerator = groupGenerator;
        this.lectureGenerator = lectureGenerator;
        this.facultyGenerator = facultyGenerator;
        this.adminGenerator = adminGenerator;
        this.maintainerGenerator = maintainerGenerator;
        this.studentGenerator = studentGenerator;
        this.teacherGenerator = teacherGenerator;
    }

    @Override
    @PostConstruct
    public void generateDataIfEmpty() {
        maintainerGenerator.generateMaintainersIfEmpty();
        adminGenerator.generateAdministratorsIfEmpty();
        facultyGenerator.generateFacultiesIfEmpty();
        courseGenerator.generateCoursesIfEmpty();
        groupGenerator.generateGroupsIfEmpty();
        studentGenerator.generateStudentsIfEmpty();
        teacherGenerator.generateTeachersIfEmpty();
        lectureGenerator.generateLecturesIfEmpty();
    }
}


