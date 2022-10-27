package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;

public interface PersonService {

    Person findPersonByName(String name) throws PersonNotFoundException;
    Instructor findInstructorByName(String name) throws PersonNotFoundException;
    Student findStudentByName(String name) throws PersonNotFoundException;
    Admin findAdminByName(String name) throws PersonNotFoundException;

    Person findPersonByID(int id) throws PersonNotFoundException;
    Instructor findInstructorByID(int id) throws PersonNotFoundException;
    Student findStudentByID(int id) throws PersonNotFoundException;
    Admin findAdminByID(int id) throws PersonNotFoundException;
}
