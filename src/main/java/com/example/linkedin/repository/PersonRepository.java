package com.example.linkedin.repository;

import com.example.linkedin.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByName(String name);
    ArrayList<Person> findAll();
}
