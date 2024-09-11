package ua.com.foxminded.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.dto.StudentDTO;
import ua.com.foxminded.dto.TeacherDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "image", ignore = true)
  AdministratorDTO mapToDto(Administrator administrator);

  @Mapping(target = "image", ignore = true)
  MaintainerDTO mapToDto(Maintainer maintainer);

  @Mapping(target = "image", ignore = true)
  StudentDTO mapToDto(Student student);

  @Mapping(target = "image", ignore = true)
  TeacherDTO mapToDto(Teacher teacher);

  @Mapping(target = "imageName", ignore = true)
  Administrator mapFromDto(AdministratorDTO adminDTO);

  @Mapping(target = "imageName", ignore = true)
  Maintainer mapFromDto(MaintainerDTO maintainerDTO);

  @Mapping(target = "imageName", ignore = true)
  Student mapFromDto(StudentDTO studentDTO);

  @Mapping(target = "imageName", ignore = true)
  Teacher mapFromDto(TeacherDTO teacherDTO);
}
