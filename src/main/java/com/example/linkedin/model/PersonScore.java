package com.example.linkedin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class PersonScore implements Comparable {

    private Long id;
    private int level;
    private Boolean field;
    private Boolean university;
    private Boolean workplace;
    private int specialities = 0;
    private int finalScore;

    PersonScore(Long id, int level){
        this.id = id;
        this.level=level;
    }

    @Override
    public int compareTo(Object o) {
        PersonScore p = (PersonScore) o;
        if (this.getFinalScore() >= p.getFinalScore())
            return 1;
        else return 0;
    }
}
