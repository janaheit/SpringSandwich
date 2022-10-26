package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.factory.SandwichShopFactory;
import be.abis.sandwichordersystem.model.SandwichShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class ListSandwichShopRepository implements SandwichShopRepository {

    @Autowired private SandwichShopFactory sandwichShopFactory;
    private List<SandwichShop> shops;

    public ListSandwichShopRepository() {
    }

    @PostConstruct
    public void init(){
        shops = sandwichShopFactory.createSandwichShops();
    }

    // BUSINESS METHODS

    @Override
    public SandwichShop findSandwichShopByName(String name) throws SandwichShopNotFoundException {
        return this.shops.stream()
                .filter(s -> s.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new SandwichShopNotFoundException("This sandwich shop does not exist"));
    }

    @Override
    public void addShop(SandwichShop sandwichShop) {
        shops.add(sandwichShop);
    }

    @Override
    public void deleteShop(SandwichShop sandwichShop) throws SandwichShopNotFoundException {
        if (shops.contains(sandwichShop)){
            shops.remove(sandwichShop);
        } else {
            throw new SandwichShopNotFoundException(sandwichShop.getName() + " was not found and could not be deleted.");
        }
    }

    // GETTERS AND SETTERS
    public List<SandwichShop> getShops() {
        return shops;
    }



}
