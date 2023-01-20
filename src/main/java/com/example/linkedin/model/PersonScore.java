package com.example.linkedin.model;

public class PersonScore {

    Long id;
    int level;
    Boolean field;
    Boolean university;
    Boolean workplace;
    int finalScore;

    PersonScore(Long id, int level){
        this.id = id;
        this.level=level;
    }

}
