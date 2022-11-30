package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.model.Admin;
import be.abis.sandwichordersystem.model.Instructor;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonJpaRepository extends JpaRepository<Person, Integer> {

    @Query(value = "select * from persons where lower(concat(plname, pfname)) like lower(concat('%', :name, '%'))", nativeQuery = true)
    Person findPersonByName(@Param("name") String name);

    @Query(value = "select * from persons where lower(concat(plname, pfname)) like lower(concat('%', :name, '%')) and kind = 'i'", nativeQuery = true)
    Instructor findInstructorByName(@Param("name") String name);

    @Query(value = "select * from persons where lower(concat(plname, pfname)) like lower(concat('%', :name, '%')) and kind = 's'", nativeQuery = true)
    Student findStudentByName(@Param("name") String name);

    @Query(value = "select * from persons where lower(concat(plname, pfname)) like lower(concat('%', :name, '%')) and kind = 'a'", nativeQuery = true)
    Admin findAdminByName(@Param("name") String name);

    @Query(value = "select * from persons where pid = :id", nativeQuery = true)
    Person findPersonById(@Param("id") int id);

    @Query(value = "select * from persons where pid = :id and kind = 'i'", nativeQuery = true)
    Instructor findInstructorByID(@Param("id") int id);

    @Query(value = "select * from persons where pid = :id and kind = 's'", nativeQuery = true)
    Student findStudentByID(@Param("id") int id);

    @Query(value = "select * from persons where pid = :id and kind = 'a'", nativeQuery = true)
    Admin findAdminByID(@Param("id") int id);

    @Query(value = "select * from persons", nativeQuery = true)
    List<Person> getPersons();

    @Query(value="select * from persons where kind = 'a'", nativeQuery = true)
    List<Admin> getAdmins();

    @Query(value="select * from persons where kind = 'i'", nativeQuery = true)
    List<Instructor> getInstructors();

    @Query(value="select * from persons where kind = 's'", nativeQuery = true)
    List<Student> getStudents();

}
