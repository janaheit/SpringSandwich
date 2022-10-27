package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    public void addOrder(Order order);
    public void deleteOrder(Order order);

    public void createOrdersForEveryoneToday();
    public void createOrder(Person person);

    public void handleOrder(Order order, Boolean noSandwich);
    public void handleOrder(Order order, Boolean noSandwich, String remark);
    public void handleOrder(Order order, Sandwich sandwich, BreadType breadType, List<Options> options, String remark) throws IngredientNotAvailableException;

    public List<Order> findOrdersByDate(LocalDate date);
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);
    public List<Order> findOrdersBySession(Session session);
    public List<Order> findTodaysOrdersForPerson(Person person);
    public List<Order> findAllUnhandeledOrders();
    public List<Order> findAllUnfilledOrders();
    public List<Person> getAllPersonsFromListOfOrders(List<Order> orders);

    public void generateOrderFile();


}
