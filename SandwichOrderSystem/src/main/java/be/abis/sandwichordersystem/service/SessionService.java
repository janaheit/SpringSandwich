package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    List<Person> findAllPersonsFollowingSession(Session session);
    List<Person> findAllPersonsFollowingSessionToday();
    List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date);

    List<Session> findSessionsToday();
    List<Session> findSessionsOnDate(LocalDate date);
    List<Session> findSessionsByInstructor(Instructor instructor);

    List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate);

    Session findSessionByID(int id) throws SessionNotFoundException;

    // Basic methods
    void addSession(Session session);
    List<Session> getSessions();
    void deleteSession(Session session) throws SessionNotFoundException;
}
