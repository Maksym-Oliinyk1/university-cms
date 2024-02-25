package ua.com.foxminded.service;

public interface UserService<E, D> extends BaseService<E> {
    void save(D user);

    void update(Long id, D user);
}
