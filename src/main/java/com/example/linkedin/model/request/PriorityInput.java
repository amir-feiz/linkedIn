package com.example.linkedin.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorityInput {
    Long id;
    int levelScore = 1;
    int fieldScore=1;
    int universityScore = 1;
    int workPlaceScore = 1;
    int specialitiesScore=1;

}
