package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListSandwichShopFactory implements SandwichShopFactory {

    @Autowired private SandwichRepository sandwichRepository;

    public ListSandwichShopFactory() {
    }

    public List<SandwichShop> createSandwichShops(){
        List<SandwichShop> shops = new ArrayList<>();
        addVleugels(shops);
        addPinkys(shops);
        return shops;
    }

    private List<SandwichShop> addVleugels(List<SandwichShop> shops) {
        List<Options> options = new ArrayList();
        options.add(Options.GRILLEDVEGGIES);
        options.add(Options.RAUWKOST);

        List<BreadType> breadTypes = new ArrayList();
        breadTypes.add(BreadType.GREY);
        breadTypes.add(BreadType.WHITE);

        List<Sandwich> vleugelsSandwiches = new ArrayList<>();
        // add the first 30 sandwiches (they belong to Vleugels)
        for (int s=1; s<=30; s++){
            try {
                vleugelsSandwiches.add(sandwichRepository.findSandwichByID(s));
            } catch (SandwichNotFoundException e) {
                System.out.println("Sandwich could not be added...");
                System.out.println(e.getMessage());
            }
        }

        //shops.add(new SandwichShop("Vleugels", vleugelsSandwiches, options, breadTypes));
        return shops;
    }

    private List<SandwichShop> addPinkys(List<SandwichShop> shops) {
        List<Options> options = new ArrayList();
        options.add(Options.CLUB);
        options.add(Options.NO_BUTTER);

        List<BreadType> breadTypes = new ArrayList();
        breadTypes.add(BreadType.GREY);
        breadTypes.add(BreadType.WHITE);

        List<Sandwich> pinkysSandwiches = new ArrayList<>();
        // add the sandwiches from 30 to end (they belong to Pinkys)
        for (int s=31; s<=sandwichRepository.getSandwiches().size(); s++){
            try {
                pinkysSandwiches.add(sandwichRepository.findSandwichByID(s));
            } catch (SandwichNotFoundException e) {
                System.out.println("Sandwich could not be added...");
                System.out.println(e.getMessage());
            }
        }

        //shops.add(new SandwichShop("Pinkys", pinkysSandwiches, options, breadTypes));
        return shops;
    }

}
