package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.OperationNotAllowedException;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    PersonService cut;


    Person mockPerson1;

    Instructor mockInstructor1;

    Student mockStudent1;

    Admin mockAdmin1;

    @BeforeEach
    public void setUp() {
        mockPerson1 = new Person("Person", "One");
        mockInstructor1 = new Instructor("Instructor", "One");
        mockStudent1 = new Student("Student", "One");
        mockAdmin1 = new Admin("Admin", "One");
    }

    @Test
    @Transactional
    public void findPersonByNameWorks() throws PersonNotFoundException, OperationNotAllowedException {
        assertEquals("van Hassel", cut.findPersonByName("marcel").getLastName());
    }

    @Test
    public void findPersonByNameThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.findPersonByName(" jkadgsafsffhjdgsfhjgsadfuyewgrfjdhs"));
    }

    @Test
    public void findInstructorByNameWorks() throws PersonNotFoundException, OperationNotAllowedException {
        assertEquals("Schillebeeckx", cut.findInstructorByName("sandy").getLastName());
    }

    @Test
    @Transactional
    public void findInstructorByNameThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        assertThrows(PersonNotFoundException.class, () -> cut.findInstructorByName("Studentone"));
        cut.deletePerson(mockStudent1);
    }

    @Test
    public void findStudentByNameWorks() throws PersonNotFoundException, OperationNotAllowedException {
        assertEquals("van Hassel", cut.findStudentByName("Marcel").getLastName());
    }

    @Test
    @Transactional
    public void findStudentByNameThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockInstructor1);
        assertThrows(PersonNotFoundException.class, () -> cut.findStudentByName("InstructorOne"));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    public void findAdminByNameWorks() throws PersonNotFoundException, OperationNotAllowedException {
        assertEquals("Admin", cut.findAdminByName("emily").getLastName());
    }

    @Test
    @Transactional
    public void findAdminByNameThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        assertThrows(PersonNotFoundException.class, () -> cut.findAdminByName("sandy"));
    }

    @Test
    @Transactional
    public void findPersonByIdWorksTest() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockInstructor1);
        int pId = cut.findPersonByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName()).getPersonNr();
        assertEquals(mockInstructor1, cut.findPersonByID(pId));
    }

    @Test
    public void findPersonByIdThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.findPersonByID(cut.getPersons().size()+100));
    }

    @Test
    @Transactional
    public void findInstructorByIdWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockInstructor1);
        int pId = cut.findPersonByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName()).getPersonNr();
        assertEquals(mockInstructor1, cut.findInstructorByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    @Transactional
    public void findInstructorByIdThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        int pId = cut.findPersonByName(mockStudent1.getFirstName()+mockStudent1.getLastName()).getPersonNr();
        assertThrows(PersonNotFoundException.class, () -> cut.findInstructorByID(pId));
        cut.deletePerson(mockStudent1);
    }

    @Test
    @Transactional
    public void findStudentByIdWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        int pId = cut.findPersonByName(mockStudent1.getFirstName()+mockStudent1.getLastName()).getPersonNr();
        assertEquals(mockStudent1, cut.findStudentByID(pId));
        cut.deletePerson(mockStudent1);
    }

    @Test
    @Transactional
    public void findStudentByIdThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockInstructor1);
        int pId = cut.findPersonByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName()).getPersonNr();
        assertThrows(PersonNotFoundException.class, () ->  cut.findStudentByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    @Transactional
    public void findAdminByIdWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockAdmin1);
        int pId = cut.findPersonByName(mockAdmin1.getFirstName()+mockAdmin1.getLastName()).getPersonNr();
        assertEquals(mockAdmin1, cut.findAdminByID(pId));
        cut.deletePerson(mockAdmin1);
    }

    @Test
    @Transactional
    public void findAdminByIdThrowsException() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockInstructor1);
        int pId = cut.findPersonByName(mockInstructor1.getFirstName()+mockInstructor1.getLastName()).getPersonNr();
        assertThrows(PersonNotFoundException.class, () -> cut.findAdminByID(pId));
        cut.deletePerson(mockInstructor1);
    }

    @Test
    @Transactional
    public void getPersonsWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        assertTrue(cut.getPersons().contains(mockStudent1));
        cut.deletePerson(mockStudent1);
    }

    @Test
    @Transactional
    public void getInstructorsWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockInstructor1);
        assertTrue(cut.getInstructors().contains(mockInstructor1) && !cut.getInstructors().contains(mockStudent1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockInstructor1);
    }

    @Test
    @Transactional
    public void getStudentsWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockInstructor1);
        assertTrue(cut.getStudents().contains(mockStudent1) && !cut.getStudents().contains(mockInstructor1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockInstructor1);
    }

    @Test
    @Transactional
    public void getAdminsWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        cut.addPerson(mockAdmin1);
        assertTrue(cut.getAdmins().contains(mockAdmin1) && !cut.getAdmins().contains(mockStudent1));
        cut.deletePerson(mockStudent1);
        cut.deletePerson(mockAdmin1);
    }

    @Test
    @Transactional
    public void addPersonWorks() throws PersonNotFoundException, OperationNotAllowedException {
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.addPerson(mockStudent1);
        int amountOfPersonsAftersTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest +1, amountOfPersonsAftersTest);
        cut.deletePerson(mockStudent1);
    }

    @Test
    public void deletePersonWorks() throws PersonNotFoundException, OperationNotAllowedException {
        cut.addPerson(mockStudent1);
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.deletePerson(mockStudent1);
        int amountOfPersonsAfterTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest -1, amountOfPersonsAfterTest);
    }

    @Test
    @Transactional
    public void deletePersonThrowsException() {
        assertThrows(PersonNotFoundException.class, () -> cut.deletePerson(mockPerson1));
    }

}
