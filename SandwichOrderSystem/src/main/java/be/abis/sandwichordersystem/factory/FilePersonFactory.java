package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.repository.PersonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// @Component
public class FilePersonFactory implements PersonFactory {

    private String filename = "/temp/javacourses/sandwichpersons.txt";

    @Override
    public List<Person> createPersons() {
        return readPersonFile();
    }

    public List<Person> readPersonFile(){
        List<Person> persons = new ArrayList<>();

        try {
            List<String> personStrings = Files.readAllLines(Paths.get(filename));
            for(String s:personStrings){
                persons.add(readPersonFromLine(s));
            }
        } catch (IOException | SubTypePersonNotFoundException e) {
            log.error("FilePersonRepository (Constructor) exception " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return persons;
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
                s.setSession(ListSessionRepository.getInstance().findSessionByID(Integer.parseInt(fields[4])));
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
}
