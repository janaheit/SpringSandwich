package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SandwichShopServiceImpl implements SandwichShopService {
    @Override
    public SandwichShopRepository getSandwichShopRepository() {
        return null;
    }

    @Override
    public List<Sandwich> getSandwiches(SandwichShop sandwichShop) {
        return null;
    }

    @Override
    public List<Options> getOptions(SandwichShop sandwichShop) {
        return null;
    }

    @Override
    public List<BreadType> getBreadTypes(SandwichShop sandwichShop) {
        return null;
    }

    @Override
    public boolean checkSandwich(Sandwich sandwich, SandwichShop sandwichShop) {
        return false;
    }

    @Override
    public List<Options> checkOptions(List<Options> options, SandwichShop sandwichShop) {
        return null;
    }

    @Override
    public boolean checkBreadType(BreadType breadType, SandwichShop sandwichShop) {
        return false;
    }

    @Override
    public void addSandwich(Sandwich sandwich, SandwichShop sandwichShop) {

    }

    @Override
    public void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException {
        // delete first from SandwichShop
        // -> if not found DO NOT delete from SandwichREpository
    }

    @Override
    public Sandwich findSandwichByName(String name) {
        return null;
    }

    @Override
    public Sandwich findSandwichById(int id) {
        return null;
    }
}
