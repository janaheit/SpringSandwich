package be.abis.sandwichordersystem.dto;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.model.Session;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private int orderID;
    private OrderStatus orderStatus;
    private int personID;
    private String personName;
    private int sandwichID;
    private String sandwichName;
    private String sandwichCategory;
    private BreadType breadType;
    private String remark;
    private List<Options> options;
    private int sessionID;
    private String sessionName;
    private int sandwichShopID;
    private String sandwichShopName;
    private LocalDate date;

    public OrderDTO() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getSandwichCategory() {
        return sandwichCategory;
    }

    public void setSandwichCategory(String sandwichCategory) {
        this.sandwichCategory = sandwichCategory;
    }

    public int getSandwichShopID() {
        return sandwichShopID;
    }

    public void setSandwichShopID(int sandwichShopID) {
        this.sandwichShopID = sandwichShopID;
    }

    public String getSandwichShopName() {
        return sandwichShopName;
    }

    public void setSandwichShopName(String sandwichShopName) {
        this.sandwichShopName = sandwichShopName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getSandwichID() {
        return sandwichID;
    }

    public void setSandwichID(int sandwichID) {
        this.sandwichID = sandwichID;
    }

    public String getSandwichName() {
        return sandwichName;
    }

    public void setSandwichName(String sandwichName) {
        this.sandwichName = sandwichName;
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

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
