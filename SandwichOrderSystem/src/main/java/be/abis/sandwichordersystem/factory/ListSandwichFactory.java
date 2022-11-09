package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.Sandwich;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ListSandwichFactory implements SandwichFactory {

    public ListSandwichFactory() {
    }

    public List<Sandwich> createSandwiches() {
        List<Sandwich> sandwiches = new ArrayList<>();

        // ---------------------Vleugels------------30-sandwiches----------------
        sandwiches.add(new Sandwich("Americain", "Vlees"));
        sandwiches.add(new Sandwich("Boulette", "Vlees"));
        sandwiches.add(new Sandwich("Pastrami", "Vlees"));
        sandwiches.add(new Sandwich("Hesp + kaas", "Vlees"));
        sandwiches.add( new Sandwich("Martino", "Vlees"));
        sandwiches.add(new Sandwich("Vleessalade", "Vlees"));
        sandwiches.add(new Sandwich("Hesp", "Vlees"));
        sandwiches.add(new Sandwich("Parmaham", "Vlees"));

        sandwiches.add(new Sandwich("Kipfilet", "Kip"));
        sandwiches.add(new Sandwich("Spicy kip curry", "Kip"));
        sandwiches.add(new Sandwich("Kip Hawai", "Kip"));

        sandwiches.add(new Sandwich("Tonijnsalade", "Vis"));
        sandwiches.add(new Sandwich("Tonijnsalade pikant", "Vis"));
        sandwiches.add(new Sandwich("Zalm (gemarineerd) + philadephia", "Vis"));

        sandwiches.add(new Sandwich("Kaas", "Veggie"));
        sandwiches.add(new Sandwich("Tomaat + mozzarella + pesto", "Veggie"));
        sandwiches.add(new Sandwich("Eiersalade", "Veggie"));
        sandwiches.add(new Sandwich("Brie", "Veggie"));
        sandwiches.add(new Sandwich("Feta", "Veggie"));
        sandwiches.add(new Sandwich("Geitenkaas", "Veggie"));

        sandwiches.add(new Sandwich("Wortelspread + sesam + tuinkers", "Vegan"));
        sandwiches.add(new Sandwich("Hummus", "Vegan"));
        sandwiches.add( new Sandwich("Vegan mayo", "Vegan"));
        sandwiches.add( new Sandwich("Avocadospread", "Vegan"));

        sandwiches.add(new Sandwich("Carolina", "Specials"));
        sandwiches.add(new Sandwich("Parmigiano", "Specials"));
        sandwiches.add(new Sandwich("Kip cocktail", "Specials"));
        sandwiches.add(new Sandwich("BBQ Chicken", "Specials"));
        sandwiches.add(new Sandwich("Flying Brie", "Specials"));
        sandwiches.add(new Sandwich("Provence", "Specials"));

        // ---------------------Pinky's---------31-end-------------------
        sandwiches.add(new Sandwich("Hesp", "Vlees"));
        sandwiches.add(new Sandwich("Rosbief", "Vlees"));
        sandwiches.add(new Sandwich("Gebraad", "Vlees"));
        sandwiches.add(new Sandwich("Gerookte nootham", "Vlees"));
        sandwiches.add(new Sandwich("Parma ham", "Vlees"));
        sandwiches.add(new Sandwich("Salami", "Vlees"));
        sandwiches.add(new Sandwich("Américain préparé", "Vlees"));
        sandwiches.add(new Sandwich("Gemengd gehakt", "Vlees"));
        sandwiches.add(new Sandwich("Martino", "Vlees"));
        sandwiches.add(new Sandwich("Kip curry", "Vlees"));
        sandwiches.add(new Sandwich("Kip zigeuner", "Vlees"));
        sandwiches.add(new Sandwich("Kipsla", "Vlees"));
        sandwiches.add(new Sandwich("Weense sla", "Vlees"));
        sandwiches.add(new Sandwich("Vleessla", "Vlees"));

        sandwiches.add(new Sandwich("Hollandse kaas", "Kaas"));
        sandwiches.add(new Sandwich("Brie", "Kaas"));
        sandwiches.add(new Sandwich("Kaassla", "Kaas"));

        sandwiches.add(new Sandwich("Tomaat garnaal", "Vis"));
        sandwiches.add(new Sandwich("Garnaalsla", "Vis"));
        sandwiches.add(new Sandwich("Tonijnsla", "Vis"));
        sandwiches.add(new Sandwich("Pikante tonijnsla", "Vis"));
        sandwiches.add(new Sandwich("Zalmsla","Vis"));
        sandwiches.add(new Sandwich("Gerookte zalmsla", "Vis"));
        sandwiches.add(new Sandwich("Krabsla", "Vis"));
        sandwiches.add(new Sandwich("Eiersla", "Vis"));

        sandwiches.add(new Sandwich("Smos", "Specialiteiten", "Club met kaas en hesp"));
        sandwiches.add(new Sandwich("Pinky", "Specialiteiten", "Sla, tomaat, ei, haringfilet, tartaar"));
        sandwiches.add(new Sandwich("Mozzarella", "Specialiteiten", "Olijfolie, zout, peper, basilicum, tomaat"));
        sandwiches.add(new Sandwich("Special", "Specialiteiten", "Sla, tomaat, ei, kipfilet, saus"));
        sandwiches.add(new Sandwich("Florida", "Specialiteiten", "Sla, ananas, kipfilet, cocktailsaus"));
        sandwiches.add(new Sandwich("Florida", "Specialiteiten", "Sla, ananas, kipfilet, cocktailsaus"));

        addPrices(sandwiches);

        return sandwiches;
    }

    private void addPrices(List<Sandwich> sandwiches){
        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Vlees"))
                .forEach(s-> s.setPrice(4.0));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Vis"))
                .forEach(s-> s.setPrice(4.5));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Specials") || s.getCategory().equals("Specialiteiten"))
                .forEach(s-> s.setPrice(5.0));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Vegan"))
                .forEach(s-> s.setPrice(3.5));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Veggie"))
                .forEach(s-> s.setPrice(3.75));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Kip"))
                .forEach(s-> s.setPrice(4.25));

        sandwiches.stream()
                .filter(s -> s.getCategory().equals("Kaas"))
                .forEach(s-> s.setPrice(4.1));
    }
}
