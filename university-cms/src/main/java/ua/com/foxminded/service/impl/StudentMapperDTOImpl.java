package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.StudentMapperDTO;

@Service
public class StudentMapperDTOImpl implements StudentMapperDTO {

    @Override
    public StudentDTO mapToDto(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setAge(student.getAge());
        studentDTO.setGroup(student.getGroup());
        studentDTO.setGender(student.getGender());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setImage(null);
        return studentDTO;
    }

    @Override
    public Student mapFromDto(StudentDTO studentDTO) {
        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setAge(studentDTO.getAge());
        student.setGroup(studentDTO.getGroup());
        student.setGender(studentDTO.getGender());
        student.setEmail(studentDTO.getEmail());
        student.setImageName(null);
        return student;
    }
}
