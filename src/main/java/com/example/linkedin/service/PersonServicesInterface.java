package com.example.linkedin.service;

import com.example.linkedin.model.Person;
import com.example.linkedin.model.response.BooleanResponse;
import com.example.linkedin.model.response.PersonResponse;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface PersonServicesInterface {

    BooleanResponse readFile() throws IOException, ParseException;
    Set<Person> firstSuggestedConnections(Long id);
    Person login(Long id);
    Person showOthersData(Long userId,String inputToFind);
    Person search(String inputToFind);
    ArrayList<PersonResponse> allUsers();
    void connect(Long id1,Long id2);
    ArrayList<Person> recommended(Long id);
    ArrayList<Person> signIn(Person p);

    void changePriority(Long id,int levelScore, int fieldScore, int universityScore, int workPlaceScore, int specialitiesScore);
}
