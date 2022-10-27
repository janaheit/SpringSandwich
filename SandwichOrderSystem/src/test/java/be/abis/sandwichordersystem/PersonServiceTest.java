package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    PersonService cut;

    @Mock
    Person mockPerson1;

    @Mock
    Instructor mockInstructor1;

    @Mock
    Student mockStudent1;

    @Mock
    Admin mockAdmin1;

    @Test
    public void findPersonByNameWorks() throws PersonNotFoundException {
        when(mockPerson1.getFirstName()).thenReturn("TestPerson");
        when(mockPerson1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockPerson1);
        assertEquals(mockPerson1, cut.findPersonByName("TestPerson Achternaam"));
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void findPersonByNameThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.findPersonByName(" jkadgsafsffhjdgsfhjgsadfuyewgrfjdhs"));
    }

    @Test
    public void findInstructorByNameWorks() throws PersonNotFoundException {
        when(mockInstructor1.getFirstName()).thenReturn("TestPerson");
        when(mockInstructor1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockInstructor1);
        assertEquals(mockInstructor1, cut.findInstructorByName("TestPerson Achternaam"));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void findInstructorByNameThrowsException() throws PersonNotFoundException {
        when(mockPerson1.getFirstName()).thenReturn("TestPerson");
        when(mockPerson1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockPerson1);
        assertThrows(PersonNotFoundException.class, () -> cut.findInstructorByName("TestPerson Achternaam"));
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void findStudentByNameWorks() throws PersonNotFoundException {
        when(mockStudent1.getFirstName()).thenReturn("TestPerson");
        when(mockStudent1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockStudent1);
        assertEquals(mockStudent1, cut.findStudentByName("TestPerson Achternaam"));
        cut.deletePerson(mockStudent1);
    }

    @Test
    public void findStudentByNameThrowsException() throws PersonNotFoundException {
        when(mockPerson1.getFirstName()).thenReturn("TestPerson");
        when(mockPerson1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockPerson1);
        assertThrows(PersonNotFoundException.class, () -> cut.findStudentByName("TestPerson Achternaam"));
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void findAdminByNameWorks() throws PersonNotFoundException {
        when(mockAdmin1.getFirstName()).thenReturn("TestPerson");
        when(mockAdmin1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockAdmin1);
        assertEquals(mockAdmin1, cut.findAdminByName("TestPerson Achternaam"));
        cut.deletePerson(mockAdmin1);
    }

    @Test
    public void findAdminByNameThrowsException() throws PersonNotFoundException {
        when(mockPerson1.getFirstName()).thenReturn("TestPerson");
        when(mockPerson1.getLastName()).thenReturn("Achternaam");
        cut.addPerson(mockPerson1);
        assertThrows(PersonNotFoundException.class, () -> cut.findAdminByName("TestPerson Achternaam"));
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void findPersonByIdWorksTest() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockInstructor1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockInstructor1);
        assertEquals(mockInstructor1, cut.findPersonByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void findPersonByIdThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.findPersonByID(cut.getPersons().size()+100));
    }

    @Test
    public void findInstructorByIdWorks() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockInstructor1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockInstructor1);
        assertEquals(mockInstructor1, cut.findInstructorByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void findInstructorByIdThrowsException() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockStudent1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockStudent1);
        assertThrows(PersonNotFoundException.class, () -> cut.findInstructorByID(pId));
        cut.deletePerson(mockStudent1);
    }

    @Test
    public void findStudentByIdWorks() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockStudent1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockStudent1);
        assertEquals(mockStudent1, cut.findStudentByID(pId));
        cut.deletePerson(mockStudent1);
    }

    @Test
    public void findStudentByIdThrowsException() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockInstructor1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockInstructor1);
        assertThrows(PersonNotFoundException.class, () ->  cut.findStudentByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void findAdminByIdWorks() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockAdmin1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockAdmin1);
        assertEquals(mockAdmin1, cut.findAdminByID(pId));
        cut.deletePerson(mockAdmin1);
    }

    @Test
    public void findAdminByIdThrowsException() throws PersonNotFoundException {
        int pId = cut.getPersons().size()+10;
        when(mockInstructor1.getPersonNr()).thenReturn(pId);
        cut.addPerson(mockInstructor1);
        assertThrows(PersonNotFoundException.class, () -> cut.findAdminByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void getPersonsWorks() throws PersonNotFoundException {
        cut.addPerson(mockPerson1);
        assertTrue(cut.getPersons().contains(mockPerson1));
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void getInstructorsWorks() throws PersonNotFoundException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockInstructor1);
        assertTrue(cut.getInstructors().contains(mockInstructor1) && !cut.getInstructors().contains(mockStudent1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void getStudentsWorks() throws PersonNotFoundException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockInstructor1);
        assertTrue(cut.getStudents().contains(mockStudent1) && !cut.getStudents().contains(mockInstructor1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void getAdminsWorks() throws PersonNotFoundException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockAdmin1);
        assertTrue(cut.getAdmins().contains(mockAdmin1) && !cut.getAdmins().contains(mockStudent1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockAdmin1);
    }

    @Test
    public void addPersonWorks() throws PersonNotFoundException {
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.addPerson(mockPerson1);
        int amountOfPersonsAftersTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest +1, amountOfPersonsAftersTest);
        cut.deletePerson(mockPerson1);
    }

    @Test
    public void deletePersonWorks() throws PersonNotFoundException {
        cut.addPerson(mockPerson1);
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.deletePerson(mockPerson1);
        int amountOfPersonsAfterTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest -1, amountOfPersonsAfterTest);
    }

    @Test
    public void deletePersonThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.deletePerson(mockPerson1));
    }





}
