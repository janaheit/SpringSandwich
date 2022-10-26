package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.Person;

import java.util.List;

public interface PersonFactory {

    List<Person> createPersons();
}
