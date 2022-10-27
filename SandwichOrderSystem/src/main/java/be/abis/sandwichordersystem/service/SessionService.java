package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface SessionService {

    List<Person> findAllPersonsFollowingSession(Session session);
    List<Person> findAllPersonsFollowingASessionToday();
    List<Person> findAllPersonsFollowingASessionOnDate(LocalDate date);

    List<Session> findSessionsToday();
    List<Session> findSessionsOnDate(LocalDate date);
    List<Session> findSessionsByInstructor(Instructor instructor);

    Session findSessionByID(int id) throws SessionNotFoundException;
}
