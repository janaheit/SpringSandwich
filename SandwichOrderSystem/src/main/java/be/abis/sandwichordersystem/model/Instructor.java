package be.abis.sandwichordersystem.model;

public class Instructor extends Person{

    private Session currentSession;

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
}
