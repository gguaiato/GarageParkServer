package controller;

import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.Person;

import javax.ws.rs.FormParam;

public class PersonController {

    private final PersonRepository personRepository = PersonRepository.instance();
    private static PersonController instance;

    private PersonController() {}

    public static PersonController instance() {
        if (instance == null) {
            instance = new PersonController();
        }
        return instance;
    }

    public boolean includeUser(String nusp, String name, String gender) {
        Person person = new Person(nusp, name, gender);
        return personRepository.insert(person);
    }

    public boolean updateValidPerson(String nusp, boolean valid) {
        Person person = personRepository.findByNusp(nusp);
        if (person != null) {
            person.setValid(valid);
            boolean updated = personRepository.update(person);
            if (updated) {
                return true;
            }
        }
        return false;
    }

    public Person getPerson(String nusp) {
        return personRepository.findByNusp(nusp);
    }
}
