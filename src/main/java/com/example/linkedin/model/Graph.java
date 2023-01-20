package com.example.linkedin.model;

import com.example.linkedin.repository.PersonRepository;
import lombok.AllArgsConstructor;

import java.util.*;

public class Graph {

    private PersonRepository personRepository;

    Map<Long,Set<Long>> vertices = new HashMap<>();

    public Set<Long> bfs(Long start) {
        Set<Long> answer = new HashSet<>();
        Set<Long> temp1 = new HashSet<>();
        Set<Long> temp2 = new HashSet<>();
        ArrayList<PersonScore> chosen = new ArrayList<>();

        for (Long l1 : vertices.get(start)) {
            answer.add(l1);
            temp1.add(l1);
        }
        for (Long l2 : temp1) {
            if (l2>=600)
                continue;
            for (Long l1 : vertices.get(l2)) {
                if (l1>=600)
                    continue;
                answer.add(l1);
                temp2.add(l1);
                chosen.add(new PersonScore(l1,5));
            }
        }
        temp1 = new HashSet<>();
        for (Long l2 : temp2) {
            if (l2>=600)
                continue;
            for (Long l1 : vertices.get(l2)) {
                if (l1>=600)
                    continue;
                answer.add(l1);
                temp1.add(l1);
                chosen.add(new PersonScore(l1,4));
            }
        }
        temp2 = new HashSet<>();
        for (Long l2 : temp1) {
            if (l2>=600)
                continue;
            for (Long l1 : vertices.get(l2)) {
                if (l1>=600)
                    continue;
                answer.add(l1);
                temp2.add(l1);
                chosen.add(new PersonScore(l1,3));

            }
        }
        temp1 = new HashSet<>();
        for (Long l2 : temp2) {
            if (l2>=600)
                continue;
            for (Long l1 : vertices.get(l2)) {
                if (l1>=600)
                    continue;
                answer.add(l1);
                temp1.add(l1);
                chosen.add(new PersonScore(l1,2));

            }
        }
        for (Long l2 : temp1) {
            if (l2>=600)
                continue;
            for (Long l1 : vertices.get(l2)) {
                if (l1>=600)
                    continue;
                answer.add(l1);
                chosen.add(new PersonScore(l1,1));

            }
        }
            return answer;

    }

    public Graph(ArrayList<Person> allUser){
        for (Person p: allUser){
            vertices.put(p.getId(),p.getConnections());
        }
    }
}
