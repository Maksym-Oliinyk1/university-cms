package ua.com.foxminded.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.entity.Student;

import java.util.UUID;

public interface StudentService extends UserService<StudentDTO> {
    Page<Student> findAllStudentByGroup(UUID groupId, Pageable pageable);
}
