package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListPersonFactory implements PersonFactory {
    @Override
    public List<Person> createPersons() {
        List<Person> persons = new ArrayList<>();

        persons.add(new Admin("VI", "P"));
        persons.add(new Instructor("Sandy", "Schillenbeeckx"));
        persons.add(new Instructor("Japie", "Yolo"));

        persons.add(new Student("Kim", "Wauters"));
        persons.add(new Student("Quentin", "Locht"));
        persons.add(new Student("Claudia", "Negrila"));
        persons.add(new Student("Jens", "Verheyden"));
        persons.add(new Student("Marcel", "van Hassel"));
        persons.add(new Student("Simon", "Hazevoets"));
        persons.add(new Student("Jana", "Heitkemper"));
        persons.add(new Student("Esben", "Six"));
        persons.add( new Student("Lisa", "Muller"));
        persons.add( new Student("Henk", "de Vries"));;

        return persons;
    }
}
