package com.example.linkedin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table
@Data
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    String name;
    String dateOfBirth;
    String universityLocation;
    String field;
    String workplace;


    @ElementCollection
    Set<String> specialties = new HashSet<>();

    @ElementCollection
    Set<Long> connections = new HashSet<>();


}
