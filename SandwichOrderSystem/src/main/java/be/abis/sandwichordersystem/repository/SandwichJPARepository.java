package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SandwichJPARepository extends JpaRepository<Sandwich, Integer> {
    Sandwich findById(int id);
    Sandwich findByName(String name);
    @Query(value="select * from sandwiches where sand_sandshopid = :shopID", nativeQuery = true)
    List<Sandwich> findSandwichesByShopId(int shopID);

    @Query(value = "select sand_sandshopid from sandwiches where sandid=:sandwichID", nativeQuery = true)
    Integer getShopIDForSandwich(int sandwichID);

    // find out if sandwich already exists
    @Query(value = "select * from sandwiches where sandname=:name and category=:cat and sand_sandshopid=:shopID", nativeQuery = true)
    Sandwich findSandwichByNameAndCategoryAndShop(String name, String cat, int shopID);

    @Modifying
    @Query(value = "insert into sandwiches(sandname, price, category, sand_sandshopid) values (:name, :price, :category, :shopID)", nativeQuery = true)
    void insertSandwich(String name, double price, String category, int shopID);

    @Modifying
    @Query(value = "insert into sandwiches(sandname, price, description, category, sand_sandshopid) " +
            "values (:name, :price, :description, :category, :shopID)", nativeQuery = true)
    void insertSandwich(String name, double price, String description, String category, int shopID);

}
