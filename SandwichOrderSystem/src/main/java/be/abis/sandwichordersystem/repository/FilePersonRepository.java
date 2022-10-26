package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.factory.PersonFactory;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FilePersonRepository implements PersonRepository {

    @Autowired private PersonFactory personFactory;
    private String filename = "/temp/javacourses/sandwichpersons.txt";

    List<Person> persons = new ArrayList<>();
    //private Logger log = LogManager.getLogger("sandwichLogger");

    public FilePersonRepository() {

    }

    @PostConstruct
    public void init(){
        persons = personFactory.createPersons();
    }

    // BUSINESS METHODS


    // crud methods
    @Override
    public void addPerson(Person person){
        // persons.add(person);
        // atm, add person is not implemented, bc we have no write to File of person.. not important rn!
    }

    @Override
    public void deletePerson(Person person) throws PersonNotFoundException {
        // atm, delete person is not implemented, bc we have no write to File of person.. not important rn!
    }

    // find methods
    @Override
    public Person findPersonByName(String name) {
        return null;
    }

    @Override
    public Instructor findInstructorByName(String name) throws PersonNotFoundException {
        return (Instructor)persons.stream()
                .filter(person -> person instanceof Instructor
                        && name.equals(person.getFirstName()+" "+person.getLastName()))
                .findAny()
                .orElseThrow(() -> {
                    //log.error("FilePersonRepository (findInstructorByName), instructor does not exist " + name);
                    return new PersonNotFoundException("This instructor does not exist.");
                });
    }

    @Override
    public Student findStudentByName(String name) throws PersonNotFoundException {
        return (Student)persons.stream()
                .filter(person -> person instanceof Student
                        && name.equals(person.getFirstName()+" "+person.getLastName()))
                .findAny()
                .orElseThrow(() -> {
                    //log.error("FilePersonRepository (findStudentByName) student does not exist " + name);
                    return new PersonNotFoundException("This student does not exist.");
                });
    }

    @Override
    public Admin findAdminByName(String name) throws PersonNotFoundException {
        return (Admin)persons.stream()
                .filter(person -> person instanceof Admin
                        && name.equals(person.getFirstName()+" "+person.getLastName()))
                .findAny()
                .orElseThrow(() -> {
                    //log.error("FilePersonRepository (findAdminByName) admin does not exist " + name);
                    return new PersonNotFoundException("This Admin does not exist.");
                });
    }

    // FILE IO stuff

    // add to file
    // tbd.

    // read from file
    // tbd.


    // GETTERS AND SETTERS

    @Override
    public List<Person> getPersons() {
        return this.persons;
    }

    @Override
    public List<Admin> getAdmins() {
        return this.persons.stream()
                .filter(p -> p instanceof Admin)
                .map(p -> (Admin)p)
                .collect(Collectors.toList());
    }

    @Override
    public List<Instructor> getInstructors() {
        return this.persons.stream()
                .filter(p -> p instanceof Instructor)
                .map(p -> (Instructor)p)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getStudents() {
        return this.persons.stream()
                .filter(p -> p instanceof Student)
                .map(p -> (Student)p)
                .collect(Collectors.toList());
    }
}
