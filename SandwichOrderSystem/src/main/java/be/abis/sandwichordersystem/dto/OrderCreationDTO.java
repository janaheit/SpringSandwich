package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;

import java.util.List;

public class OrderCreationDTO {

    private boolean noSandwich;
    private String personFullName;
    private int sandwichID;
    private int orderId;

    private BreadType breadType;
    private String remark;
    private List<Options> options;

    public OrderCreationDTO() {
    }

    public boolean getNoSandwich() {
        return noSandwich;
    }

    public void setNoSandwich(boolean noSandwich) {
        this.noSandwich = noSandwich;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public void setPersonFullName(String name) {
        this.personFullName = name;
    }

    public int getSandwichID() {
        return sandwichID;
    }

    public void setSandwichID(int sandwichID) {
        this.sandwichID = sandwichID;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}


