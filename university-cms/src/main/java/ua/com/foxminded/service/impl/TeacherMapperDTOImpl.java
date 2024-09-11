package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherMapperDTO;

@Service
public class TeacherMapperDTOImpl implements TeacherMapperDTO {

    @Override
    public TeacherDTO mapToDto(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFirstName(teacher.getFirstName());
        teacherDTO.setLastName(teacher.getLastName());
        teacherDTO.setAcademicDegree(teacher.getAcademicDegree());
        teacherDTO.setLectures(teacher.getLectures());
        teacherDTO.setGender(teacher.getGender());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setImage(null);
        return teacherDTO;
    }

    @Override
    public Teacher mapFromDto(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setGender(teacherDTO.getGender());
        teacher.setAcademicDegree(teacherDTO.getAcademicDegree());
        teacher.setLectures(teacher.getLectures());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setImageName(null);
        return teacher;
    }
}
