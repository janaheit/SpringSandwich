package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;

import java.util.List;

public interface SandwichRepository {
    Sandwich findSandwichByID(int id) throws SandwichNotFoundException;
    Sandwich findSandwichByName(String name) throws SandwichNotFoundException;
    List<Sandwich> getSandwiches();
    void addSandwich(Sandwich sandwich);
    void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException;
}
