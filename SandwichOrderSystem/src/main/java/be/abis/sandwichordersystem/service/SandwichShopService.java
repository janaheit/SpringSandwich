package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;

import java.util.List;

public interface SandwichShopService {

    public List<Sandwich> getSandwiches(SandwichShop sandwichShop);
    public List<Options> getOptions(SandwichShop sandwichShop);
    public List<BreadType> getBreadTypes(SandwichShop sandwichShop);

    public boolean checkSandwich(Sandwich sandwich, SandwichShop sandwichShop);
    public List<Options> checkOptions(List<Options> options, SandwichShop sandwichShop);
    public boolean checkBreadType(BreadType breadType, SandwichShop sandwichShop);

    public void addSandwich(Sandwich sandwich, SandwichShop sandwichShop);
    public void deleteSandwich(Sandwich sandwich);

    public Sandwich findSandwichByName(String name);
    public Sandwich findSandwichById(int id);

}
