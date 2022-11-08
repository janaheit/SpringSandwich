package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Sandwich;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public class OrderModel {

    private boolean noSandwich;
    private Person person;
    private Sandwich sandwich;
    private BreadType breadType;
    private String remark;
    private List<Options> options;

    public OrderModel() {
    }

    public boolean getNoSandwich() {
        return noSandwich;
    }

    public void setNoSandwich(boolean noSandwich) {
        this.noSandwich = noSandwich;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Sandwich getSandwich() {
        return sandwich;
    }

    public void setSandwich(Sandwich sandwich) {
        this.sandwich = sandwich;
    }

    public BreadType getBreadType() {
        return breadType;
    }

    public void setBreadType(BreadType breadType) {
        this.breadType = breadType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}


