package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;

import java.util.List;

public interface SandwichJPAService {
    //SandwichShopRepository getSandwichShopRepository();
    List<SandwichShop> getSandwichShops();
    List<Sandwich> getSandwichesForShop(int shopID);
    List<Options> getOptionsForShop(int shopID);
    List<BreadType> getBreadTypesForShop(int shopID);

    boolean checkIfSandwichInShop(int sandwichID, int shopID);
    List<Options> checkIfOptionsInShop(List<Options> options, int shopID);
    boolean checkIfBreadTypeInShop(BreadType breadType, int shopID);

    void addSandwichToShop(Sandwich sandwich, int shopID) throws SandwichAlreadyExistsException, SandwichShopNotFoundException;
    void deleteSandwichByID(int sandwichID) throws SandwichNotFoundException;

    Sandwich findSandwichByName(String name) throws SandwichNotFoundException;
    Sandwich findSandwichById(int id) throws SandwichNotFoundException;

    SandwichShop findShopForSandwich(int sandwichID) throws SandwichShopNotFoundException;
    SandwichShop findShopByName(String name) throws SandwichShopNotFoundException;
    SandwichShop findShopByID(int id) throws SandwichShopNotFoundException;
}
