package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.SandwichShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SandwichShopJPARepository extends JpaRepository<SandwichShop, Integer> {

    /*
    SandwichShop findSandwichShopByName(String name) throws SandwichShopNotFoundException;
    List<SandwichShop> getShops();
    void addShop(SandwichShop sandwichShop);
    void deleteShop(SandwichShop sandwichShop) throws SandwichShopNotFoundException;

    public SandwichShop findSandwichShopById(int id) throws SandwichShopNotFoundException;


     */
}

