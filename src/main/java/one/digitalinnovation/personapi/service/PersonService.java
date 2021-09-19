package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.response.MessageResponseDto;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public MessageResponseDto createPerson(Person person) {
        var personSaved = personRepository.save(person);
        return MessageResponseDto.builder()
                .message("Created person with ID: " + personSaved.getId())
                .build();
    }

}
