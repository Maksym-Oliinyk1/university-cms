package ua.com.foxminded.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User {

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;
}
