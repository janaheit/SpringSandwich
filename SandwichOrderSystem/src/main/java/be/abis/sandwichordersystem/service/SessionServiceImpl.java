package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired private SessionRepository sessionRepository;

    public SessionServiceImpl() {
    }

    @Override
    public List<Person> findAllPersonsFollowingSession(Session session) {
        return sessionRepository.findAllPersonsFollowingSession(session);
    }

    @Override
    public List<Person> findAllPersonsFollowingSessionToday() {
        return sessionRepository.findAllPersonsFollowingSessionOnDate(LocalDate.now());
    }

    @Override
    public List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date) {
        return sessionRepository.findAllPersonsFollowingSessionOnDate(date);
    }

    @Override
    public List<Session> findSessionsToday() {
        return sessionRepository.findSessionsOnDate(LocalDate.now());
    }

    @Override
    public List<Session> findSessionsOnDate(LocalDate date) {
        return sessionRepository.findSessionsOnDate(date);
    }

    @Override
    public List<Session> findSessionsByInstructor(Instructor instructor) {
        return sessionRepository.findSessionsByInstructor(instructor);
    }

    @Override
    public Session findSessionByID(int id) throws SessionNotFoundException {
        return sessionRepository.findSessionByID(id);
    }

    // Basic method implementations
    @Override
    public void addSession(Session session) {
        this.sessionRepository.addSession(session);
    }

    @Override
    public List<Session> getSessions() {
        return this.sessionRepository.getSessions();
    }

    @Override
    public void deleteSession(Session session) throws SessionNotFoundException {
        this.sessionRepository.deleteSession(session);
    }
}
