package be.abis.sandwichordersystem.model;



import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="orders")
public class Order implements Comparable<Order> {

    @SequenceGenerator(name="orderGen", sequenceName = "orders_oid_seq", allocationSize = 1)

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderGen")
    @Column(name="oid")
    private int orderNum;
    @Column(name="ostatus")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.UNFILLED;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "o_pid")
    private Person person;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "o_sandid")
    private Sandwich sandwich;
    @Column(name="obread")
    @Enumerated(EnumType.STRING)
    private BreadType breadType;
    @Column(name="oremark")
    private String remark;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ORDEROPTIONS", joinColumns = @JoinColumn(name="OO_OID"))
    @Column(name="option")
    private List<Options> options;
    //@JsonIgnore
    // You actually want to retrieve the session data in the api, just not all the students, but at least the name of the session
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="o_sid")
    private Session session;
    /* Deleted for now and added the seperate fields from the database
    @JsonIgnore
    private DayOrder dayOrder;
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "O_SHOP")
    private SandwichShop sandwichShop;
    @Column(name = "ODATE")
    private LocalDate date;

    //@JsonIgnore
    @Column(name = "OPRICE")
    private double price;

    // TODO implement  amount in functions
    @Column(name="OAMOUNT")
    private int amount;

    // Constructors
    public Order() {
    }

    public Order(Person person, DayOrder dayOrder) {
        this();
        this.person = person;
        this.date = dayOrder.getDate();
        this.sandwichShop = dayOrder.getCurrentSandwichShop();
    }

    // Getters and setters

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
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
        this.price = sandwich.getPrice();
        //TODO later with proper prices, we can add prices of sandwich and options?
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
        return new DayOrder(this.sandwichShop, this.date);
    }

    public void setDayOrder(DayOrder dayOrder) {
        this.date = dayOrder.getDate();
        this.sandwichShop = dayOrder.getCurrentSandwichShop();
    }

    public int getOrderNum() {
        return orderNum;
    }

    public SandwichShop getSandwichShop() {
        return sandwichShop;
    }

    public void setSandwichShop(SandwichShop sandwichShop) {
        this.sandwichShop = sandwichShop;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return Double.compare(order.price, price) == 0 && amount == order.amount && orderStatus == order.orderStatus && Objects.equals(person, order.person) && Objects.equals(sandwich, order.sandwich) && breadType == order.breadType && Objects.equals(remark, order.remark) && Objects.equals(options, order.options) && Objects.equals(session, order.session) && Objects.equals(sandwichShop, order.sandwichShop) && Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus, person, sandwich, breadType, remark, options, session, sandwichShop, date, price, amount);
    }
}
