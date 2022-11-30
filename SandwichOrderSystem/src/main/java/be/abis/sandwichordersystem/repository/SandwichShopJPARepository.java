package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.model.SandwichShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SandwichShopJPARepository extends JpaRepository<SandwichShop, Integer> {

    @Query(value = "select option from ssoptions where ss_sandshopid = :shopID", nativeQuery = true)
    List<String> findOptionsForShopID(int shopID);
    @Query(value = "select bread from ssbreadtypes where ss_sandshopid=:shopID", nativeQuery =true)
    List<String> findBreadTypesForShopID(int shopID);

    @Query(value = "select sandshopid, shopname from sandwichshops where sandshopid=:shopID", nativeQuery = true)
    List<Object[]> findObjectById(int shopID);
    @Query(value = "select sandshopid, shopname from sandwichshops where shopname=:name", nativeQuery = true)
    List<Object[]> findObjectByName(String name);

    @Query(value = "select * from sandwichshops where sandshopid=:shopID", nativeQuery = true)
    SandwichShop findShopById(int shopID);

    SandwichShop findById(int shopID);
/*
    SandwichShop findSandwichShopByName(String name) throws SandwichShopNotFoundException;
    List<SandwichShop> getShops();
    void addShop(SandwichShop sandwichShop);
    void deleteShop(SandwichShop sandwichShop) throws SandwichShopNotFoundException;
    public SandwichShop findSandwichShopById(int id) throws SandwichShopNotFoundException;

 */
}

