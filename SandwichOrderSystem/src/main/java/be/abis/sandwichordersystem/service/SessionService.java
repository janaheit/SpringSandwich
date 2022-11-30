package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.SessionAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    List<Person> findAllPersonsFollowingSession(int sessionId);
    List<Person> findAllPersonsFollowingSessionToday();
    List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date);

    List<Session> findSessionsToday();
    List<Session> findSessionsOnDate(LocalDate date);
    List<Session> findSessionsByInstructor(int instructorId);

    List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate);

    Session findSessionByID(int id) throws SessionNotFoundException;

    void enrolPersonForSession(int personId, int sessionId);

    void setTeacherForSession(int personId, int sessionId);

    Session findDbSession(Session session);

    // Basic methods
    void addSession(Session session) throws SessionAlreadyExistsException;
    List<Session> getSessions();
    void deleteSession(Session session) throws SessionNotFoundException;
}
