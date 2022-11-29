package be.abis.sandwichordersystem.model;

import be.abis.sandwichordersystem.enums.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="SESSIONS")
public class Session {

    @SequenceGenerator(name="sessionGen", sequenceName = "sessions_sid_seq", allocationSize = 1)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionGen")
    @Column(name = "SID")
    private int sessionNumber;

    @Column(name = "S_COURSE")
    @Enumerated(EnumType.STRING)
    private Course course;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "SINS_PID")
    private Instructor instructor;
    @Column(name="SSTARTDATE")
    private LocalDate startDate;
    @Column(name = "SENDDATE")
    private LocalDate endDate;


    /*
    @OneToMany(targetEntity = Student.class, mappedBy = "currentSession", fetch = FetchType.EAGER)
    private List<Student> students;
     */

    public Session() {
    }

    public Session(Course course, Instructor instructor, LocalDate startDate, LocalDate endDate) {
        this.course = course;
        this.instructor = instructor;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addStudent(Student student){
        //students.add(student);
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Instructor getInstructor() {
        return instructor;
    }


    public List<Student> getStudents() {
        return new ArrayList<>();

    }
/*
    public void setStudents(List<Student> students) {
        this.students = students;
    }

     */



    public Course getCourse() {
        return course;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // equals and hashcode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return sessionNumber == session.sessionNumber && course.equals(session.course) && instructor.equals(session.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, instructor, sessionNumber);
    }

    @Override
    public String toString() {
        return "Session{" +
                "course=" + course +
                ", instructor=" + instructor +
                ", students=" + //students +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", sessionNumber=" + sessionNumber +
                '}';
    }
}
