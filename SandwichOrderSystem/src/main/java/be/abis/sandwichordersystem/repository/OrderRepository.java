package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;


import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {

    public void addOrder(Order order);
    public void deleteOrder(Order order) throws OrderNotFoundException;
    public List<Order> getOrders();
    public List<Order> findOrdersByDate(LocalDate date);
    public List<Order> findOrdersBySession(Session session);
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);
}
