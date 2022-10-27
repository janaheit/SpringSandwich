package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.service.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessionServiceTest {

    @Autowired
    SessionService cut;

    @Mock
    Session mockSession1;
    @Mock
    Session mockSession2;
    @Mock
    Student mockStudent1;
    @Mock
    Student mockStudent2;
    @Mock
    Student mockStudent3;
    @Mock
    Instructor mockInstructor1;
    @Mock
    Instructor mockInstructor2;

    // findAllPersonsFollowingSession
    @Test
    public void findAllPersonsFollowingSessionWorks() {
        List<Student> students = new ArrayList<>();
        students.add(mockStudent1);
        students.add(mockStudent2);
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession1.getStudents()).thenReturn(students);
        List<Person> testOutput = cut.findAllPersonsFollowingSession(mockSession1);
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || testOutput.contains(mockStudent3)) {
            succeed = false;
        }
        assertTrue(succeed);
    }
    // findAllPersonsFollowingSessionToday
    @Test
    public void findAllPersonsFollowingSessionTodayWithTwoSessionsButOneTodayWorks() throws SessionNotFoundException {
        List<Student> studentsSes1 = new ArrayList<>();
        List<Student> studentsSes2 = new ArrayList<>();
        studentsSes1.add(mockStudent1);
        studentsSes1.add(mockStudent2);
        studentsSes2.add(mockStudent3);
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession1.getStudents()).thenReturn(studentsSes1);
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(2));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(mockSession2.getInstructor()).thenReturn(mockInstructor2);
        when(mockSession2.getStudents()).thenReturn(studentsSes2);
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().minusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
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
    public void findAllPersonsFollowingSessionTodayWithTwoSessionsBothTodayWorks() throws SessionNotFoundException {
        List<Student> studentsSes1 = new ArrayList<>();
        List<Student> studentsSes2 = new ArrayList<>();
        studentsSes1.add(mockStudent1);
        studentsSes1.add(mockStudent2);
        studentsSes2.add(mockStudent3);
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession1.getStudents()).thenReturn(studentsSes1);
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(2));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(mockSession2.getInstructor()).thenReturn(mockInstructor2);
        when(mockSession2.getStudents()).thenReturn(studentsSes2);
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Person> testOutput = cut.findAllPersonsFollowingSessionToday();
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || !testOutput.contains(mockStudent3) || !testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findAllPersonsFollowingSessionOnDate
    @Test
    public void findAllPersonsFollowingOnDateWithOneThatDayWorks() throws SessionNotFoundException {
        List<Student> studentsSes1 = new ArrayList<>();
        List<Student> studentsSes2 = new ArrayList<>();
        studentsSes1.add(mockStudent1);
        studentsSes1.add(mockStudent2);
        studentsSes2.add(mockStudent3);
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession1.getStudents()).thenReturn(studentsSes1);
        when(mockSession1.getStartDate()).thenReturn(LocalDate.of(2022,9,28));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.of(2022,11,11));
        when(mockSession2.getInstructor()).thenReturn(mockInstructor2);
        when(mockSession2.getStudents()).thenReturn(studentsSes2);
        when(mockSession2.getStartDate()).thenReturn(LocalDate.of(2021,9,28));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.of(2021,11,11));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Person> testOutput = cut.findAllPersonsFollowingSessionOnDate(LocalDate.of(2022,10,1));
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || testOutput.contains(mockStudent3) || testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    @Test
    public void findAllPersonsFollowingSessionOnDateWithTwoSessionsWorks() throws SessionNotFoundException {
        List<Student> studentsSes1 = new ArrayList<>();
        List<Student> studentsSes2 = new ArrayList<>();
        studentsSes1.add(mockStudent1);
        studentsSes1.add(mockStudent2);
        studentsSes2.add(mockStudent3);
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession1.getStudents()).thenReturn(studentsSes1);
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(2));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(mockSession2.getInstructor()).thenReturn(mockInstructor2);
        when(mockSession2.getStudents()).thenReturn(studentsSes2);
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Person> testOutput = cut.findAllPersonsFollowingSessionOnDate(LocalDate.now());
        Boolean succeed = true;
        if(!testOutput.contains(mockInstructor1) || !testOutput.contains(mockStudent1) || !testOutput.contains(mockStudent2) || !testOutput.contains(mockStudent3) || !testOutput.contains(mockInstructor2)) {
            succeed = false;
        }
        assertTrue(succeed);
        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findSessionsToday
    @Test
    public void findSessionsTodayWithOneSessionsTodayWorks() throws SessionNotFoundException {
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(1));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().minusDays(1));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    @Test
    public void findSessionsTodayWithTwoSessionsTodayWorks() throws SessionNotFoundException {
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(1));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findSessionsOnDate
    @Test
    public void findSessionsOnDateWithOneSessionsOnDateWorks() throws SessionNotFoundException {
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(1));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().minusDays(1));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsOnDate(LocalDate.now());
        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    @Test
    public void findSessionsOnDateWithTwoSessionsOnDateWorks() throws SessionNotFoundException {
        when(mockSession1.getStartDate()).thenReturn(LocalDate.now().minusDays(1));
        when(mockSession1.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(mockSession2.getStartDate()).thenReturn(LocalDate.now().minusDays(10));
        when(mockSession2.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsToday();
        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findSessionsByInstructor
    @Test
    public void findSessionsByInstructorWithOneSessionWorks() throws SessionNotFoundException {
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession2.getInstructor()).thenReturn(mockInstructor2);
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsByInstructor(mockInstructor1);

        assertTrue(outputList.contains(mockSession1) && !outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    @Test
    public void findSessionsByInstructorWithTwoSessionsWorks() throws SessionNotFoundException {
        when(mockSession1.getInstructor()).thenReturn(mockInstructor1);
        when(mockSession2.getInstructor()).thenReturn(mockInstructor1);
        cut.addSession(mockSession1);
        cut.addSession(mockSession2);
        List<Session> outputList = cut.findSessionsByInstructor(mockInstructor1);

        assertTrue(outputList.contains(mockSession1) && outputList.contains(mockSession2));

        cut.deleteSession(mockSession1);
        cut.deleteSession(mockSession2);
    }

    // findSessionById (Throws sessionnotfoundexception)

    @Test
    public void findSessionByIdWorks() throws SessionNotFoundException {
        int sId = cut.getSessions().size()+123;
        when(mockSession1.getSessionNumber()).thenReturn(sId);
        cut.addSession(mockSession1);

        assertEquals(mockSession1, cut.findSessionByID(sId));

        cut.deleteSession(mockSession1);
    }

    @Test
    public void findSessionByIdThrowsException() {
        int sId = cut.getSessions().size()+178773;
        assertThrows(SessionNotFoundException.class, () -> cut.findSessionByID(sId));
    }

}
