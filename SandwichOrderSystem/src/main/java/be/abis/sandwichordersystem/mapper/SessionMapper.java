package be.abis.sandwichordersystem.mapper;

import be.abis.sandwichordersystem.dto.SessionDTO;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public static SessionDTO toDTO(Session s){

        return new SessionDTO(s.getSessionNumber(), s.getCourse().getTitle(), s.getInstructor().getPersonNr(),
                s.getInstructor().getFirstName()+ " "+s.getInstructor().getLastName(), s.getStartDate(), s.getEndDate());
    }
}
