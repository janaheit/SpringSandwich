package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.PersonRepository;
import be.abis.sandwichordersystem.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionRepositoryTest {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private PersonRepository personRepository;

    @Mock Instructor instructor;
    @Mock Session session;

    @Test
    public void findSessionWithInstructorSandy(){
        Instructor sandy = personRepository.getInstructors().get(0);
        assertEquals(1, sessionRepository.findSessionsByInstructor(sandy).size());
    }

    @Test
    public void findSessionWithNonExistingInstructorGivesEmptyList(){
        assertEquals(0,sessionRepository.findSessionsByInstructor(instructor).size());
    }

    @Test
    public void findSessionsOf26_10_2022(){
        assertEquals(2,sessionRepository.findSessionsOnDate(LocalDate.of(2022,10,26)).size());
    }

    @Test
    public void findSessionsWithNonExistingDateReturnsEmptyList(){
        assertEquals(2,sessionRepository.findSessionsOnDate(LocalDate.now().plusDays(1)).size());
    }

    @Test
    public void findSessionWithID1() throws SessionNotFoundException {
        assertEquals(1, sessionRepository.findSessionByID(1).getSessionNumber());
    }

    @Test
    public void findSessionWithNonExistingIDThrowsException() {
        assertThrows(SessionNotFoundException.class, () -> sessionRepository.findSessionByID(5791));
    }

    @Test
    public void findSessionWithNegativeIDThrowsException() {
        assertThrows(SessionNotFoundException.class, () -> sessionRepository.findSessionByID(-5791));
    }

    @Test
    public void addSessionWorks(){
        sessionRepository.addSession(session);
        assertTrue(sessionRepository.getSessions().contains(session));
        sessionRepository.getSessions().remove(session);
    }

    @Test
    public void deleteSessionWorks() throws SessionNotFoundException {
        sessionRepository.getSessions().add(session);
        sessionRepository.deleteSession(session);
        assertFalse(sessionRepository.getSessions().contains(session));
    }

}
