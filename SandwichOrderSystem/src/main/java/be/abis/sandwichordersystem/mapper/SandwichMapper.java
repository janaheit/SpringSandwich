package be.abis.sandwichordersystem.mapper;

import be.abis.sandwichordersystem.dto.SandwichCreationDTO;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;

public class SandwichMapper {

    public static Sandwich toSandwich(SandwichCreationDTO s, SandwichShop shop){
        Sandwich sandwich = new Sandwich(s.getName(), s.getCategory(), s.getDescription());
        sandwich.setShop(shop);
        sandwich.setPrice(s.getPrice());
        return sandwich;
    }
}
