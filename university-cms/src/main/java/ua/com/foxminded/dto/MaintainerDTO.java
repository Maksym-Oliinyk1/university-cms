package ua.com.foxminded.dto;

import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.enums.Gender;


public class MaintainerDTO extends UserDTO {
    public MaintainerDTO(Long id, String firstName, String lastName, Gender gender, int age, String email, MultipartFile multipartFile) {
        super(id, firstName, lastName, gender, age, email, multipartFile);
    }

    public MaintainerDTO() {

    }
}
