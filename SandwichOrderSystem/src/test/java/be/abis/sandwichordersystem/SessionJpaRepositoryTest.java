package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.PersonJpaRepository;
import be.abis.sandwichordersystem.repository.PersonRepository;
import be.abis.sandwichordersystem.repository.SessionJpaRepository;
import be.abis.sandwichordersystem.repository.SessionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@SpringBootTest
public class SessionJpaRepositoryTest {

    @Autowired
    private SessionJpaRepository sessionRepository;
    @Autowired private PersonJpaRepository personRepository;


    Instructor instructor;

    // Test session
    Session session;
    @BeforeEach
    public void setUp() {
        instructor = personRepository.getInstructors().get(0);
        System.out.println(instructor.getFirstName());
        session = new Session(Course.JAVA_ADVANCED, instructor, LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
    }

    @Test
    @Transactional
    public void checkSave() {
        Instructor myTeacher = new Instructor("Henk", "de Tank");
        Session mySession = new Session(Course.INTERNET_ENABLING, myTeacher, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2));
        sessionRepository.save(mySession);
    }

    @Test
    public void findSessionOne() {
        Session mySession = sessionRepository.findSessionByID(1);
        System.out.println(mySession.getCourse().getTitle());
        System.out.println(mySession.getInstructor().getFirstName());
        //System.out.println(mySession.getStudents());
    }

    @Test
    public void findSessionWithInstructorSandy(){
        Instructor sandy = personRepository.getInstructors().get(0);
        assertEquals(1, sessionRepository.findSessionsByInstructor(sandy.getPersonNr()).size());
    }

    @Test
    public void findSessionWithNonExistingInstructorGivesEmptyList(){
        assertEquals(0,sessionRepository.findSessionsByInstructor(257).size());
    }

    @Test
    public void findSessionsOf26_10_2022(){
        assertEquals(2,sessionRepository.findSessionsOnDate(LocalDate.of(2022,10,26)).size());
    }

    @Test
    // ? wtf is this test doing?
    public void findSessionsWithNonExistingDateReturnsEmptyList(){
        assertEquals(2,sessionRepository.findSessionsOnDate(LocalDate.now().plusDays(1)).size());
    }

    @Test
    public void findSessionWithID1() throws SessionNotFoundException {
        assertEquals(1, sessionRepository.findSessionByID(1).getSessionNumber());
    }



    @Test
    @Transactional
    public void addSessionWorks(){
        sessionRepository.save(session);
        assertTrue(sessionRepository.getSessions().contains(session));
    }

    @Test
    @Transactional
    public void deleteSessionWorks() {
        sessionRepository.save(session);
        sessionRepository.delete(session);
        assertFalse(sessionRepository.getSessions().contains(session));
    }

    @Test
    @Transactional
    public void findSessionsByPeriodStartDateInRange() throws SessionNotFoundException {
        session.setStartDate(LocalDate.now().plusDays(1));
        session.setEndDate(LocalDate.now().plusDays(10));
        sessionRepository.save(session);
        List<Session> assertList = sessionRepository.findSessionsByPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        assertTrue(assertList.contains(session));
    }

    @Test
    @Transactional
    public void findSessionByPeriodEndDateInRange() throws SessionNotFoundException {
        session.setStartDate(LocalDate.now().minusDays(10));
        session.setEndDate(LocalDate.now().plusDays(3));
        sessionRepository.save(session);
        List<Session> assertList = sessionRepository.findSessionsByPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        assertTrue(assertList.contains(session));
    }

    @Test
    @Transactional
    public void findSessionByPeriodBothDatesInRange() throws SessionNotFoundException {
        session.setStartDate(LocalDate.now().plusDays(1));
        session.setEndDate(LocalDate.now().plusDays(3));
        sessionRepository.save(session);
        List<Session> assertList = sessionRepository.findSessionsByPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        assertTrue(assertList.contains(session));
    }

    @Test
    @Transactional
    public void findSessionByPeriodBothDatesBeforePeriod() throws SessionNotFoundException {
        session.setStartDate(LocalDate.now().minusDays(20));
        session.setEndDate(LocalDate.now().minusDays(10));
        sessionRepository.save(session);
        List<Session> assertList = sessionRepository.findSessionsByPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        assertFalse(assertList.contains(session));
    }

    @Test
    @Transactional
    public void findSessionByPeriodBothDatesAfterPeriod() throws SessionNotFoundException {
        session.setStartDate(LocalDate.now().plusDays(10));
        session.setEndDate(LocalDate.now().plusDays(15));
        sessionRepository.save(session);
        List<Session> assertList = sessionRepository.findSessionsByPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        assertFalse(assertList.contains(session));
    }

}
