package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.Course;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SessionTest {

    Session cut;

    @Mock
    Instructor mockPerson;

    @Mock
    Student student1;

    @Mock
    Student student2;

    @BeforeEach
    public void setUp() {
        cut = new Session(Course.JAVA_ADVANCED, mockPerson, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2));
    }



}
