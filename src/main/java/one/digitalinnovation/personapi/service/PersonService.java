package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.request.PersonDto;
import one.digitalinnovation.personapi.dto.response.MessageResponseDto;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public MessageResponseDto createPerson(PersonDto personDto) {
        Person personToSave = personMapper.toModel(personDto);
        Person personSaved = personRepository.save(personToSave);

        return MessageResponseDto.builder()
                .message("Created person with ID: " + personSaved.getId())
                .build();
    }

    public List<PersonDto> listAll() {
        var allPersons = personRepository.findAll();
        // TODO - Aprimorar.
        return allPersons.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto findById(Long id) throws PersonNotFoundException {
        var person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        return personMapper.toDto(person);
    }

}
