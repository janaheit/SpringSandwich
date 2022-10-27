package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichRepository;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SandwichShopServiceImpl implements SandwichShopService {

    @Autowired
    SandwichShopRepository sandwichShopRepository;

    @Autowired
    SandwichRepository sandwichRepository;

    @Override
    public SandwichShopRepository getSandwichShopRepository() {
        return sandwichShopRepository;
    }

    @Override
    public List<Sandwich> getSandwiches(SandwichShop sandwichShop) {
        return sandwichShop.getSandwiches();
    }

    @Override
    public List<Options> getOptions(SandwichShop sandwichShop) {
        return sandwichShop.getOptions();
    }

    @Override
    public List<BreadType> getBreadTypes(SandwichShop sandwichShop) {
        return sandwichShop.getBreadTypes();
    }

    @Override
    public boolean checkSandwich(Sandwich sandwich, SandwichShop sandwichShop) {
        if (sandwichShop.getSandwiches().contains(sandwich)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Options> checkOptions(List<Options> options, SandwichShop sandwichShop) {
        List<Options> outputList = new ArrayList<>();
        for (Options option : options) {
            if(sandwichShop.getOptions().contains(option)) {
                outputList.add(option);
            }
        }
        return outputList;
    }

    @Override
    public boolean checkBreadType(BreadType breadType, SandwichShop sandwichShop) {
        if (sandwichShop.getBreadTypes().contains(breadType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addSandwich(Sandwich sandwich, SandwichShop sandwichShop) {
        if (!sandwichRepository.getSandwiches().contains(sandwich)) {
            sandwichRepository.addSandwich(sandwich);
        }
        if (!sandwichShop.getSandwiches().contains(sandwich)) {
            sandwichShop.addSandwich(sandwich);
        }
    }

    @Override
    public SandwichShop findShopForSandwich(Sandwich sandwich) throws SandwichShopNotFoundException {

        return sandwichShopRepository.getShops().stream().filter(sandwichShop -> sandwichShop.getSandwiches().contains(sandwich)).findAny().orElseThrow(() -> new SandwichShopNotFoundException("No shop seems to have this sandwich"));
    }

    @Override
    public void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException {
        // delete first from SandwichShop
        SandwichShop foundSandwichShop;
        try {
            foundSandwichShop = findShopForSandwich(sandwich);
            foundSandwichShop.deleteSandwich(sandwich);
            sandwichRepository.deleteSandwich(sandwich);
        } catch (SandwichShopNotFoundException e) {
            throw new SandwichNotFoundException("Sandwich not found in any shop");
        }

        // -> if not found DO NOT delete from SandwichREpository
    }

    @Override
    public Sandwich findSandwichByName(String name) throws SandwichNotFoundException {
        return sandwichRepository.findSandwichByName(name);
    }

    @Override
    public Sandwich findSandwichById(int id) throws SandwichNotFoundException {
        return sandwichRepository.findSandwichByID(id);
    }
}
