package omid.ro.civil_registration.services;

import lombok.RequiredArgsConstructor;
import omid.ro.civil_registration.dto.OutputMessage;
import omid.ro.civil_registration.entities.Person;
import omid.ro.civil_registration.repositories.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.Event;
import static omid.ro.civil_registration.config.CivilRegistrationStateMachine.State;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Person insertPerson(Person person) {
        return repository.save(person);
    }

    public Person getById(Long personId) {
        return repository.findById(personId).get();
    }



}
