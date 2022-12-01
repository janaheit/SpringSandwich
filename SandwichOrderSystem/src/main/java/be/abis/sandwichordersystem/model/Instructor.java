package be.abis.sandwichordersystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@DiscriminatorValue("i")
public class Instructor extends Person{

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "P_SID")
    private Session instructingSession;

    public Instructor() {
    }

    public Instructor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void teachSession(Session session){
        this.instructingSession = session;
    }

    // Getters and setters
    public Session getCurrentSession() {
        return instructingSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.instructingSession = currentSession;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + getPersonNr() + ", " +
                "name=" + getFirstName() + " " + getLastName() + ", " +
                "currentSession=" + instructingSession +
                '}';
    }
}
