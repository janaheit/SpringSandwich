package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;


import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {

    public void addOrder(Order order);
    public void deleteOrder(Order order);
    public List<Order> getAllOrders();
    public List<Order> findOrdersByDate(LocalDate date);
    public List<Order> findOrdersBySession(Session session);
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);

}
