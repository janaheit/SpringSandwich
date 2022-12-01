package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.model.Instructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

public class SessionDTO {
    private int sessionNumber;
    private String course;
    private int instructorID;
    private String instructorName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    public SessionDTO() {
    }
    public SessionDTO(int sessionNumber, String course, int instructorID, String instructorName, LocalDate startDate,
                      LocalDate endDate) {
        this.sessionNumber = sessionNumber;
        this.course = course;
        this.instructorID = instructorID;
        this.instructorName = instructorName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
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
}
