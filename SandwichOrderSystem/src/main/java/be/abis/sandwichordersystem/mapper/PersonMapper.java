package be.abis.sandwichordersystem.mapper;

import be.abis.sandwichordersystem.dto.PersonDTO;
import be.abis.sandwichordersystem.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public static PersonDTO toDTO(Person p){
        return new PersonDTO(p.getPersonNr(), p.getFirstName() + " " + p.getLastName());
    }
}
