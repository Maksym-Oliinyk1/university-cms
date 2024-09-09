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
import ua.com.foxminded.dto.AdministratorDTO;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.ImageService;
import ua.com.foxminded.service.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {AdministratorServiceImpl.class})
class AdministratorServiceImplTest {

    private static final String USER_ROLE = "ADMINISTRATOR";

    @MockBean
    private AdministratorRepository administratorRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private AdministratorServiceImpl administratorService;

    @BeforeEach
    void setUp() {
        Mockito.reset(administratorRepository, imageService, userMapper);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        Administrator administrator = createAdministrator();
        AdministratorDTO administratorDTO = createAdministratorDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        administratorDTO.setImage(imageFile);
        when(userMapper.mapFromDto(administratorDTO)).thenReturn(administrator);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), anyLong(), any(MultipartFile.class)))
                .thenReturn("32.png");
        when(administratorRepository.save(any(Administrator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        administratorService.save(administratorDTO);

        verify(userMapper, times(1)).mapFromDto(administratorDTO);
        verify(imageService, times(1)).saveUserImage(anyString(), anyLong(), eq(imageFile));
        verify(administratorRepository, times(2)).save(administrator);
    }

    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Administrator administrator = createAdministrator();
        AdministratorDTO administratorDTO = createAdministratorDTO();
        when(userMapper.mapFromDto(administratorDTO)).thenReturn(administrator);
        when(imageService.getDefaultIUserImage(any(Gender.class), anyString()))
                .thenReturn("default.png");

        administratorService.save(administratorDTO);

        verify(userMapper, times(1)).mapFromDto(administratorDTO);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
        verify(administratorRepository, times(1)).save(administrator);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        Administrator administrator = createAdministrator();
        AdministratorDTO administratorDTO = createAdministratorDTO();
        when(userMapper.mapFromDto(administratorDTO)).thenReturn(administrator);

        administratorService.save(administratorDTO);

        verify(userMapper, times(1)).mapFromDto(administratorDTO);
        verify(imageService, times(1)).getDefaultIUserImage(any(Gender.class), anyString());
        verify(administratorRepository, times(1)).save(administrator);
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingAdministratorWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        AdministratorDTO administratorDTO = createAdministratorDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        administratorDTO.setImage(imageFile);
        Administrator existingAdministrator = createAdministrator();
        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class)))
                .thenReturn("32.png");

        administratorService.update(id, administratorDTO);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
        assertEquals("32.png", existingAdministrator.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        AdministratorDTO administratorDTO = createAdministratorDTO();
        Administrator existingAdministrator = createAdministrator();
        administratorDTO.setImage(null);
        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));

        administratorService.update(id, administratorDTO);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        AdministratorDTO administratorDTO = createAdministratorDTO();
        Administrator existingAdministrator = createAdministrator();
        MultipartFile emptyFile = mock(MultipartFile.class);

        lenient().when(emptyFile.isEmpty()).thenReturn(true);

        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));

        administratorService.update(id, administratorDTO);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void update_NonExistingAdministrator_ThrowException() {
        Long id = 1L;
        AdministratorDTO administratorDTO = createAdministratorDTO();
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administratorService.update(id, administratorDTO));

        verify(administratorRepository, never()).save(any(Administrator.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
        verify(imageService, never()).getDefaultIUserImage(Gender.MALE, USER_ROLE);
    }

    @Test
    void delete_ExistingAdministrator_DeleteSuccessful() {
        Long id = 1L;
        Administrator administrator = createAdministrator();
        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));

        administratorService.delete(id);

        verify(administratorRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(administrator.getImageName());
    }

    @Test
    void findByIdDTO_ExistingAdministrator_ReturnAdministratorDTO() {
        Long id = 1L;
        Administrator administrator = createAdministrator();
        AdministratorDTO administratorDTO = createAdministratorDTO();

        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));
        when(userMapper.mapToDto(administrator)).thenReturn(administratorDTO);

        AdministratorDTO result = administratorService.findByIdDTO(id);

        assertEquals(administratorDTO, result);
    }

    @Test
    void findByIdDTO_NonExistingAdministrator_ThrowException() {
        Long id = 1L;

        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administratorService.findByIdDTO(id));
    }

    @Test
    void findById_ExistingAdministrator_ReturnAdministrator() {
        Long id = 1L;
        Administrator administrator = createAdministrator();
        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));

        Administrator result = administratorService.findById(id);

        assertEquals(administrator, result);
    }

    @Test
    void findById_NonExistingAdministrator_ThrowException() {
        Long id = 1L;
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administratorService.findById(id));
    }

    @Test
    void findAll_ReturnPageOfAdministrators() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);

        when(administratorRepository.findAll(pageable)).thenReturn(page);

        Page<Administrator> result = administratorService.findAll(pageable);

        assertEquals(page, result);
    }

    private AdministratorDTO createAdministratorDTO() {
        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setFirstName("John");
        administratorDTO.setLastName("Doe");
        administratorDTO.setEmail("john.doe@example.com");
        administratorDTO.setGender(Gender.MALE);
        return administratorDTO;
    }

    private Administrator createAdministrator() {
        Administrator administrator = new Administrator();
        administrator.setId(1L);
        administrator.setFirstName("John");
        administrator.setLastName("Doe");
        administrator.setEmail("john.doe@example.com");
        administrator.setGender(Gender.MALE);
        return administrator;
    }
}
