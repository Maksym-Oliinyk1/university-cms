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
import ua.com.foxminded.entity.Maintainer;
import ua.com.foxminded.enums.Gender;
import ua.com.foxminded.repository.MaintainerRepository;
import ua.com.foxminded.service.ImageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MaintainerServiceImpl.class})
class MaintainerServiceImplTest {

    @MockBean
    private MaintainerRepository maintainerRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private MaintainerServiceImpl maintainerService;

    @BeforeEach
    void setUp() {
        Mockito.reset(maintainerRepository);
    }

    @Test
    void save_ValidDataWithImage_SaveSuccessful() {
        Maintainer maintainer = createMaintainer();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(anyLong(), any(MultipartFile.class))).thenReturn("32.png");

        maintainerService.save(maintainer, imageFile);

        verify(maintainerRepository, times(1)).save(maintainer);
        verify(imageService, times(1)).saveUserImage(eq(maintainer.getId()), eq(imageFile));
        assertEquals("32.png", maintainer.getImageName());
    }

    @Test
    void save_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Maintainer maintainer = createMaintainer();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        maintainerService.save(maintainer, emptyFile);

        verify(maintainerRepository, times(1)).save(maintainer);
        verify(imageService, times(1)).setDefaultImageForUser(maintainer);
    }

    @Test
    void save_ValidDataWithoutImage_SaveSuccessful() {
        Maintainer maintainer = createMaintainer();

        maintainerService.save(maintainer, null);

        verify(maintainerRepository, times(1)).save(maintainer);
        verify(imageService, times(1)).setDefaultImageForUser(maintainer);
        verify(imageService, never()).saveUserImage(anyLong(), any(MultipartFile.class));
    }

    @Test
    void save_InvalidData_ThrowException() {
        Maintainer maintainer = createMaintainer();
        maintainer.setEmail("invalidemail");

        assertThrows(RuntimeException.class, () -> maintainerService.save(maintainer, null));

        verify(maintainerRepository, never()).save(any(Maintainer.class));
        verify(imageService, never()).setDefaultImageForUser(any(Maintainer.class));
        verify(imageService, never()).saveUserImage(anyLong(), any(MultipartFile.class));
    }

    @Test
    void update_ExistingMaintainerWithValidDataAndImage_UpdateSuccessful() {
        Long id = 1L;
        Maintainer existingMaintainer = createMaintainer();
        Maintainer updatedMaintainer = createMaintainer();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageService.saveUserImage(eq(id), any(MultipartFile.class))).thenReturn("32.png");

        maintainerService.update(id, updatedMaintainer, imageFile);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).saveUserImage(eq(id), eq(imageFile));
        assertEquals("32.png", existingMaintainer.getImageName());
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsNull() {
        Long id = 1L;
        Maintainer existingMaintainer = createMaintainer();
        Maintainer updatedMaintainer = createMaintainer();
        updatedMaintainer.setImageName(null);
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));

        maintainerService.update(id, updatedMaintainer, null);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).setDefaultImageForUser(existingMaintainer);
    }

    @Test
    void update_ShouldSetDefaultImageWhenImageFileIsEmpty() {
        Long id = 1L;
        Maintainer existingMaintainer = createMaintainer();
        Maintainer updatedMaintainer = createMaintainer();
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(existingMaintainer));

        maintainerService.update(id, updatedMaintainer, emptyFile);

        verify(maintainerRepository, times(1)).save(existingMaintainer);
        verify(imageService, times(1)).setDefaultImageForUser(existingMaintainer);
    }

    @Test
    void update_NonExistingMaintainer_ThrowException() {
        Long id = 1L;
        Maintainer updatedMaintainer = createMaintainer();
        when(maintainerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> maintainerService.update(id, updatedMaintainer, null));

        verify(maintainerRepository, never()).save(any(Maintainer.class));
        verify(imageService, never()).saveUserImage(anyLong(), any(MultipartFile.class));
        verify(imageService, never()).setDefaultImageForUser(any(Maintainer.class));
    }

    @Test
    void delete_ExistingMaintainer_DeleteSuccessful() {
        Long id = 1L;
        Maintainer maintainer = createMaintainer();
        when(maintainerRepository.findById(id)).thenReturn(Optional.of(maintainer));

        maintainerService.delete(id);

        verify(maintainerRepository, times(1)).deleteById(id);
        verify(imageService, times(1)).deleteUserImage(id);
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



