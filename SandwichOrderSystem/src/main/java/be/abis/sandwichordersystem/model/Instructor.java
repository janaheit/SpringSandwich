package be.abis.sandwichordersystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@DiscriminatorValue("i")
public class Instructor extends Person{

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "P_SID")
    private Session currentSession;

    public Instructor() {
    }

    public Instructor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void teachSession(Session session){
        this.currentSession = session;
    }

    // Getters and setters
    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + getPersonNr() + ", " +
                "name=" + getFirstName() + " " + getLastName() + ", " +
                "currentSession=" + currentSession +
                '}';
    }
}
