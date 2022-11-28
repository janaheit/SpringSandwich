package be.abis.sandwichordersystem.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="KIND", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("")
@Table(name="PERSONS")
public class Person {

    @SequenceGenerator(name = "PersonIdGen", sequenceName = "persons_pid_seq", allocationSize = 1)

    @Column(name = "PFNAME")
    private String firstName;
    @Column(name = "PLNAME")
    private String lastName;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonIdGen")
    @Column(name = "PID")
    private int personNr;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personNr == person.personNr && firstName.equals(person.firstName) && lastName.equals(person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, personNr);
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPersonNr() {
        return personNr;
    }

    public void setPersonNr(int personNr) {
        this.personNr = personNr;
    }

}
