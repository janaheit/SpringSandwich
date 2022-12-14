package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SessionJpaRepository extends JpaRepository<Session, Integer> {

    @Query(value = " select * from sessions where sid = :sid", nativeQuery = true)
    Session findSessionByID(@Param("sid") int id);

    @Query(value = " select * from sessions where sins_pid = :instructorId", nativeQuery = true)
    List<Session> findSessionsByInstructor(@Param("instructorId") int instructorId);

    @Query(value = "select * from sessions where sstartdate <= :date and senddate >= :date", nativeQuery = true)
    List<Session> findSessionsOnDate(@Param("date") LocalDate date);

    /* Moved to Person repo
    @Query(value = "select pid, plname, pfname, kind, p_sid from sessions join persons on p_sid = sid  where sstartdate <= :date and senddate >= :date", nativeQuery = true)
    List<Person> findAllPersonsFollowingSessionOnDate(@Param("date") LocalDate date);
     */

    /* moved to person repo
    @Query(value = "select pid, plname, pfname, kind, p_sid from sessions join persons on p_sid = sid where sid = :sessionId", nativeQuery = true)
    List<Person> findAllPersonsFollowingSession(@Param("sessionId") int sessionId);
     */

    @Query(value = "select * from sessions where (sstartdate <= :startDate or sstartdate <= :endDate) and (senddate >= :startDate or senddate >= :endDate)", nativeQuery = true)
    List<Session> findSessionsByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select * from sessions", nativeQuery = true)
    List<Session> getSessions();

    @Query(value = "select pid, plname, pfname, kind, p_sid from sessions join persons on p_sid = sid where sid = :sessionId and kind = 's'", nativeQuery = true)
    List<Student> findAllStudentsFollowingSession(@Param("sessionId") int sessionId);

    @Query(value = "select * from sessions where s_course = :course and sstartdate = :sdate and senddate = :enddate and sins_pid = :instructorId", nativeQuery = true)
    List<Session> findExactSessionsIfExist(@Param("course")String courseName, @Param("sdate") LocalDate startDate, @Param("enddate") LocalDate endDate, @Param("instructorId") int instructorId);

    @Modifying
    @Query(value = "update persons set p_sid = :sessionId where pid = :personId", nativeQuery = true)
    public void setCurrentSessionForPerson(@Param("personId") int personId, @Param("sessionId") int sessionId);

    @Modifying
    @Query(value = "update sessions set sins_pid = :instructorId where sid = :sessionId", nativeQuery = true)
    public void setInstructorInSession(@Param("instructorId") int instructorId, @Param("sessionId") int sessionId);

}
