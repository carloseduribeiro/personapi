package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDto;
import one.digitalinnovation.personapi.dto.response.MessageResponseDto;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public MessageResponseDto createPerson(PersonDto personDto) {
        var personToSave = personMapper.toModel(personDto);
        var savedPerson = personRepository.save(personToSave);

        return createMessageResponse(savedPerson.getId(), "Created person with ID: ");
    }

    public List<PersonDto> listAll() {
        var personList = personRepository.findAll();
        return personList.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto findById(Long id) throws PersonNotFoundException {
        var person = personExists(id);
        return personMapper.toDto(person);
    }

    public void deleteById(Long id) throws PersonNotFoundException {
        personExists(id);
        personRepository.deleteById(id);
    }

    public MessageResponseDto updateById(Long id, PersonDto personDto) throws PersonNotFoundException {
        personExists(id);

        var personToUpdate = personMapper.toModel(personDto);
        var updatedPerson = personRepository.save(personToUpdate);

        return createMessageResponse(updatedPerson.getId(), "Updated person with ID: ");
    }

    private MessageResponseDto createMessageResponse(Long id, String s) {
        return MessageResponseDto.builder()
                .message(s + id)
                .build();
    }

    private Person personExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

}
