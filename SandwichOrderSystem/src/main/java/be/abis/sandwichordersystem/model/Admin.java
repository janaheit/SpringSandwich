package be.abis.sandwichordersystem.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("a")
public class Admin extends Person {

    public Admin() {
    }

    public Admin(String firstName, String lastName) {
        super(firstName, lastName);
    }
}
