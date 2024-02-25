package ua.com.foxminded.dto;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.enums.Gender;

public class StudentDTO extends UserDTO {

    private Group group;

    public StudentDTO(Long id, String firstName, String lastName, Gender gender, Group group, int age, String email, MultipartFile multipartFile) {
        super(id, firstName, lastName, gender, age, email, multipartFile);
        this.group = group;
    }

    public StudentDTO() {

    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
