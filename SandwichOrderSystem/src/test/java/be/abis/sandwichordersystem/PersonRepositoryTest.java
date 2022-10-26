package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository cut;

    // Mocks
    @Mock
    Person person1;
    @Mock
    Person person2;
    @Mock
    Admin mockAdmin;
    @Mock
    Student mockStudent;
    @Mock
    Instructor mockInstructor;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void addPersonWorks() throws PersonNotFoundException {
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.addPerson(person1);
        int amountOfPersonsAfterTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest + 1, amountOfPersonsAfterTest);
        cut.deletePerson(person1);
    }

    @Test
    public void deletePersonWorks() throws PersonNotFoundException {
        cut.addPerson(person1);
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.deletePerson(person1);
        assertEquals(amountOfPersonsBeforeTest -1, cut.getPersons().size());
    }

    @Test
    public void deletePersonThatDoesntExistThrowsException() throws PersonNotFoundException {
        cut.addPerson(person1);
        assertThrows(PersonNotFoundException.class, () -> cut.deletePerson(person2));
        cut.deletePerson(person1);
    }

    @Test
    public void getAdminsTest() throws PersonNotFoundException {
        boolean test = false;
        cut.addPerson(mockAdmin);
        cut.addPerson(mockInstructor);
        if(cut.getAdmins().contains(mockAdmin) && !cut.getAdmins().contains(mockInstructor)) {
            test = true;
        }
        assertTrue(test);
        cut.deletePerson(mockAdmin);
        cut.deletePerson(mockInstructor);
    }

    @Test
    public void getInstructorsTest() throws PersonNotFoundException {
        boolean test = false;
        cut.addPerson(mockInstructor);
        cut.addPerson(mockAdmin);
        if(cut.getInstructors().contains(mockInstructor) && !cut.getInstructors().contains(mockAdmin)) {
            test = true;
        }
        assertTrue(test);
        cut.deletePerson(mockAdmin);
        cut.deletePerson(mockInstructor);
    }

    @Test
    public void getStudentsTest() throws PersonNotFoundException {
        boolean test = false;
        cut.addPerson(mockStudent);
        cut.addPerson(mockAdmin);
        if(cut.getStudents().contains(mockStudent) && !cut.getStudents().contains(mockAdmin)) {
            test = true;
        }
        assertTrue(test);
        cut.deletePerson(mockAdmin);
        cut.deletePerson(mockStudent);
    }

    @Test
    public void findPersonByNameWorks() throws PersonNotFoundException {
        when(person1.getFirstName()).thenReturn("Testing");
        when(person1.getLastName()).thenReturn("Testperson");
        cut.addPerson(person1);
        Person test = cut.findPersonByName("Testing Testperson");
        assertEquals(person1, test);
        cut.deletePerson(person1);
    }

    @Test
    public void findPersonTByNameThatNotExistsThrowsException() throws PersonNotFoundException {
        when(person1.getFirstName()).thenReturn("Testing");
        when(person1.getLastName()).thenReturn("Testperson");
        cut.addPerson(person1);
        assertThrows(PersonNotFoundException.class, () -> cut.findPersonByName("testietesttesttesttestiejajajalalala jazeker"));
        cut.deletePerson(person1);
    }

    //TODO still have to test findAdmin/Student/Instructor by name methods

}
