package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.factory.PersonFactory;
import be.abis.sandwichordersystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ListPersonRepository implements PersonRepository {

    @Autowired private PersonFactory personFactory;
    private List<Person> persons = new ArrayList<>();


    public ListPersonRepository() {
    }

    @PostConstruct
    public void init(){
        persons = personFactory.createPersons();
    }

    // find methods
    @Override
    public Person findPersonByName(String name) throws PersonNotFoundException {
        return persons.stream()
                .filter(person -> name.equalsIgnoreCase(person.getFirstName()+" "+person.getLastName()))
                .findAny()
                .orElseThrow(() -> {
                    //log.error("FilePersonRepository (findInstructorByName), instructor does not exist " + name);
                    return new PersonNotFoundException("This person does not exist");
                });

    }

    @Override
    public Instructor findInstructorByName(String name) throws PersonNotFoundException {
        return (Instructor)persons.stream()
                .filter(person -> person instanceof Instructor
                       // && name.equals(person.getFirstName()+" "+person.getLastName())
                        && (person.getFirstName()+" "+person.getLastName()).toUpperCase().startsWith(name.toUpperCase()))
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

    @Override
    public Person findPersonByID(int id) throws PersonNotFoundException {
        return persons.stream()
                .filter(person -> id == person.getPersonNr())
                .findAny()
                .orElseThrow(() -> {
                    //log.error("FilePersonRepository (findInstructorByName), instructor does not exist " + name);
                    return new PersonNotFoundException("This person does not exist");
                });
    }

    @Override
    public Instructor findInstructorByID(int id) throws PersonNotFoundException {
        return (Instructor)persons.stream()
                .filter(person -> person instanceof Instructor
                        && id == person.getPersonNr())
                .findAny()
                .orElseThrow(() -> {
                    return new PersonNotFoundException("This Instructor does not exist.");
                });
    }

    @Override
    public Student findStudentByID(int id) throws PersonNotFoundException {
        return (Student)persons.stream()
                .filter(person -> person instanceof Student
                        && id == person.getPersonNr())
                .findAny()
                .orElseThrow(() -> {
                    return new PersonNotFoundException("This Student does not exist.");
                });
    }

    @Override
    public Admin findAdminByID(int id) throws PersonNotFoundException {
        return (Admin)persons.stream()
                .filter(person -> person instanceof Admin
                        && id == person.getPersonNr())
                .findAny()
                .orElseThrow(() -> {
                    return new PersonNotFoundException("This Admin does not exist.");
                });
    }

    // GETTER AND SETTER

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

    @Override
    public void addPerson(Person person) {
        persons.add(person);
    }

    @Override
    public void deletePerson(Person person) throws PersonNotFoundException {
        if (persons.contains(person)) {
            persons.remove(person);
        } else {
            throw new PersonNotFoundException("This person isn't found, so cannot be deleten");
        }
    }



}
