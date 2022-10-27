package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;

import java.util.List;

public interface SandwichShopService {
    SandwichShopRepository getSandwichShopRepository();
    List<Sandwich> getSandwiches(SandwichShop sandwichShop);
    List<Options> getOptions(SandwichShop sandwichShop);
    List<BreadType> getBreadTypes(SandwichShop sandwichShop);

    boolean checkSandwich(Sandwich sandwich, SandwichShop sandwichShop);
    List<Options> checkOptions(List<Options> options, SandwichShop sandwichShop);
    boolean checkBreadType(BreadType breadType, SandwichShop sandwichShop);

    void addSandwich(Sandwich sandwich, SandwichShop sandwichShop);
    void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException;

    Sandwich findSandwichByName(String name) throws SandwichNotFoundException;
    Sandwich findSandwichById(int id) throws SandwichNotFoundException;

}
