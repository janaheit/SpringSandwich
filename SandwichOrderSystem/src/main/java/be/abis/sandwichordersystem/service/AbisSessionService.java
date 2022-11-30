package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.exception.SessionAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.PersonJpaRepository;
import be.abis.sandwichordersystem.repository.SessionJpaRepository;
import be.abis.sandwichordersystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AbisSessionService implements SessionService {

    @Autowired private SessionJpaRepository sessionRepository;
    @Autowired
    private PersonJpaRepository personRepository;

    public AbisSessionService() {
    }

    @Override
    public List<Person> findAllPersonsFollowingSession(int sessionId) {
        return personRepository.findAllPersonsFollowingSession(sessionId);
    }

    @Override
    public List<Person> findAllPersonsFollowingSessionToday() {
        return personRepository.findAllPersonsFollowingSessionOnDate(LocalDate.now());
    }

    @Override
    public List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date) {
        return personRepository.findAllPersonsFollowingSessionOnDate(date);
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
    public List<Session> findSessionsByInstructor(int instructorId) {
        return sessionRepository.findSessionsByInstructor(instructorId);
    }

    @Override
    public Session findSessionByID(int id) throws SessionNotFoundException {
        Session mySession = sessionRepository.findSessionByID(id);
        if (mySession == null) {
            throw new SessionNotFoundException("No session with id " + id + " was found!");
        }
        return mySession;
    }

    //TODO might have to implement some checks, throw exceptions etc.
    @Override
    public void enrolPersonForSession(int personId, int sessionId) {
        sessionRepository.setCurrentSessionForPerson(personId, sessionId);
    }

    //TODO Implement further validation
    @Override
    public void setTeacherForSession(int personId, int sessionId) {
        sessionRepository.setInstructorInSession(personId, sessionId);
        sessionRepository.setCurrentSessionForPerson(personId, sessionId);
    }

    // Basic method implementations
    @Override
    public void addSession(Session session) throws SessionAlreadyExistsException {
        Course myCourse = session.getCourse();
        LocalDate startDate = session.getStartDate();
        LocalDate endDate = session.getEndDate();
        Integer instructorId = null;
        System.out.println("Add session started");
        if(session.getInstructor() != null) {
            System.out.println("Instructor not null");
            instructorId = session.getInstructor().getPersonNr();
            System.out.println(instructorId);
        }
        List<Session> checkSessions = this.sessionRepository.findExactSessionsIfExist(myCourse.name(), startDate, endDate, instructorId.intValue());
        System.out.println("List of sessions retrieved");
        if (checkSessions.size()==0) {
            this.sessionRepository.save(session);
        } else {
            throw new SessionAlreadyExistsException("This session already exists and cannot be added");
        }
    }

    @Override
    public List<Session> getSessions() {
        return this.sessionRepository.getSessions();
    }

    @Override
    public void deleteSession(Session session) throws SessionNotFoundException {
        this.sessionRepository.delete(session);
    }

    @Override
    public List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate) {
        return sessionRepository.findSessionsByPeriod(startDate, endDate);
    }

    @Override
    public Session findDbSession(Session session) {
        Course myCourse = session.getCourse();
        LocalDate startDate = session.getStartDate();
        LocalDate endDate = session.getEndDate();
        Integer instructorId = null;
        if(session.getInstructor() != null) {
            instructorId = session.getInstructor().getPersonNr();
        }
        List<Session> checkSessions = this.sessionRepository.findExactSessionsIfExist(myCourse.name(), startDate, endDate, instructorId);
        return checkSessions.get(0);
    }
}
