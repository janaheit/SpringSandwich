package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.Sandwich;

import java.util.List;

public interface SandwichFactory {

    List<Sandwich> createSandwiches();
}
