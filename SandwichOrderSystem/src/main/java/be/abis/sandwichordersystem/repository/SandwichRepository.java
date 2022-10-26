package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import exception.SandwichNotFoundException;
import model.SandwichAdmin.Sandwich;

import java.util.List;

public interface SandwichRepository {
    Sandwich findSandwichByID(int id) throws SandwichNotFoundException;
    List<Sandwich> getSandwiches();
    void addSandwich(Sandwich sandwich);
    void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException;
}
