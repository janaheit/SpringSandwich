package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.factory.SandwichFactory;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListSandwichRepository implements SandwichRepository {

    @Autowired SandwichFactory sandwichFactory;
    private SandwichShop shop;
    private List<Sandwich> sandwiches = new ArrayList<>();

    public ListSandwichRepository() {

    }

    @PostConstruct
    public void init(){
        sandwiches = sandwichFactory.createSandwiches();
    }

    // BUSINESS METHODS
    public Sandwich findSandwichByID(int id) throws SandwichNotFoundException {
        return this.sandwiches.stream()
                .filter(s -> s.getSandwichNr()==id)
                .findAny()
                .orElseThrow(() -> new SandwichNotFoundException("This sandwich does not exist"));
    }

    // GETTERS AND SETTERS
    public List<Sandwich> getSandwiches() {
        return this.sandwiches;
    }

    @Override
    public void addSandwich(Sandwich sandwich) {
        sandwiches.add(sandwich);
    }

    @Override
    public void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException {
        if(sandwiches.contains(sandwich)) {
            sandwiches.remove(sandwich);
        } else {
            throw new SandwichNotFoundException("Sandwich cannot be deleten since it doesnt exist");
        }
    }
}
