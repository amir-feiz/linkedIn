package com.example.linkedin.model;

import com.example.linkedin.repository.PersonRepository;
import lombok.AllArgsConstructor;

import java.util.*;

public class Graph {

    PersonRepository personRepository;

    Map<Long,Set<Long>> vertices = new HashMap<>();

    public Set<Long> bfs(Long start){
        Set<Long> answer = new HashSet<>();
        for(Long l1:vertices.get(start))
            answer.add(l1);
        for (int i=0;i<5;i++)
            for (Long l2 : answer)
                for (Long l1 : vertices.get(l2))
                    answer.add(l1);

        return answer;
    }

    public Graph(){
        ArrayList<Person> allPeople = personRepository.findAll();
        for (Person p: allPeople){
            vertices.put(p.getId(),p.getConnections());
        }
    }
}
