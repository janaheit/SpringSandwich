package be.abis.sandwichordersystem.model;


import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Sandwichshops")
public class SandwichShop {

    @SequenceGenerator(name = "shopSeqGen", sequenceName = "sandwichshops_sandshopid_seq", allocationSize = 1)
    @Id
    @Column(name = "sandshopid")
    private int sandwichShopID;
    @Column(name = "shopname")
    private String name;
    /*
    @OneToMany(targetEntity = Sandwich.class, mappedBy = "shop", fetch=FetchType.LAZY)
    private List<Sandwich> sandwiches = new ArrayList<>();
    @ElementCollection(targetClass = BreadType.class, fetch = FetchType.LAZY)
    @CollectionTable(name="ssbreadtypes")
    @Column(name = "bread")
    private List<BreadType> breadTypes;
    @ElementCollection(targetClass = Options.class, fetch = FetchType.LAZY)
    @CollectionTable(name="ssoptions")
    @Column(name = "option")
    private List<Options> options;

     */

    // Constructors

    public SandwichShop() {
    }

    public SandwichShop(String name) {
        this();
        this.name = name;
    }
    /*
    public SandwichShop(String name, List<Sandwich> sandwiches, List<Options> options, List<BreadType> breadTypes) {
        this(name);
        this.sandwiches = sandwiches;
        this.options = options;
        this.breadTypes = breadTypes;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SandwichShop that = (SandwichShop) o;
        return sandwichShopID == that.sandwichShopID && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sandwichShopID, name);
    }

    // Getters and Setters


    @Override
    public String toString() {
        return "SandwichShop{" +
                "name='" + name +"}";
    }

    public int getSandwichShopID() {
        return sandwichShopID;
    }

    public void setSandwichShopID(int sandwichShopID) {
        this.sandwichShopID = sandwichShopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public List<Sandwich> getSandwiches() {
        return sandwiches;
    }

    public void setSandwiches(List<Sandwich> sandwiches) {
        this.sandwiches = sandwiches;
    }


    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public List<BreadType> getBreadTypes() {
        return breadTypes;
    }

    public void setBreadTypes(List<BreadType> breadTypes) {
        this.breadTypes = breadTypes;
    }

    // Business methods
    public void addSandwich(Sandwich sandwich){
        sandwiches.add(sandwich);
    }

    public void deleteSandwich(Sandwich sandwich) throws SandwichNotFoundException {
        if(sandwiches.contains(sandwich)) {
            sandwiches.remove(sandwich);
        } else {
            throw new SandwichNotFoundException("Cannot delete sandwich since it's not found in this sandwichshop");
        }
    }

     */
}
