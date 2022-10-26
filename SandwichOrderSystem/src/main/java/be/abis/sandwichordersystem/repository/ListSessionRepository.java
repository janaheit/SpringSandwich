package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ListSessionRepository implements SessionRepository {

    // Attributes
    private List<Session> sessions;

    // Method implementations
    @Override
    public Session findSessionByID(int id) throws SessionNotFoundException {
        List<Session> outputList = this.sessions.stream().filter(session -> session.getSessionNumber() == id).collect(Collectors.toList());
        if (outputList.size() != 1) {
            throw new SessionNotFoundException("Zero or more sessions found " + outputList.size());
        }
        return outputList.get(0);
    }

    @Override
    public List<Session> findSessionsByInstructor(Instructor instructor) {
        List<Session> output = this.sessions.stream().filter(session -> session.getInstructor().equals(instructor)).collect(Collectors.toList());
        return output;
    }

    @Override
    public List<Session> findSessionsByDate(LocalDate date) {
        List<Session> output = this.sessions.stream().filter(session -> session.getStartDate().isBefore(date.plusDays(1))).filter(session -> session.getEndDate().isBefore(date.plusDays(1))).collect(Collectors.toList());
        return output;
    }

    @Override
    public List<Session> getSessions() {
        return this.sessions;
    }

    @Override
    public void addSession(Session session) {
        this.sessions.add(session);
    }

    @Override
    public void deleteSession(Session session) {
        if(this.sessions.contains(session)) {
            this.sessions.remove(session);
        }
    }
}