package ua.com.foxminded.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.foxminded.entity.Administrator;
import ua.com.foxminded.repository.AdministratorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AdministratorServiceImpl.class})
class AdministratorServiceImplTest {

    @MockBean
    private AdministratorRepository administratorRepository;

    @Autowired
    private AdministratorServiceImpl administratorService;

    @BeforeEach
    void setUp() {
        Mockito.reset(administratorRepository);
    }

    @Test
    void saveAdministrator_ValidName_Success() {
        Administrator administrator = new Administrator(1L, "John", "Doe");
        when(administratorRepository.save(any(Administrator.class))).thenReturn(administrator);

        administratorService.save(administrator);

        verify(administratorRepository, times(1)).save(administrator);
    }

    @Test
    void saveAdministrator_InvalidName_ThrowsException() {
        Administrator administrator = new Administrator(1L, "John1", "Doe");
        assertThrows(RuntimeException.class, () -> administratorService.save(administrator));

        verify(administratorRepository, never()).save(administrator);
    }

    @Test
    void deleteAdministrator_Exists_Success() {
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(true);

        administratorService.delete(id);

        verify(administratorRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAdministrator_NotExists_ThrowsException() {
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> administratorService.delete(id));

        verify(administratorRepository, never()).deleteById(id);
    }

    @Test
    void findByIdAdministrator_Exists_Success() {
        Long id = 1L;
        Administrator administrator = new Administrator(id, "John", "Doe");
        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));

        Administrator result = administratorService.findById(id);

        assertNotNull(result);
        assertEquals(administrator, result);
    }

    @Test
    void findByIdAdministrator_NotExists_ThrowsException() {
        Long id = 1L;
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> administratorService.findById(id));
    }

    @Test
    void findAllAdministrators_Success() {
        Administrator administrator1 = new Administrator(1L, "John", "Doe");
        Administrator administrator2 = new Administrator(2L, "Jane", "Doe");
        when(administratorRepository.findAll()).thenReturn(Arrays.asList(administrator1, administrator2));

        List<Administrator> result = administratorService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(administrator1));
        assertTrue(result.contains(administrator2));
    }
}