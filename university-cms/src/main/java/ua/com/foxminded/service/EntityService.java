package ua.com.foxminded.service;

public interface EntityService<E> extends BaseService<E> {
    void save(E e);

    void update(Long id, E e);
}
