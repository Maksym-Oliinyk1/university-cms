package ua.com.foxminded.service;

public interface UserMapperDTO<U, D> {
    D mapToDto(U user);

    U mapFromDto(D userDTO);
}
