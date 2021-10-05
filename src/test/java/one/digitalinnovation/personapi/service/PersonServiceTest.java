package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.request.PersonDto;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeDto;
import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonMapper personMapper;

    @Test
    public void testGivenPersonDtoThenReturnSuccessSavedMessage() {
        var personDto = createFakeDto();
        var expectedSavedPerson = createFakeEntity();

        when(personMapper.toModel(personDto)).thenReturn(expectedSavedPerson);
        when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        var messageResponseDto = personService.createPerson(personDto);

        assertEquals("Created person with ID: 1", messageResponseDto.getMessage());

    }


    @Test
    public void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
        var expectedPersonDto = createFakeDto();
        var expectedSavedPerson = createFakeEntity();
        expectedPersonDto.setId(expectedSavedPerson.getId());

        when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
        when(personMapper.toDto(expectedSavedPerson)).thenReturn(expectedPersonDto);

        PersonDto personDto = personService.findById(expectedSavedPerson.getId());

        assertEquals(expectedPersonDto, personDto);

        assertEquals(expectedSavedPerson.getId(), personDto.getId());
        assertEquals(expectedPersonDto.getFirstName(), personDto.getFirstName());

    }

    @Test
    public void testGivenInvalidPersonIdThenThrowExcpetion() {
        var invalidPersonId = 1L;
        when(personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidPersonId));
    }

    @Test
    public void testGivenNoDataThenReturnAllPeopleRegistered() {
        var expectedRegisteredPeopleList = Collections.singletonList(createFakeEntity());
        var personDto = createFakeDto();

        when(personRepository.findAll()).thenReturn(expectedRegisteredPeopleList);
        when(personMapper.toDto(any(Person.class))).thenReturn(personDto);

        var expectedPeopleDtoList = personService.listAll();

        assertFalse(expectedPeopleDtoList.isEmpty());
        assertEquals(expectedPeopleDtoList.get(0).getId(), personDto.getId());
    }

    @Test
    public void testGivenValidPersonIdAndUpdateInfoThenReturnSuccessOnUpdate() throws PersonNotFoundException {
        var updatePersonId = 2L;

        var updatePersonDto = createFakeDto();
        updatePersonDto.setId(updatePersonId);
        updatePersonDto.setLastName("Eduardo Ribeiro");

        var expectedUpdatedPerson = createFakeEntity();
        expectedUpdatedPerson.setId(updatePersonId);
        expectedUpdatedPerson.setLastName("Eduardo Ribeiro");

        when(personRepository.findById(updatePersonId)).thenReturn(Optional.of(expectedUpdatedPerson));
        when(personMapper.toModel(updatePersonDto)).thenReturn(expectedUpdatedPerson);
        when(personRepository.save(any(Person.class))).thenReturn(expectedUpdatedPerson);

        var messageResponseDto = personService.updateById(updatePersonId, updatePersonDto);

        assertEquals("Updated person with ID: 2", messageResponseDto.getMessage());
    }

    @Test
    public void testGivenInvalidPersonIdAndUpdateInfoThenThrowExceptionOnUpdate() throws PersonNotFoundException {
        var invalidPersonId = 1L;

        var updatePersonDto = createFakeDto();
        updatePersonDto.setId(invalidPersonId);
        updatePersonDto.setLastName("Eduardo Ribeiro");

        when(personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.updateById(invalidPersonId, updatePersonDto));
    }

    @Test
    public void testGivenValidPersonIdThenReturnSuccessOnDelete() throws PersonNotFoundException {
        var deletePersonId = 1L;
        var expectedDeletedPerson = createFakeEntity();

        when(personRepository.findById(deletePersonId)).thenReturn(Optional.of(expectedDeletedPerson));
        personService.deleteById(deletePersonId);

        verify(personRepository, times(1)).deleteById(deletePersonId);
    }

    @Test
    public void testGivenInvalidPersonIdThenReturnSuccessOnDelete() throws PersonNotFoundException {
        var invalidPersonId = 1L;

        when(personRepository.findById(invalidPersonId)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(invalidPersonId));
    }
}
