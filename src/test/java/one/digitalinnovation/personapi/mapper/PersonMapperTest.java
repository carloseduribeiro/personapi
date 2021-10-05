package one.digitalinnovation.personapi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeDto;
import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PersonMapperTest {

    @Autowired
    private PersonMapper personMapper;

    @Test
    public void testGivenPersonDtoThenReturnPersonEntity() {
        var personDto = createFakeDto();
        var person = personMapper.toModel(personDto);

        assertEquals(personDto.getFirstName(), person.getFirstName());
        assertEquals(personDto.getLastName(), person.getLastName());
        assertEquals(personDto.getCpf(), person.getCpf());

        var phone = person.getPhones().get(0);
        var phoneDto = personDto.getPhones().get(0);

        assertEquals(phoneDto.getType(), phone.getType());
        assertEquals(phoneDto.getNumber(), phone.getNumber());
    }

    @Test
    public void testGivenPersonEntityThenReturnPersonDto() {
        var person = createFakeEntity();
        var personDto = personMapper.toDto(person);

        assertEquals(person.getFirstName(), personDto.getFirstName());
        assertEquals(person.getLastName(), personDto.getLastName());
        assertEquals(person.getCpf(), personDto.getCpf());

        var phone = person.getPhones().get(0);
        var phoneDto = personDto.getPhones().get(0);

        assertEquals(phone.getType(), phoneDto.getType());
        assertEquals(phone.getNumber(), phoneDto.getNumber());
    }
}
