package com.example.linkedin.repository;

import com.example.linkedin.model.LastNumber;
import com.example.linkedin.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface LastIdRepository extends JpaRepository<LastNumber, Long> {

    ArrayList<LastNumber> findAll();

}
