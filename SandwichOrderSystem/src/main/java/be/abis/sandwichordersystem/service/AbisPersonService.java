package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.OperationNotAllowedException;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import be.abis.sandwichordersystem.repository.PersonJpaRepository;
import be.abis.sandwichordersystem.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbisPersonService implements PersonService {

    @Autowired private PersonJpaRepository personRepository;

    public AbisPersonService() {
    }

    @Override
    public Person findPersonByName(String name) throws PersonNotFoundException {
        //System.out.println("lets go: " +name);
        Person myPerson = personRepository.findPersonByName(name);
        if (myPerson == null) {
            throw new PersonNotFoundException("No person with name: " + name + " found");
        }
        return myPerson;
    }

    @Override
    public Instructor findInstructorByName(String name) throws PersonNotFoundException {
        Instructor myInstructor = personRepository.findInstructorByName(name);
        if(myInstructor == null) {
            throw new PersonNotFoundException("No instructor with name: " + name + " found");
        }
        return myInstructor;
    }

    @Override
    public Student findStudentByName(String name) throws PersonNotFoundException {
        Student myStudent = personRepository.findStudentByName(name);
        if(myStudent == null) {
            throw new PersonNotFoundException("No student with name: " + name + " found");
        }
        return myStudent;
    }

    @Override
    public Admin findAdminByName(String name) throws PersonNotFoundException {
        Admin myAdmin = personRepository.findAdminByName(name);
        if(myAdmin==null) {
            throw new PersonNotFoundException("No admin with name: " + name + " found");
        }
        return myAdmin;
    }

    @Override
    public Person findPersonByID(int id) throws PersonNotFoundException {
        Person myPerson = personRepository.findPersonById(id);
        if (myPerson==null) {
            throw new PersonNotFoundException("No person found with id: " + id + "!");
        }
        return myPerson;
    }

    @Override
    public Instructor findInstructorByID(int id) throws PersonNotFoundException {
        Instructor myInstructor = personRepository.findInstructorByID(id);
        if (myInstructor==null) {
            throw new PersonNotFoundException("No instructor found with id: " + id + "!");
        }
        return myInstructor;
    }

    @Override
    public Student findStudentByID(int id) throws PersonNotFoundException {
        Student myStudent = personRepository.findStudentByID(id);
        if(myStudent==null) {
            throw new PersonNotFoundException("No student found with id: " + id + "!");
        }
        return myStudent;
    }

    @Override
    public Admin findAdminByID(int id) throws PersonNotFoundException {
        Admin myAdmin = personRepository.findAdminByID(id);
        if(myAdmin==null) {
            throw new PersonNotFoundException("No admin found with id: " + id + "!");
        }
        return myAdmin;
    }

    @Override
    public List<Person> getPersons() {
        return personRepository.getPersons();
    }

    @Override
    public List<Admin> getAdmins() {
        return personRepository.getAdmins();
    }

    @Override
    public List<Instructor> getInstructors() {
        return personRepository.getInstructors();
    }

    @Override
    public List<Student> getStudents() {
        return personRepository.getStudents();
    }

    @Override
    public void addPerson(Person person) throws OperationNotAllowedException {
        Person checkPerson = personRepository.findPersonByName(person.getFirstName()+person.getFirstName());
        if (checkPerson!=null) {
            throw new OperationNotAllowedException("Person already exists and cannot be added.");
        }
        if (person instanceof Student || person instanceof Admin || person instanceof Instructor) {
            personRepository.save(person);
        } else {
            throw new OperationNotAllowedException("This person is not a Student, Instructor or Admin and cannot be added.");
        }
    }

    @Override
    public void deletePerson(Person person) throws PersonNotFoundException {
        Person checkPerson = personRepository.findPersonByName(person.getFirstName()+person.getLastName());
        if (checkPerson==null) {
            throw new PersonNotFoundException("No person found with name " + person.getFirstName() + " " + person.getLastName());
        }
        personRepository.deleteById(checkPerson.getPersonNr());
    }
}
