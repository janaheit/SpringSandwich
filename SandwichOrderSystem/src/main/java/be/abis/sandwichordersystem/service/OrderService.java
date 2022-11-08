package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    boolean addOrder(Order order);
    boolean deleteOrder(Order order) throws OrderNotFoundException;
    void createOrdersForEveryoneToday() throws SandwichShopNotFoundException;
    Order createOrder(Person person);

    void handleOrder(Order order, Boolean noSandwich);
    void handleOrder(Order order, Boolean noSandwich, String remark);
    void handleOrder(Order order, Sandwich sandwich, BreadType breadType, List<Options> options, String remark) throws IngredientNotAvailableException;

    List<Order> findOrdersByDate(LocalDate date);
    List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);
    List<Order> findOrdersBySession(Session session);
    List<Order> findTodaysOrdersForPerson(Person person);
    List<Order> findAllUnhandeledOrders();
    List<Order> findAllUnfilledOrders();
    List<Person> getAllPersonsFromListOfOrders(List<Order> orders);

    void generateOrderFile() throws IOException;

    void setTodaysSandwichShop(SandwichShop sandwichShop);

    SandwichShop getTodaysSandwichShop();

    OrderRepository getOrderRepository();
    void setOrderRepository(OrderRepository orderRepository);

    Order findTodaysOrderByName(String name) throws PersonNotFoundException;


}
