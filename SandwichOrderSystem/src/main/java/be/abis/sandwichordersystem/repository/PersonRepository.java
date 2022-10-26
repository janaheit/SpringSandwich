package be.abis.sandwichordersystem.repository;

import exception.PersonNotFoundException;
import model.CourseAdmin.Admin;
import model.CourseAdmin.Instructor;
import model.CourseAdmin.Person;
import model.CourseAdmin.Student;

import java.util.List;

public interface PersonRepository {

    Person findPersonByName(String name);
    Instructor findInstructorByName(String name) throws PersonNotFoundException;
    Student findStudentByName(String name) throws PersonNotFoundException;
    Admin findAdminByName(String name) throws PersonNotFoundException;
    List<Person> getPersons();
    List<Admin> getAdmins();
    List<Instructor> getInstructors();
    List<Student> getStudents();
    void addPerson(Person person);
    void deletePerson(Person person) throws PersonNotFoundException;
}
