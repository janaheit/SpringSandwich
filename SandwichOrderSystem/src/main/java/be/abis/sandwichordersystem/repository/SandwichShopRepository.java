package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.SandwichShop;


import java.util.List;

public interface SandwichShopRepository {

    SandwichShop findSandwichShopByName(String name) throws SandwichShopNotFoundException;
    List<SandwichShop> getShops();
    void addShop(SandwichShop sandwichShop);
    void deleteShop(SandwichShop sandwichShop) throws SandwichShopNotFoundException;

    public SandwichShop findSandwichShopById(int id) throws SandwichShopNotFoundException;
}
