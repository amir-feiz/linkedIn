package com.example.linkedin.service;

import com.example.linkedin.exception.EntityNotFoundException;
import com.example.linkedin.model.Person;
import com.example.linkedin.model.response.BooleanResponse;
import com.example.linkedin.model.response.PersonResponse;
import com.example.linkedin.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class PersonServicesImpl implements PersonServicesInterface {

    private PersonRepository personRepository;

    @Override
    public BooleanResponse readFile() throws IOException, ParseException {

            JSONParser parser = new JSONParser();

            JSONArray a = (JSONArray) parser.parse(new FileReader("C:\\Users\\hp\\Desktop\\users2.json"));

            Person person;

            for (Object o : a) {
                JSONObject p = (JSONObject) o;

                person = new Person();

                person.setId(Long.parseLong((String) p.get("id")));
                person.setName((String) p.get("name"));
                person.setDateOfBirth((String) p.get("dateOfBirth"));
                person.setUniversityLocation((String) p.get("universityLocation"));
                person.setField((String) p.get("field"));
                person.setWorkplace((String) p.get("workplace"));

                JSONArray specialtiesJSonArray = ((JSONArray)p.get("specialties"));
                for (Object o1:specialtiesJSonArray)
                    person.getSpecialties().add((String)o1);

                JSONArray connectionJSonArray = ((JSONArray)p.get("connectionId"));
                for (Object o1:connectionJSonArray)
                    person.getConnections().add(Long.parseLong((String)o1));
                personRepository.save(person);
            }

        return new BooleanResponse(true);
    }

    @Override
    public Set<Person> firstSuggestedConnections(Long id) {

        Set<Person> firstPeopleSuggestionList = new HashSet<>();
        Optional<Person> person = personRepository.findById(id);

        for (Long i1: person.get().getConnections())
            for (Long i2: personRepository.findById(i1).get().getConnections())
                for (Long i3: personRepository.findById(i2).get().getConnections())
                    for (Long i4: personRepository.findById(i3).get().getConnections())
                        for (Long i5: personRepository.findById(i4).get().getConnections())
                            for (Long i6: personRepository.findById(i5).get().getConnections())
                                firstPeopleSuggestionList.add(personRepository.findById(i6).get());



                return firstPeopleSuggestionList;
    }

    @Override
    public Person login(Long id) {
        Person person;

        if(personRepository.findById(id).isEmpty()){
            throw new EntityNotFoundException(id);
        }
        person = personRepository.findById(id).get();

        return person;

    }

    @Override
    public Person showOthersData(Long userId, String inputToFind) {

        if(personRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException(userId);
        }else {
            return search(inputToFind);
        }
    }
    public Person search(String inputToFind) {
        Person person;

            if(inputToFind.charAt(0)>=48 && inputToFind.charAt(0)<=57){

                Long secondId = Long.parseLong(inputToFind);
                if (personRepository.findById(secondId).isEmpty())
                    throw new EntityNotFoundException(inputToFind);
                person = personRepository.findById(secondId).get();

            }else {
                if (personRepository.findByName(inputToFind).isEmpty())
                    throw new EntityNotFoundException(inputToFind);
                person = personRepository.findByName(inputToFind).get();

            }
            return person;
    }

    @Override
    public ArrayList<PersonResponse> allUsers() {
        ArrayList<PersonResponse> personResponses= new ArrayList<>();
        ArrayList<Person> allUser = personRepository.findAll();
        for (Person p: allUser){
            personResponses.add(new PersonResponse(p.getId(), p.getName(),p.getSpecialties(),p.getConnections()));
        }
        return  personResponses;
    }

    @Override
    public void connect(Long id1, Long id2) {
        if (personRepository.findById(id1).isEmpty() || personRepository.findById(id2).isEmpty())
            throw new EntityNotFoundException();
        Person p1 = personRepository.findById(id1).get();
        Person p2 = personRepository.findById(id2).get();

        p1.getConnections().add(p2.getId());
        p2.getConnections().add(p1.getId());

        personRepository.save(p1);
        personRepository.save(p2);

    }
}
