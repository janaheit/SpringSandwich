package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
@Component
public class ListSessionFactory implements SessionFactory {

    @Autowired private PersonRepository personRepository;

    @Override
    public List<Session> createSessions() {
        List<Session> sessions = new ArrayList<>();

        List<Instructor> instructors = personRepository.getInstructors();
        List<Student> students = personRepository.getStudents();

        sessions.add(new Session(Course.JAVA_ADVANCED, instructors.get(0),
                LocalDate.of(2020, 9, 1), LocalDate.of(2022, 12, 31) ));

        sessions.add(new Session(Course.INTERNET_ENABLING, instructors.get(1),
                LocalDate.of(2020, 9, 1), LocalDate.of(2022, 12, 31) ));


        // add students to session 1  and 2

        for (int s=0; s<students.size()/2;s++){
            sessions.get(0).addStudent(students.get(s));
            students.get(s).setCurrentSession(sessions.get(0));
        }

        for (int l=students.size()/2; l<students.size();l++){
            sessions.get(1).addStudent(students.get(l));
            students.get(l).setCurrentSession(sessions.get(1));
        }

        return sessions;
    }
}
*/