package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.factory.PersonFactory;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.exception.SubTypePersonNotFoundException;
import be.abis.sandwichordersystem.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.FileIOService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FilePersonRepository implements PersonRepository {

    @Autowired private PersonFactory personFactory;
    private String filename = "/temp/javacourses/sandwichpersons.txt";

    List<Person> persons = new ArrayList<>();
    private Logger log = LogManager.getLogger("sandwichLogger");

    public FilePersonRepository() {

    }

    @PostConstruct
    public void init(){
        persons = personFactory.createPersons();
    }

    // BUSINESS METHODS

    public void readFromFile(){
        try {
            List<String> personStrings = Files.readAllLines(Paths.get(filename));
            for(String s:personStrings){
                persons.add(readPersonFromLine(s));
            }
        } catch (IOException | SubTypePersonNotFoundException e) {
            log.error("FilePersonRepository (readFromFile) exception " + e.getMessage());
            System.out.println("Could not read repository...");
            System.out.println(e.getMessage());
        }
    }

    public Person readPersonFromLine(String line) throws SubTypePersonNotFoundException {
        String[] fields = line.split(";");

        String type = fields[0];

        int personNr = Integer.parseInt(fields[1]);
        String firstName = fields[2];
        String lastName = fields[3];

        Person p;
        if (type.equals("S")){
            p = createStudent(fields, firstName, lastName, personNr);
        } else if (type.equals("I")){
            p = createInstructor(fields, firstName, lastName, personNr);
        } else if (type.equals("A")) {
            p = createAdmin(fields, firstName, lastName, personNr);
        } else {
            throw new SubTypePersonNotFoundException(type + " could not be found in subtypes");
        }

        return p;
    }

    public Person createStudent(String[] fields, String firstName, String lastName, int personNr){

        Student s = new Student(firstName, lastName);
        s.setPersonNr(personNr);

        if (!fields[4].equals("null")){
            try {
                s.setCurrentSession(ListSessionRepository.getInstance().findSessionByID(Integer.parseInt(fields[4])));
            } catch (SessionNotFoundException e) {
                System.out.println("Student "+ firstName + " was not added to session " + fields[4]);
                System.out.println(e.getMessage());
            }
        }

        return s;
    }

    public Person createInstructor(String[] fields, String firstName, String lastName, int personNr){

        Instructor i = new Instructor(firstName, lastName);
        i.setPersonNr(personNr);

        if (!fields[4].equals("null")){
            try {
                i.teachSession(ListSessionRepository.getInstance().findSessionByID(Integer.parseInt(fields[4])));
            } catch (SessionNotFoundException e) {
                System.out.println("Instructor "+ firstName + " was not added to session " + fields[4]);
                System.out.println(e.getMessage());
            }
        }

        return i;
    }

    public Person createAdmin(String[] fields, String firstName, String lastName, int personNr){

        Admin a = new Admin(firstName, lastName);
        a.setPersonNr(personNr);

        return a;
    }


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
                    log.error("FilePersonRepository (findInstructorByName), instructor does not exist " + name);
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
                    log.error("FilePersonRepository (findStudentByName) student does not exist " + name);
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
                    log.error("FilePersonRepository (findAdminByName) admin does not exist " + name);
                    return new PersonNotFoundException("This Admin does not exist.");
                });
    }

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
