package ua.com.foxminded.service.impl;

import org.springframework.stereotype.Service;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.service.MaintainerMapperDTO;

@Service
public class MaintainerMapperDTOImpl implements MaintainerMapperDTO {

    @Override
    public MaintainerDTO mapToDto(Maintainer maintainer) {
        MaintainerDTO maintainerDTO = new MaintainerDTO();
        maintainerDTO.setId(maintainer.getId());
        maintainerDTO.setFirstName(maintainer.getFirstName());
        maintainerDTO.setLastName(maintainer.getLastName());
        maintainerDTO.setAge(maintainer.getAge());
        maintainerDTO.setGender(maintainer.getGender());
        maintainerDTO.setEmail(maintainer.getEmail());
        maintainerDTO.setImage(null);
        return maintainerDTO;
    }

    @Override
    public Maintainer mapFromDto(MaintainerDTO maintainerDTO) {
        Maintainer maintainer = new Maintainer();
        maintainer.setFirstName(maintainerDTO.getFirstName());
        maintainer.setLastName(maintainerDTO.getLastName());
        maintainer.setAge(maintainerDTO.getAge());
        maintainer.setGender(maintainerDTO.getGender());
        maintainer.setEmail(maintainerDTO.getEmail());
        maintainer.setImageName(null);
        return maintainer;
    }
}
