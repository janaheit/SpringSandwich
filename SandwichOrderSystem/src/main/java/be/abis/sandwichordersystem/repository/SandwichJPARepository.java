package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SandwichJPARepository extends JpaRepository<Sandwich, Integer> {
    /*Sandwich findSandwichByID(int id) throws SandwichNotFoundException;
    Sandwich findSandwichByName(String name) throws SandwichNotFoundException;
    List<Sandwich> getSandwiches();
    void addSandwich(Sandwich sandwich);
    void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException;

     */
}
