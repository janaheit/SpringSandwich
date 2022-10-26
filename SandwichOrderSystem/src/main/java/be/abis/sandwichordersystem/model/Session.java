package be.abis.sandwichordersystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Session {
    private Course course;
    private Instructor instructor;
    private List<Student> students = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private static int counter = 0;
    private int sessionNumber;

    public Session(Course course, Instructor instructor, LocalDate startDate, LocalDate endDate) {
        this.course = course;
        this.instructor = instructor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.counter ++;
        this.sessionNumber = counter;
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

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
}
