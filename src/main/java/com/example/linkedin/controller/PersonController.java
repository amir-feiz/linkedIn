package com.example.linkedin.controller;

import com.example.linkedin.model.Person;
import com.example.linkedin.model.request.Input;
import com.example.linkedin.model.request.PriorityInput;
import com.example.linkedin.model.request.TwoIdInput;
import com.example.linkedin.model.request.TwoPersonInput;
import com.example.linkedin.model.response.BooleanResponse;
import com.example.linkedin.model.response.PersonResponse;
import com.example.linkedin.service.PersonServicesInterface;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@RestController
@AllArgsConstructor

public class PersonController {

    private PersonServicesInterface personServices;

    @GetMapping("/upload")
    public ResponseEntity<BooleanResponse> upload() throws IOException, ParseException {
        return new ResponseEntity<>(personServices.readFile(), HttpStatus.OK);
    }

    @GetMapping("/login")
    @CrossOrigin
    public ResponseEntity<Person> login(@RequestBody Input input) throws IOException {
        return new ResponseEntity<>(personServices.login(Long.parseLong(input.getId())), HttpStatus.OK);
    }

    @GetMapping("/othersData")
    @CrossOrigin
    public ResponseEntity<Person> othersData(@RequestBody TwoPersonInput input) throws IOException {
        return new ResponseEntity<>(personServices.showOthersData(input.getId(), input.getInput()), HttpStatus.OK);
    }
    @GetMapping("/search")
    @CrossOrigin
    public ResponseEntity<Person> search(@RequestBody Input input) throws IOException {
        return new ResponseEntity<>(personServices.search(input.getId()), HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    @CrossOrigin
    public ResponseEntity<ArrayList<PersonResponse>> allUsers() {
        return new ResponseEntity<>(personServices.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/connect")
    @CrossOrigin
    public HttpStatus allUsers(@RequestBody TwoIdInput twoIdInput) {
        personServices.connect(twoIdInput.getId1(), twoIdInput.getId2());
        return HttpStatus.OK;
    }
    @GetMapping("/recommend")
    @CrossOrigin
    public ResponseEntity<ArrayList<Person>> recommend(@RequestBody Input input) {
        return new ResponseEntity<>(personServices.recommended(Long.parseLong(input.getId())), HttpStatus.OK);

    }

    @GetMapping("/signIn")
    @CrossOrigin
    public ResponseEntity<ArrayList<Person>> signIn(@RequestBody Person input) {
        return new ResponseEntity<>(personServices.signIn(input), HttpStatus.OK);

    }
    @GetMapping("/priorityChange")
    @CrossOrigin
    public HttpStatus allUsers(@RequestBody PriorityInput priorityInput) {
        personServices.changePriority(priorityInput.getId(),priorityInput.getLevelScore(), priorityInput.getFieldScore(),
                priorityInput.getUniversityScore(), priorityInput.getWorkPlaceScore(), priorityInput.getSpecialitiesScore());
        return HttpStatus.OK;
    }

}
