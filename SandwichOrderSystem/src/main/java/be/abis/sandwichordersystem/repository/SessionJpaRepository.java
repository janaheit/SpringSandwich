package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SessionJpaRepository extends JpaRepository<Session, Integer> {
    /*

    @Query(value = " select * from sessions where sid = :sid", nativeQuery = true)
    Session findSessionByID(@Param("sid") int id);

    List<Session> findSessionsByInstructor(Instructor instructor);
    List<Session> findSessionsOnDate(LocalDate date);
    List<Person> findAllPersonsFollowingSessionOnDate(LocalDate date);
    List<Person> findAllPersonsFollowingSession(Session session);

    List<Session> findSessionsByPeriod(LocalDate startDate, LocalDate endDate);

    List<Session> getSessions();

     */

}
