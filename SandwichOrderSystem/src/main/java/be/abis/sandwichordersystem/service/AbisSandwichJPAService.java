package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichJPARepository;
import be.abis.sandwichordersystem.repository.SandwichShopJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbisSandwichJPAService implements SandwichJPAService {

    @Autowired
    SandwichJPARepository sandwichRepository;
    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    @Override
    public List<Sandwich> getSandwichesForShop(int shopID) {
        return sandwichRepository.findSandwichesByShopId(shopID);
    }

    // TODO : what if the DB has a faulty value? a null will be inserted in the list
    @Override
    public List<Options> getOptionsForShop(int shopID) {
        List<String> optionStrings = sandwichShopRepository.findOptionsForShopID(shopID);

        return optionStrings.stream()
                .map(Options::fromStringToOption)
                .collect(Collectors.toList());
    }

    // TODO : what if the DB has a faulty value? a null will be inserted in the list
    @Override
    public List<BreadType> getBreadTypesForShop(int shopID) {
        List<String> breadStrings = sandwichShopRepository.findBreadTypesForShopID(shopID);

        return breadStrings.stream()
                .map(BreadType::fromStringToBreadType)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfSandwichInShop(int sandwichID, int shopID) {
        int shopIDForSandwich = sandwichRepository.getShopIDForSandwich(sandwichID);
        return (shopIDForSandwich == shopID);
    }

    // Returns a list of options that are indeed present in the Sandwich Shop
    // (all the ones that are NOT in the shop will be discarded)
    @Override
    public List<Options> checkIfOptionsInShop(List<Options> options, int shopID) {
        List<Options> realOptions = sandwichShopRepository.findOptionsForShopID(shopID).stream()
                .map(Options::fromStringToOption)
                .collect(Collectors.toList());

        List<Options> outputList = new ArrayList<>();
        for (Options option : options) {
            if(realOptions.contains(option)) {
                outputList.add(option);
            }
        }
        return outputList;
    }

    @Override
    public boolean checkIfBreadTypeInShop(BreadType breadType, int shopID) {
        List<BreadType> breadtypes = sandwichShopRepository.findBreadTypesForShopID(shopID).stream()
                .map(BreadType::fromStringToBreadType)
                .collect(Collectors.toList());
        return breadtypes.contains(breadType);
    }

    @Override
    public void addSandwichToShop(Sandwich sandwich, int shopID) throws SandwichAlreadyExistsException {
        Sandwich existing = sandwichRepository.findSandwichByNameAndCategoryAndShop(sandwich.getName(),
                sandwich.getCategory(), sandwich.getSandwichID());

        if (existing == null) {
            sandwichRepository.save(sandwich);
        } else {
            throw new SandwichAlreadyExistsException("This sandwich already exists, won't be added.");
        }
    }

    @Override
    public void deleteSandwichByID(int sandwichID) throws SandwichNotFoundException {
        try {
            sandwichRepository.deleteById(sandwichID);
        } catch (EmptyResultDataAccessException e){
            throw new SandwichNotFoundException("This sandwich was not found.");
        }
    }

    @Override
    public Sandwich findSandwichByName(String name) throws SandwichNotFoundException {
        Sandwich s = sandwichRepository.findByName(name);
        if (s==null) throw new SandwichNotFoundException("Sandwich could not be found.");
        return s;
    }

    @Override
    public Sandwich findSandwichById(int id) throws SandwichNotFoundException {
        Sandwich s = sandwichRepository.findById(id);
        if (s==null) throw new SandwichNotFoundException("Sandwich could not be found.");
        return s;
    }

    // TODO what is important to give back here? Shop with breadtypes, options and sandwiches or are neither of these important?
    @Transactional
    @Override
    public SandwichShop findShopForSandwich(int sandwichID) throws SandwichShopNotFoundException {
        int id = sandwichRepository.getShopIDForSandwich(sandwichID);
        Object[] shopList = sandwichShopRepository.findObjectById(id);
        if (shopList==null) throw new SandwichShopNotFoundException("This shop was not found");
        SandwichShop shop = new SandwichShop(shopList[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList[0].toString()));
        return shop;
    }

    @Override
    public SandwichShop findShopByName(String name) throws SandwichShopNotFoundException {
        Object[] shopList = sandwichShopRepository.findByName(name);
        if (shopList==null) throw new SandwichShopNotFoundException("This shop was not found");

        SandwichShop shop = new SandwichShop(shopList[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList[0].toString()));
        return shop;
    }
}
