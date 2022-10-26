package be.abis.sandwichordersystem.model;

public class Student extends Person {

    private Session currentSession;

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
