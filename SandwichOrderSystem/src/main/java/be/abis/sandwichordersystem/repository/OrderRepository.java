package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;


import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {

    public boolean addOrder(Order order);
    public boolean deleteOrder(Order order) throws OrderNotFoundException;
    public List<Order> getOrders();
    public List<Order> findOrdersByDate(LocalDate date);
    public List<Order> findOrdersBySession(Session session);
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);
    public List<Order> findOrdersByStatusAndDates(OrderStatus status, LocalDate startDate, LocalDate endDate) throws OrderNotFoundException;

    public List<Order> findOrdersByPersonAndDates(Person person, LocalDate startDate, LocalDate endDate) throws OrderNotFoundException;
}
