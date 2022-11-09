package be.abis.sandwichordersystem.model;



import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

public class Order implements Comparable<Order> {

    // Attributes
    private static int orderCounter=0;

    private int orderNum;
    private OrderStatus orderStatus = OrderStatus.UNFILLED;
    private Person person;
    private Sandwich sandwich;
    private BreadType breadType;
    private String remark;
    private List<Options> options;
    //@JsonIgnore
    // You actually want to retrieve the session data in the api, just not all the students, but at least the name of the session
    private Session session;
    @JsonIgnore
    private DayOrder dayOrder;

    // Constructors
    public Order() {
        this.orderNum = orderCounter;
        orderCounter += 1;
    }

    public Order(Person person, DayOrder dayOrder) {
        this();
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

    public int getOrderNum() {
        return orderNum;
    }

    // Compares
    // TODO No one knows why this is here!
    @Override
    public int compareTo(Order o) {
        return 0;
    }

    // Equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNum == order.orderNum && person.equals(order.person) && Objects.equals(sandwich, order.sandwich) && Objects.equals(dayOrder, order.dayOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNum, person, sandwich, dayOrder);
    }
}
