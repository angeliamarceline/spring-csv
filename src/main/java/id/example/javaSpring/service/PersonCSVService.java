package id.example.javaSpring.service;

import id.example.javaSpring.helper.PersonCSVHelper;
import id.example.javaSpring.model.entity.Person;
import id.example.javaSpring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PersonCSVService {
    @Autowired
    PersonRepository repository;

    public void save(MultipartFile file) {
        try {
            List<Person> persons = PersonCSVHelper.csvToPerson(file.getInputStream());
            repository.saveAll(persons);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Person> getAllPersons() {
        return repository.findAll();
    }
}
