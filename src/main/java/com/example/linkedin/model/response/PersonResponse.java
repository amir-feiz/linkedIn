package com.example.linkedin.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class PersonResponse {
    Long id;
    String name;
    Set<String> skills;
    Set<Long> connections;
}
