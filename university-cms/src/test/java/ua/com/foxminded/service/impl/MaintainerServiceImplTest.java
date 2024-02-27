package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.com.foxminded.dto.MaintainerDTO;
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MaintainerServiceImpl.class})
class MaintainerServiceImplTest {

    private static final String USER_ROLE = "MAINTAINER";

    @MockBean
    private MaintainerRepository maintainerRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MaintainerServiceImpl maintainerService;

    @BeforeEach
    void setUp() {
        Mockito.reset(maintainerRepository, imageService, userMapper);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        Maintainer maintainer = createMaintainer();
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        maintainerDTO.setImage(imageFile);
        when(userMapper.mapFromDto(maintainerDTO)).thenReturn(maintainer);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), anyLong(), any(MultipartFile.class))).thenReturn("32.png");
        when(maintainerRepository.save(any(Maintainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        maintainerService.save(maintainerDTO);

        verify(userMapper, times(1)).mapFromDto(maintainerDTO);
        verify(imageService, times(1)).saveUserImage(anyString(), anyLong(), eq(imageFile));
        verify(maintainerRepository, times(2)).save(maintainer);
    }

    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Maintainer maintainer = createMaintainer();
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        when(userMapper.mapFromDto(maintainerDTO)).thenReturn(createMaintainer());
        when(imageService.getDefaultIUserImage(any(Gender.class), anyString())).thenReturn("default.png");

        maintainerService.save(maintainerDTO);

        verify(userMapper, times(1)).mapFromDto(maintainerDTO);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
        verify(maintainerRepository, times(1)).save(maintainer);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        Maintainer maintainer = createMaintainer();
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        when(userMapper.mapFromDto(maintainerDTO)).thenReturn(createMaintainer());

        maintainerService.save(maintainerDTO);

        verify(userMapper, times(1)).mapFromDto(maintainerDTO);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
        verify(maintainerRepository, times(1)).save(maintainer);
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingMaintainerWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        maintainerDTO.setImage(imageFile);
        Maintainer existingMaintainer = createMaintainer();
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class))).thenReturn("32.png");

        maintainerService.update(id, maintainerDTO);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
        assertEquals("32.png", existingMaintainer.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        Maintainer existingMaintainer = createMaintainer();
        maintainerDTO.setImage(null);
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));

        maintainerService.update(id, maintainerDTO);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        Maintainer existingMaintainer = createMaintainer();
        MultipartFile emptyFile = mock(MultipartFile.class);

        lenient().when(emptyFile.isEmpty()).thenReturn(true);

        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));

        maintainerService.update(id, maintainerDTO);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_NonExistingMaintainer_ThrowException() {
        Long id = 1L;
        MaintainerDTO maintainerDTO = createMaintainerDTO();
        when(maintainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> maintainerService.update(id, maintainerDTO));

        verify(maintainerRepository, never()).save(any(Maintainer.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
        verify(imageService, never()).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void delete_ExistingMaintainer_DeleteSuccessful() {
        Long id = 1L;
        Maintainer maintainer = createMaintainer();
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(maintainer));

        maintainerService.delete(id);

        verify(maintainerRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(maintainer.getImageName());
    }

    @Test
    void findByIdDTO_ExistingMaintainer_ReturnMaintainerDTO() {
        Long id = 1L;
        Maintainer maintainer = createMaintainer();
        MaintainerDTO maintainerDTO = createMaintainerDTO();

        when(maintainerRepository.findById(id)).thenReturn(Optional.of(maintainer));
        when(userMapper.mapToDto(maintainer)).thenReturn(maintainerDTO);

        MaintainerDTO result = maintainerService.findByIdDTO(id);

        assertEquals(maintainerDTO, result);
    }

    @Test
    void findByIdDTO_NonExistingMaintainer_ThrowException() {
        Long id = 1L;

        when(maintainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> maintainerService.findByIdDTO(id));
    }

    @Test
    void findById_ExistingMaintainer_ReturnMaintainer() {
        Long id = 1L;
        Maintainer maintainer = createMaintainer();
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(maintainer));

        Maintainer result = maintainerService.findById(id);

        assertEquals(maintainer, result);
    }

    @Test
    void findById_NonExistingMaintainer_ThrowException() {
        Long id = 1L;
        when(maintainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> maintainerService.findById(id));
    }

    @Test
    void findAll_ReturnPageOfMaintainers() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);

        when(maintainerRepository.findAll(pageable)).thenReturn(page);

        Page<Maintainer> result = maintainerService.findAll(pageable);

        assertEquals(page, result);
    }

    private MaintainerDTO createMaintainerDTO() {
        MaintainerDTO maintainerDTO = new MaintainerDTO();
        maintainerDTO.setFirstName("John");
        maintainerDTO.setLastName("Doe");
        maintainerDTO.setEmail("john.doe@example.com");
        maintainerDTO.setGender(Gender.MALE);
        return maintainerDTO;
    }

    private Maintainer createMaintainer() {
        Maintainer maintainer = new Maintainer();
        maintainer.setId(1L);
        maintainer.setFirstName("John");
        maintainer.setLastName("Doe");
        maintainer.setEmail("john.doe@example.com");
        maintainer.setGender(Gender.MALE);
        return maintainer;
    }
}




