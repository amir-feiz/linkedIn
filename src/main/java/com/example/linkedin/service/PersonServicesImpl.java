package com.example.linkedin.service;

import com.example.linkedin.exception.EntityNotFoundException;
import com.example.linkedin.model.Graph;
import com.example.linkedin.model.LastNumber;
import com.example.linkedin.model.Person;
import com.example.linkedin.model.PersonScore;
import com.example.linkedin.model.response.BooleanResponse;
import com.example.linkedin.model.response.PersonResponse;
import com.example.linkedin.repository.LastIdRepository;
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
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class PersonServicesImpl implements PersonServicesInterface {

    private PersonRepository personRepository;
    private LastIdRepository lastIdRepository;
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
            lastIdRepository.deleteAll();
            lastIdRepository.save(new LastNumber(Long.valueOf(599)));

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

    @Override
    public ArrayList<Person> recommended(Long id) {

        Person thisPesron = personRepository.findById(id).get();

        Long lastId = lastIdRepository.findAll().get(0).getId();

        Graph graph = new Graph(personRepository.findAll());
        Long last = lastIdRepository.findAll().get(0).getId();
        ArrayList<PersonScore> scores = graph.bfs(Long.valueOf(id), last);
        for (int i = 0; i < scores.size(); i++)
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(i).getId() == scores.get(j).getId() ||
                        scores.get(j).getId() == id)
                    scores.remove(j);
            }
        Set<Long> zeroLevelConnections = personRepository.findById(id).get().getConnections();
        Person temp;
        for(int i=0;i<scores.size();i++){
            temp = personRepository.findById(scores.get(i).getId()).get();
            scores.get(i).setField(
                    thisPesron.getField().equals(temp.getField())

            );
            scores.get(i).setWorkplace(
                    thisPesron.getWorkplace().equals(temp.getWorkplace())
            );
            scores.get(i).setUniversity(
                    thisPesron.getUniversityLocation().equals(temp.getUniversityLocation())
            );

            int counter = 0;
            for(String s1:temp.getSpecialties())
                for (String s2:thisPesron.getSpecialties())
                    if (s1.equals(s2))
                        counter++;

            scores.get(i).setSpecialities(counter);
            scores.get(i).setFinalScore(
                    scores.get(i).getLevel()*thisPesron.getLevelScore() +
                            ((scores.get(i).getWorkplace()) ? 0:1) *thisPesron.getWorkPlaceScore() +
                            ((scores.get(i).getField()) ? 0:1)*thisPesron.getFieldScore() +
                            ((scores.get(i).getUniversity()) ? 0:1)*thisPesron.getUniversityScore()+
                            scores.get(i).getSpecialities()*thisPesron.getSpecialitiesScore()
            );



//            System.out.println(scores.get(i).getId() + "    "  + scores.get(i).getFinalScore());

        }
        PersonScore tempScore;
        for (int i=0;i<scores.size();i++)
            for (int j=0;j<scores.size()-1;j++)
                if (scores.get(j).getFinalScore() < scores.get(j+1).getFinalScore()){
                    tempScore = scores.get(j);
                    scores.set(j, scores.get(j+1));
                    scores.set(j+1,tempScore);
                }
//        for (int i=0;i<scores.size();i++)
//            System.out.println(scores.get(i).getId() + "         "+scores.get(i).getFinalScore());

        ArrayList<Person> answer = new ArrayList<>();
        int counter = 0;

        int i=0;
        while (counter < 20){
            if (scores.get(i).getId() != id && !zeroLevelConnections.contains(scores.get(i).getId())) {
                answer.add(personRepository.findById(scores.get(i).getId()).get());
                counter++;
            }
            i++;
            if (i== lastId)
                break;
        }

        if(counter<20)
            answer = recommendWithoutConnection(20-counter,answer,thisPesron);

        return answer;
    }

    @Override
    public ArrayList<Person> signIn(Person p) {
        p.setId(lastIdRepository.findAll().get(0).getId()+1);
        lastIdRepository.deleteAll();
        System.out.println(p.getId());
        lastIdRepository.save(new LastNumber(p.getId()));
        System.out.println(lastIdRepository.findAll().get(0));
        personRepository.save(p);
        ArrayList<Person> answer = new ArrayList<>();
        answer = recommendWithoutConnection(20,answer,p);
        return answer;
    }

    @Override
    public void changePriority(Long id,int levelScore, int fieldScore, int universityScore, int workPlaceScore, int specialitiesScore) {
        Person p = personRepository.findById(id).get();
        if (levelScore>3)
            levelScore =3;
        if (fieldScore>3)
            fieldScore =3;
        if (universityScore>3)
            universityScore =3;
        if (workPlaceScore>3)
            workPlaceScore =3;
        if (specialitiesScore>3)
            specialitiesScore =3;
        if (levelScore<0)
            levelScore =0;
        if (fieldScore<0)
            fieldScore =0;
        if (universityScore<0)
            universityScore =0;
        if (workPlaceScore<0)
            workPlaceScore =0;
        if (specialitiesScore<0)
            specialitiesScore =0;

        p.setSpecialitiesScore(specialitiesScore);
        p.setFieldScore(fieldScore);
        p.setLevelScore(levelScore);
        p.setWorkPlaceScore(workPlaceScore);
        p.setUniversityScore(universityScore);

        personRepository.save(p);


    }


    ArrayList<Person> recommendWithoutConnection(int number,ArrayList<Person> input,Person person){
        Long lastId = lastIdRepository.findAll().get(0).getId();
        Long i = Long.valueOf(1);
        int counter = 0;
        Person thisPerson = person;
        Person p;
        while (i<lastId-1){
//            System.out.println(i);
            p = personRepository.findById(i).get();
            if(p.getField().equals(thisPerson.getField()) &&  p.getWorkplace().equals(thisPerson.getWorkplace()) &&
                p.getUniversityLocation().equals(thisPerson.getUniversityLocation())&& !input.contains(p)) {
                input.add(p);
                counter++;
            }
            i++;
            counter++;
            if (counter == number)
                return input;
        }
        i= Long.valueOf(1);
         while (i<lastId){
             p = personRepository.findById(i).get();
             if(p.getField().equals(thisPerson.getField()) &&  p.getWorkplace().equals(thisPerson.getWorkplace()) && !input.contains(p)) {
                 input.add(p);
                 counter++;
             }else if (p.getField().equals(thisPerson.getField()) &&  p.getUniversityLocation().equals(thisPerson.getUniversityLocation()) && !input.contains(p)) {
                 input.add(p);
                 counter++;
             }else if (p.getWorkplace().equals(thisPerson.getWorkplace()) &&  p.getUniversityLocation().equals(thisPerson.getUniversityLocation()) && !input.contains(p)) {
                 input.add(p);
                 counter++;
             }
             i++;
             if (counter == number)
                 return input;
         }

         return input;
    }
}
