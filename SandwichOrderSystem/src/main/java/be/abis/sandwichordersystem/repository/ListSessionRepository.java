package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.factory.SessionFactory;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ListSessionRepository implements SessionRepository {

    @Autowired private SessionFactory sessionFactory;
    private List<Session> sessions;

    public ListSessionRepository() {
    }

    @PostConstruct
    public void init(){
        sessions = sessionFactory.createSessions();
    }

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
    public List<Session> findSessionsOnDate(LocalDate date) {
        List<Session> output = this.sessions.stream().filter(session -> session.getStartDate().isBefore(date.plusDays(1))).filter(session -> session.getEndDate().isAfter(date.minusDays(1))).collect(Collectors.toList());
        return output;
    }

    @Override
    public List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date) {
        List<Session> sessionsOnDate = findSessionsOnDate(date);
        List<Person> personsFollowingSessionOnDate = new ArrayList<>();

        for (Session s:sessionsOnDate){
            //personsFollowingSessionOnDate.addAll(s.getStudents());
            personsFollowingSessionOnDate.add(s.getInstructor());
        }

        HashSet<Person> personHashSet = new HashSet<>(personsFollowingSessionOnDate);
        return new ArrayList<>(personHashSet);
    }

    @Override
    public List<Person> findAllPersonsFollowingSession(Session session) {

        List<Person> personsFollowingSession = new ArrayList<>();
        //personsFollowingSession.addAll(session.getStudents());
        personsFollowingSession.add(session.getInstructor());

        return personsFollowingSession;
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
    public void deleteSession(Session session) throws SessionNotFoundException {
        if(this.sessions.contains(session)) {
            this.sessions.remove(session);
        } else {
            throw new SessionNotFoundException(session.getCourse().getTitle() + " could not be found and therefore could not be deleted.");
        }
    }

    @Override
    public List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate) {
        return this.sessions.stream().filter(session -> session.getStartDate().isBefore(startDate.plusDays(1)) || session.getStartDate().isBefore(endDate.minusDays(1))).filter(session -> session.getEndDate().isAfter(startDate.minusDays(1)) || session.getEndDate().isAfter(endDate.minusDays(1))).collect(Collectors.toList());
    }
}
