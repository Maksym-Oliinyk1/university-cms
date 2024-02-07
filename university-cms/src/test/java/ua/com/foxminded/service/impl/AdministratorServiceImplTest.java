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
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.AdministratorRepository;
import ua.com.foxminded.service.ImageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {AdministratorServiceImpl.class})
class AdministratorServiceImplTest {
    private static final String ADMIN_ROLE = "ADMIN";

    @MockBean
    private AdministratorRepository administratorRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private AdministratorServiceImpl administratorService;

    @BeforeEach
    void setUp() {
        Mockito.reset(administratorRepository);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        Administrator admin = createAdministrator();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(eq(ADMIN_ROLE), anyLong(), eq(imageFile))).thenReturn("32.png");
        when(administratorRepository.save(admin)).thenReturn(admin);
        administratorService.save(admin, imageFile);
        verify(administratorRepository, times(2)).save(admin);
        verify(imageService, times(1)).saveUserImage(eq(ADMIN_ROLE), eq(admin.getId()), eq(imageFile));
        assertEquals("32.png", admin.getImageName());
    }


    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Administrator admin = createAdministrator();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        administratorService.save(admin, emptyFile);

        verify(administratorRepository, times(1)).save(admin);
        verify(imageService, times(1)).setDefaultImageForUser(admin);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        Administrator admin = createAdministrator();

        administratorService.save(admin, null);

        verify(administratorRepository, times(1)).save(admin);
        verify(imageService, times(1)).setDefaultImageForUser(admin);
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void save_InvalidData_ThrowException() {
        Administrator admin = createAdministrator();
        admin.setEmail("invalidemail");

        assertThrows(RuntimeException.class, () -> administratorService.save(admin, null));

        verify(administratorRepository, never()).save(any(Administrator.class));
        verify(imageService, never()).setDefaultImageForUser(any(Administrator.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingAdministratorWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        Administrator existingAdministrator = createAdministrator();
        Administrator updatedAdministrator = createAdministrator();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyString(), eq(id), any(MultipartFile.class))).thenReturn("32.png");

        administratorService.update(id, updatedAdministrator, imageFile);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).saveUserImage(anyString(), eq(id), eq(imageFile));
        assertEquals("32.png", existingAdministrator.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        Administrator existingAdministrator = createAdministrator();
        Administrator updatedAdministrator = createAdministrator();
        updatedAdministrator.setImageName(null);
        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));

        administratorService.update(id, updatedAdministrator, null);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).setDefaultImageForUser(existingAdministrator);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        Administrator existingAdministrator = createAdministrator();
        Administrator updatedAdministrator = createAdministrator();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(administratorRepository.findById(id)).thenReturn(Optional.of(existingAdministrator));

        administratorService.update(id, updatedAdministrator, emptyFile);

        verify(administratorRepository, times(1)).save(existingAdministrator);
        verify(imageService, times(1)).setDefaultImageForUser(existingAdministrator);
    }

    @Test
    void update_NonExistingAdministrator_ThrowException() {
        Long id = 1L;
        Administrator updatedAdministrator = createAdministrator();
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administratorService.update(id, updatedAdministrator, null));

        verify(administratorRepository, never()).save(any(Administrator.class));
        verify(imageService, never()).saveUserImage(anyString(), anyLong(), any(MultipartFile.class));
        verify(imageService, never()).setDefaultImageForUser(any(Administrator.class));
    }

    @Test
    void delete_ExistingAdministrator_DeleteSuccessful() {
        Long id = 1L;
        Administrator administrator = createAdministrator();
        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));

        administratorService.delete(id);

        verify(administratorRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(anyString(), id);
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

