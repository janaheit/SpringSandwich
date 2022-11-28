package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonJpaRepository;
import be.abis.sandwichordersystem.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonJpaRepositoryTest {

    @Autowired
    PersonJpaRepository cut;

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

    //@Test
    /*
    public void addPersonWorks() throws PersonNotFoundException {
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.addPerson(person1);
        int amountOfPersonsAfterTest = cut.getPersons().size();
        assertEquals(amountOfPersonsBeforeTest + 1, amountOfPersonsAfterTest);
        cut.deletePerson(person1);
    }

    //@Test
    public void deletePersonWorks() throws PersonNotFoundException {
        cut.addPerson(person1);
        int amountOfPersonsBeforeTest = cut.getPersons().size();
        cut.deletePerson(person1);
        assertEquals(amountOfPersonsBeforeTest -1, cut.getPersons().size());
    }

    //@Test
    public void deletePersonThatDoesntExistThrowsException() throws PersonNotFoundException {
        cut.addPerson(person1);
        assertThrows(PersonNotFoundException.class, () -> cut.deletePerson(person2));
        cut.deletePerson(person1);
    }

     */

    @Test
    public void firstTest() {
        System.out.println(cut.getPersons());
    }

    @Test
    @Transactional
    public void getAdminsTest() throws PersonNotFoundException {
        boolean test = false;
        Person myAdmin = cut.findPersonByName("Emily");
        Person myInstructor = cut.findPersonByName("Sandy");

        List<Admin> adminList = cut.getAdmins();
        //System.out.println(adminList);

        if(adminList.contains(myAdmin) && !adminList.contains(myInstructor)) {
            test = true;
        }
        assertTrue(test);
    }

    @Test
    public void getInstructorsTest() throws PersonNotFoundException {
        boolean test = false;
        Person myAdmin = cut.findPersonByName("Emily");
        Person myInstructor = cut.findPersonByName("Sandy");

        if(cut.getInstructors().contains(myInstructor) && !cut.getInstructors().contains(myAdmin)) {
            test = true;
        }
        assertTrue(test);
    }

    @Test
    @Transactional
    public void getStudentsTest() throws PersonNotFoundException {
        boolean test = false;
        Person myAdmin = cut.findPersonByName("Emily");
        Person myStudent = cut.findPersonByName("Jana");

        if(cut.getStudents().contains(myStudent) && !cut.getStudents().contains(myAdmin)) {
            test = true;
        }
        assertTrue(test);
    }
/*
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

    @Test
    public void findPersonByNameWithRealData() throws PersonNotFoundException {
        Person myFirstPerson = cut.getPersons().get(0);
        StringBuilder fullName = new StringBuilder();
        fullName.append(myFirstPerson.getFirstName()).append(" ").append(myFirstPerson.getLastName());
        Person mySecondPerson = cut.findPersonByName(fullName.toString());
        assertEquals(myFirstPerson, mySecondPerson);
    }

 */

    //TODO still have to test findAdmin/Student/Instructor by name methods

}
