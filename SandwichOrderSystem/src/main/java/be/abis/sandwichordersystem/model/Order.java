package be.abis.sandwichordersystem.model;



import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;

import java.util.List;

public class Order implements Comparable<Order> {

    // Attributes
    private OrderStatus orderStatus = OrderStatus.UNFILLED;
    private Person person;
    private Sandwich sandwich;
    private BreadType breadType;
    private String remark;
    private List<Options> options;
    private Session session;
    private DayOrder dayOrder;

    // Constructor
    public Order(Person person, DayOrder dayOrder) {
        this.person = person;
        this.dayOrder = dayOrder;
    }

    // Getters and setters
    public void setNoSandwich(){
        this.orderStatus = OrderStatus.NOSANDWICH;
        this.sandwich = null;
        this.breadType = null;
        this.options = null;
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DayOrder getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(DayOrder dayOrder) {
        this.dayOrder = dayOrder;
    }

    // Compares
    @Override
    public int compareTo(Order o) {
        return 0;
    }
}
