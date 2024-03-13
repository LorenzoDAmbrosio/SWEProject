package com.project.sweprojectspring.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // getters and setters omitted for brevity
}

interface UserRepository extends Repository<User, Long> {

    User save(User User);

    Optional<User> findById(long id);
}
