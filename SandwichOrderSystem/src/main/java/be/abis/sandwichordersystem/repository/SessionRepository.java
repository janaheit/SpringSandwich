package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionRepository {

    Session findSessionByID(int id) throws SessionNotFoundException;
    List<Session> findSessionsByInstructor(Instructor instructor);
    List<Session> findSessionsOnDate(LocalDate date);
    List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date);
    List<Person> findAllPersonsFollowingSession(Session session);

    List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate);

    List<Session> getSessions();
    void addSession(Session session);
    void deleteSession(Session session) throws SessionNotFoundException;
}
