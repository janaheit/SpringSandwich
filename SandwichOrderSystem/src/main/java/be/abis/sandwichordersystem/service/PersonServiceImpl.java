package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired private PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    @Override
    public Person findPersonByName(String name) throws PersonNotFoundException {
        return personRepository.findPersonByName(name);
    }

    @Override
    public Instructor findInstructorByName(String name) throws PersonNotFoundException {
        return personRepository.findInstructorByName(name);
    }

    @Override
    public Student findStudentByName(String name) throws PersonNotFoundException {
        return personRepository.findStudentByName(name);
    }

    @Override
    public Admin findAdminByName(String name) throws PersonNotFoundException {
        return findAdminByName(name);
    }

    @Override
    public Person findPersonByID(int id) throws PersonNotFoundException {
        return findPersonByID(id);
    }

    @Override
    public Instructor findInstructorByID(int id) throws PersonNotFoundException {
        return findInstructorByID(id);
    }

    @Override
    public Student findStudentByID(int id) throws PersonNotFoundException {
        return personRepository.findStudentByID(id);
    }

    @Override
    public Admin findAdminByID(int id) throws PersonNotFoundException {
        return personRepository.findAdminByID(id);
    }
}