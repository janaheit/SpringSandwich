package be.abis.sandwichordersystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Student extends Person {

    @JsonIgnore
    private Session currentSession;

    public Student() {
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    // Getters and Setters
    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
}
