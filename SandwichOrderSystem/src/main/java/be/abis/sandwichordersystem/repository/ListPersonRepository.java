package be.abis.sandwichordersystem.repository;

import exception.PersonNotFoundException;
import model.CourseAdmin.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListPersonRepository implements PersonRepository {

    private static ListPersonRepository personRepo = new ListPersonRepository();

    Admin admin;
    List<Instructor> instructors = new ArrayList<Instructor>();
    List<Student> students = new ArrayList<Student>();
    List<Course> courses = new ArrayList<>();
    List<Session> sessions = new ArrayList<Session>();

    private ListPersonRepository() {
        admin = new Admin("VI", "P");

        instructors.add(new Instructor("Sandy", "Schillenbeeckx"));
        instructors.add(new Instructor("Japie", "Yolo"));

        students.add(new Student("Kim", "Wauters"));
        students.add(new Student("Quentin", "Locht"));
        students.add(new Student("Claudia", "Negrila"));
        students.add(new Student("Jens", "Verheyden"));
        students.add(new Student("Marcel", "van Hassel"));
        students.add(new Student("Simon", "Hazevoets"));
        students.add(new Student("Jana", "Heitkemper"));
        students.add(new Student("Esben", "Six"));
        students.add( new Student("Lisa", "Muller"));
        students.add( new Student("Henk", "de Vries"));

        courses.add(Course.INTERNET_ENABLING);
        courses.add(Course.JAVA_ADVANCED);

        sessions.add(new Session(courses.get(0), instructors.get(0), LocalDate.now(), LocalDate.now()));
        sessions.add(new Session(courses.get(1), instructors.get(1),LocalDate.now(), LocalDate.now()));

        sessions.get(0).setInstructor(instructors.get(0));
        sessions.get(1).setInstructor(instructors.get(1));

        students.get(0).setSession(sessions.get(0));
        students.get(1).setSession(sessions.get(0));
        students.get(2).setSession(sessions.get(0));
        students.get(3).setSession(sessions.get(0));
        students.get(4).setSession(sessions.get(0));
        students.get(5).setSession(sessions.get(1));
        students.get(6).setSession(sessions.get(1));
        students.get(7).setSession(sessions.get(1));
        students.get(8).setSession(sessions.get(1));
        students.get(9).setSession(sessions.get(1));

    }

    public static ListPersonRepository getInstance(){
        return personRepo;
    }

    public Admin getAdmin() {
        return admin;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Session> getSessions() {
        return sessions;
    }


    @Override
    public Person findPersonByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Instructor findInstructorByName(String name) throws PersonNotFoundException {
        return null;
    }

    @Override
    public Student findStudentByName(String name) throws PersonNotFoundException {
        return null;
    }

    @Override
    public Admin findAdminByName(String name) throws PersonNotFoundException {
        return null;
    }

    @Override
    public List<Person> getPersons() {
        return null;
    }

    @Override
    public List<Admin> getAdmins() {
        return null;
    }
}
