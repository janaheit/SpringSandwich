package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.SandwichShop;

import java.util.List;

public interface SandwichShopFactory {

    List<SandwichShop> createSandwichShops();
}
