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
        Integer shopIDForSandwich = sandwichRepository.getShopIDForSandwich(sandwichID);
        if (shopIDForSandwich == null) return false;
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

    // TODO should this throw an exception if shop does not exist?
    // currently returns false for non existing shop
    // same for check sandwich and check options!
    @Override
    public boolean checkIfBreadTypeInShop(BreadType breadType, int shopID) {
        List<BreadType> breadtypes = sandwichShopRepository.findBreadTypesForShopID(shopID).stream()
                .map(BreadType::fromStringToBreadType)
                .collect(Collectors.toList());
        return breadtypes.contains(breadType);
    }

    public Sandwich addSandwichToShopNew(Sandwich sandwich, int shopID) throws SandwichShopNotFoundException, SandwichAlreadyExistsException {
        // find shop here & add to sandwich
        List<Object[]> shopList = sandwichShopRepository.findObjectById(shopID);
        if (shopList.size()==0) throw new SandwichShopNotFoundException("This shop was not found");

        SandwichShop shop = new SandwichShop(shopList.get(0)[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList.get(0)[0].toString()));

        sandwich.setShop(shop);

        Sandwich existing = sandwichRepository.findSandwichByNameAndCategoryAndShop(sandwich.getName(),
                sandwich.getCategory(), sandwich.getShop().getSandwichShopID()); // shop not sandwich

        if (existing == null) {
            return sandwichRepository.save(sandwich);
        } else {
            throw new SandwichAlreadyExistsException("This sandwich already exists, won't be added.");
        }
    }

    // Sandwich needs name, price, category (can have shopID, but second param is used)
    @Transactional
    @Override
    public void addSandwichToShop(Sandwich sandwich, int shopID) throws SandwichAlreadyExistsException, SandwichShopNotFoundException {

        // if shop does not exist, DO NOT ADD
        if (sandwichShopRepository.findObjectById(shopID).size() == 0) {
            throw new SandwichShopNotFoundException("This sandwich-shop does not exist");
        }

        // check if sandwich already exists
        if (sandwichRepository.findSandwichByNameAndCategoryAndShop(sandwich.getName(),
                sandwich.getCategory(), shopID) == null) {
            if (sandwich.getDescription()==null) {
                sandwichRepository.insertSandwich(sandwich.getName(), sandwich.getPrice(), sandwich.getCategory(),
                        shopID);
            } else {
                sandwichRepository.insertSandwich(sandwich.getName(), sandwich.getPrice(), sandwich.getDescription(),
                        sandwich.getCategory(), shopID);
            }
        } else{
            throw new SandwichAlreadyExistsException("This sandwich already exists, so it will not be added again.");
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
        Integer id = sandwichRepository.getShopIDForSandwich(sandwichID);
        if (id==null) throw new SandwichShopNotFoundException("this sandwich does not exist");
        List<Object[]> shopList = sandwichShopRepository.findObjectById(id);
        if (shopList.size()==0) throw new SandwichShopNotFoundException("This shop was not found");
        SandwichShop shop = new SandwichShop(shopList.get(0)[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList.get(0)[0].toString()));

        return shop;
    }

    @Override
    public SandwichShop findShopByName(String name) throws SandwichShopNotFoundException {
        List<Object[]> shopList = sandwichShopRepository.findObjectByName(name);
        if (shopList.size()==0) throw new SandwichShopNotFoundException("This shop was not found");

        SandwichShop shop = new SandwichShop(shopList.get(0)[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList.get(0)[0].toString()));
        return shop;
    }

    @Override
    public SandwichShop findShopByID(int id) throws SandwichShopNotFoundException {
        List<Object[]> shopList = sandwichShopRepository.findObjectById(id);
        if (shopList.size()==0) throw new SandwichShopNotFoundException("This shop was not found");

        SandwichShop shop = new SandwichShop(shopList.get(0)[1].toString().trim());
        shop.setSandwichShopID(Integer.parseInt(shopList.get(0)[0].toString()));
        return shop;
    }
}
