package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.service.AdminMapperDTO;

@Service
public class AdminMapperDTOImpl implements AdminMapperDTO {

    @Override
    public AdministratorDTO mapToDto(Administrator administrator) {
        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setId(administratorDTO.getId());
        administratorDTO.setFirstName(administrator.getFirstName());
        administratorDTO.setLastName(administrator.getLastName());
        administratorDTO.setGender(administrator.getGender());
        administratorDTO.setAge(administrator.getAge());
        administratorDTO.setEmail(administrator.getEmail());
        administratorDTO.setImage(null);
        return administratorDTO;
    }

    @Override
    public Administrator mapFromDto(AdministratorDTO administratorDTO) {
        Administrator administrator = new Administrator();
        administrator.setFirstName(administratorDTO.getFirstName());
        administrator.setLastName(administratorDTO.getLastName());
        administrator.setGender(administratorDTO.getGender());
        administrator.setAge(administratorDTO.getAge());
        administrator.setEmail(administratorDTO.getEmail());
        administrator.setImageName(null);
        return administrator;
    }
}
