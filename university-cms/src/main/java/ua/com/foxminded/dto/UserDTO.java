package ua.com.foxminded.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.enums.Gender;

import java.time.LocalDate;

public abstract class UserDTO {
    private Long id;

    @Pattern(regexp = "[a-zA-Z]+", message = "First name should contain only letters")
    @NotNull
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]+", message = "Last name should contain only letters")
    @NotNull
    private String lastName;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate age;

    @Pattern(regexp = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")
    @NotNull
    private String email;
    private MultipartFile image;

    UserDTO(Long id, String firstName, String lastName, Gender gender, int age, String email, MultipartFile multipartFile) {
    }

    UserDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getAge() {
        return age;
    }

    public void setAge(LocalDate age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
