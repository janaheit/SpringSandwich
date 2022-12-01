package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;

import java.util.List;

public interface PersonRepository {

    Person findPersonByName(String name) throws PersonNotFoundException;
    Instructor findInstructorByName(String name) throws PersonNotFoundException;
    Student findStudentByName(String name) throws PersonNotFoundException;
    Admin findAdminByName(String name) throws PersonNotFoundException;

    Person findPersonByID(int id) throws PersonNotFoundException;
    Instructor findInstructorByID(int id) throws PersonNotFoundException;
    Student findStudentByID(int id) throws PersonNotFoundException;
    Admin findAdminByID(int id) throws PersonNotFoundException;

    List<Person> getPersons();
    List<Admin> getAdmins();
    List<Instructor> getInstructors();
    List<Student> getStudents();
    void addPerson(Person person);
    void deletePerson(Person person) throws PersonNotFoundException;
}
