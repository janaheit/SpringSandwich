package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.exception.SessionAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonJpaRepository;
import be.abis.sandwichordersystem.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessionServiceTest {

    @Autowired
    SessionService cut;

    @Autowired
    PersonJpaRepository personRepository;

    Session mockSession1;
    Session mockSession2;
    Student mockStudent1;
    Student mockStudent2;
    Student mockStudent3;
    Instructor mockInstructor1;
    Instructor mockInstructor2;

    @BeforeEach
    public void setUp() {
        mockStudent1 = new Student("Student", "One");
        mockStudent2 = new Student("Student2", "Two");
        mockStudent3 = new Student("Student3", "Three");
        mockInstructor1 = new Instructor("Instructor", "One");
        mockInstructor2 = new Instructor("Instructor2", "Two");

        mockSession1 = new Session(Course.UNIX_SHELL, mockInstructor1, LocalDate.now(), LocalDate.now());
        mockSession2 = new Session(Course.J2EE_WEBSPHERE, mockInstructor2, LocalDate.now(), LocalDate.now());


    }

    // findAllPersonsFollowingSession
    @Test
    @Transactional
    public void findAllPersonsFollowingSessionWorks() throws SessionAlreadyExistsException {
        personRepository.save(mockInstructor1);
        System.out.println("Person saved");
        Instructor myInstructor = personRepository.findInstructorByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName());
        System.out.println("Person retrieved " + myInstructor.getPersonNr());
        mockSession1.setInstructor(myInstructor);
        cut.addSession(mockSession1);
        Session mySession = cut.findDbSession(mockSession1);
        cut.setTeacherForSession(myInstructor.getPersonNr(), mySession.getSessionNumber());
        personRepository.save(mockStudent1);
        personRepository.save(mockStudent2);
        personRepository.save(mockStudent3);
        Student myStudent1 = personRepository.findStudentByName(mockStudent1.getFirstName()+mockStudent1.getLastName());
        Student myStudent2 = personRepository.findStudentByName(mockStudent2.getFirstName()+mockStudent2.getLastName());
        cut.enrolPersonForSession(myStudent1.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent2.getPersonNr(), mySession.getSessionNumber());

        List<Person> testOutput = cut.findAllPersonsFollowingSession(mySession.getSessionNumber());
        System.out.println(testOutput);
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || testOutput.contains(mockStudent3)) {
            succeed = false;
        }
        assertTrue(succeed);
    }
    // findAllPersonsFollowingSessionToday
    @Test
    @Transactional
    public void findAllPersonsFollowingSessionTodayWithTwoSessionsButOneTodayWorks() throws SessionNotFoundException, SessionAlreadyExistsException {
        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().minusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        Session mySession = cut.findDbSession(mockSession1);
        Session mySession2 = cut.findDbSession(mockSession2);
        cut.setTeacherForSession(mySession.getInstructor().getPersonNr(), mySession.getSessionNumber());
        cut.setTeacherForSession(mySession2.getInstructor().getPersonNr(), mySession2.getSessionNumber());
        personRepository.save(mockStudent1);
        personRepository.save(mockStudent2);
        personRepository.save(mockStudent3);
        Student myStudent1 = personRepository.findStudentByName(mockStudent1.getFirstName()+mockStudent1.getLastName());
        Student myStudent2 = personRepository.findStudentByName(mockStudent2.getFirstName()+mockStudent2.getLastName());
        Student myStudent3 = personRepository.findStudentByName(mockStudent3.getFirstName()+mockStudent3.getLastName());

        cut.enrolPersonForSession(myStudent1.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent2.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent3.getPersonNr(), mySession2.getSessionNumber());

        List<Person> testOutput = cut.findAllPersonsFollowingSessionToday();
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || testOutput.contains(mockStudent3) || testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    @Test
    @Transactional
    public void findAllPersonsFollowingSessionTodayWithTwoSessionsBothTodayWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        Session mySession = cut.findDbSession(mockSession1);
        Session mySession2 = cut.findDbSession(mockSession2);
        cut.setTeacherForSession(mySession.getInstructor().getPersonNr(), mySession.getSessionNumber());
        cut.setTeacherForSession(mySession2.getInstructor().getPersonNr(), mySession2.getSessionNumber());
        personRepository.save(mockStudent1);
        personRepository.save(mockStudent2);
        personRepository.save(mockStudent3);
        Student myStudent1 = personRepository.findStudentByName(mockStudent1.getFirstName()+mockStudent1.getLastName());
        Student myStudent2 = personRepository.findStudentByName(mockStudent2.getFirstName()+mockStudent2.getLastName());
        Student myStudent3 = personRepository.findStudentByName(mockStudent3.getFirstName()+mockStudent3.getLastName());

        cut.enrolPersonForSession(myStudent1.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent2.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent3.getPersonNr(), mySession2.getSessionNumber());


        List<Person> testOutput = cut.findAllPersonsFollowingSessionToday();
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || !testOutput.contains(mockStudent3) || !testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
    }

    // findAllPersonsFollowingSessionOnDate
    @Test
    @Transactional
    public void findAllPersonsFollowingOnDateWithOneThatDayWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.of(2021,9,28));
        mockSession2.setEndDate(LocalDate.of(2021,11,11));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        Session mySession = cut.findDbSession(mockSession1);
        Session mySession2 = cut.findDbSession(mockSession2);
        cut.setTeacherForSession(mySession.getInstructor().getPersonNr(), mySession.getSessionNumber());
        cut.setTeacherForSession(mySession2.getInstructor().getPersonNr(), mySession2.getSessionNumber());
        personRepository.save(mockStudent1);
        personRepository.save(mockStudent2);
        personRepository.save(mockStudent3);
        Student myStudent1 = personRepository.findStudentByName(mockStudent1.getFirstName()+mockStudent1.getLastName());
        Student myStudent2 = personRepository.findStudentByName(mockStudent2.getFirstName()+mockStudent2.getLastName());
        Student myStudent3 = personRepository.findStudentByName(mockStudent3.getFirstName()+mockStudent3.getLastName());

        cut.enrolPersonForSession(myStudent1.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent2.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent3.getPersonNr(), mySession2.getSessionNumber());

        List<Person> testOutput = cut.findAllPersonsFollowingSessionOnDate(LocalDate.now());
        System.out.println(testOutput.size());
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || testOutput.contains(mockStudent3) || testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
    }

    @Test
    @Transactional
    public void findAllPersonsFollowingSessionOnDateWithTwoSessionsWorks() throws SessionNotFoundException, SessionAlreadyExistsException {
        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        Session mySession = cut.findDbSession(mockSession1);
        Session mySession2 = cut.findDbSession(mockSession2);
        cut.setTeacherForSession(mySession.getInstructor().getPersonNr(), mySession.getSessionNumber());
        cut.setTeacherForSession(mySession2.getInstructor().getPersonNr(), mySession2.getSessionNumber());
        personRepository.save(mockStudent1);
        personRepository.save(mockStudent2);
        personRepository.save(mockStudent3);
        Student myStudent1 = personRepository.findStudentByName(mockStudent1.getFirstName()+mockStudent1.getLastName());
        Student myStudent2 = personRepository.findStudentByName(mockStudent2.getFirstName()+mockStudent2.getLastName());
        Student myStudent3 = personRepository.findStudentByName(mockStudent3.getFirstName()+mockStudent3.getLastName());

        cut.enrolPersonForSession(myStudent1.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent2.getPersonNr(), mySession.getSessionNumber());
        cut.enrolPersonForSession(myStudent3.getPersonNr(), mySession2.getSessionNumber());

        List<Person> testOutput = cut.findAllPersonsFollowingSessionOnDate(LocalDate.now());
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || !testOutput.contains(mockStudent3) || !testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);

    }

    // findSessionsToday
    @Test
    @Transactional
    public void findSessionsTodayWithOneSessionsTodayWorks() throws SessionNotFoundException, SessionAlreadyExistsException {
        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().minusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));

    }

    @Test
    @Transactional
    public void findSessionsTodayWithTwoSessionsTodayWorks() throws SessionNotFoundException, SessionAlreadyExistsException {
        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

    }

    // findSessionsOnDate
    @Test
    @Transactional
    public void findSessionsOnDateWithOneSessionsOnDateWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().minusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        List<Session> outputList = cut.findSessionsOnDate(LocalDate.now());
        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));

    }

    @Test
    @Transactional
    public void findSessionsOnDateWithTwoSessionsOnDateWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

    }

    // findSessionsByInstructor
    @Test
    @Transactional
    public void findSessionsByInstructorWithOneSessionWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        Instructor myInstructor = personRepository.findInstructorByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName());


        List<Session> outputList = cut.findSessionsByInstructor(myInstructor.getPersonNr());

        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));


    }

    @Test
    @Transactional
    public void findSessionsByInstructorWithTwoSessionsWorks() throws SessionNotFoundException, SessionAlreadyExistsException {

        mockSession1.setStartDate(LocalDate.now().minusDays(2));
        mockSession1.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setStartDate(LocalDate.now().minusDays(10));
        mockSession2.setEndDate(LocalDate.now().plusDays(2));
        mockSession2.setInstructor(mockInstructor1);
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);

        Instructor myInstructor = personRepository.findInstructorByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName());


        List<Session> outputList = cut.findSessionsByInstructor(myInstructor.getPersonNr());

        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findSessionById (Throws sessionnotfoundexception)

    @Test
    @Transactional
    public void findSessionByIdWorks() throws SessionNotFoundException {

        Session mySession = cut.findSessionByID(1);
        assertEquals(Course.JAVA_PROGRAMMING, mySession.getCourse());

    }

    @Test
    public void findSessionByIdThrowsException() {
        int sId = cut.getSessions().size()+178773;
        assertThrows(SessionNotFoundException.class, () -> cut.findSessionByID(sId));
    }

}
