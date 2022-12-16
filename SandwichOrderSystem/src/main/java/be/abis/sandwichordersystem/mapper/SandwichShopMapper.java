package be.abis.sandwichordersystem.mapper;

import be.abis.sandwichordersystem.dto.SandwichCreationDTO;
import be.abis.sandwichordersystem.dto.SandwichDTO;
import be.abis.sandwichordersystem.dto.SandwichShopDTO;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;

public class SandwichShopMapper {

    public static SandwichShopDTO toDTO(SandwichShop sandwichShop){
        SandwichShopDTO s = new SandwichShopDTO();
        s.setName(sandwichShop.getName());
        s.setId(sandwichShop.getSandwichShopID());
        return s;
    }
}
