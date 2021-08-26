package id.example.javaSpring.controller;

import id.example.javaSpring.helper.PersonCSVHelper;
import id.example.javaSpring.message.ResponseMessage;
import id.example.javaSpring.model.dto.*;
import id.example.javaSpring.model.entity.Person;
import id.example.javaSpring.model.entity.Province;
import id.example.javaSpring.repository.PersonRepository;
import id.example.javaSpring.service.PersonCSVService;
import id.example.javaSpring.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    PersonCSVService personCSVService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPerson() {
        return personService.getAllPerson();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getPerson(@PathVariable Integer id) {
        return personService.getPerson(id);
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertPerson(@RequestBody PersonDto dto) {
        return personService.insertPerson(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDto dto, @PathVariable Integer id) {
        return personService.updatePerson(dto, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Integer id) {
       return personService.deletePerson(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (PersonCSVHelper.hasCSVFormat(file)) {
            try {
                personCSVService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping("/data")
    public ResponseEntity<List<Person>> getAllPersons() {
        try {
            List<Person> persons = personCSVService.getAllPersons();

            if (persons.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(persons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

