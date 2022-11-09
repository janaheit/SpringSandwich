package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Repository
public class ListOrderRepository implements OrderRepository {

    // Attributes
    private List<Order> orders;

    public ListOrderRepository() {
        this.orders = new ArrayList<>();
    }

    // Method implementations
    @Override
    public boolean addOrder(Order order) {
        return orders.add(order);
    }

    @Override
    public boolean deleteOrder(Order order) throws OrderNotFoundException {
        if(orders.contains(order)) {
            return orders.remove(order);
        } else {
            throw new OrderNotFoundException("This order was not found and therefore could not be deleted.");
        }
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public List<Order> findOrdersByDate(LocalDate date) {
        List<Order> output = this.orders.stream().filter(order -> order.getDayOrder().getDate().isEqual(date)).collect(Collectors.toList());
        return output;
    }

    public Map<Session, List<Order>> findOrdersByDateBySession(LocalDate date){
        Map<Session, List<Order>> output = this.orders.stream()
                .filter(order -> order.getDayOrder().getDate().isEqual(date))
                .collect(groupingBy(Order::getSession));
        return output;
    }

    @Override
    public List<Order> findOrdersBySession(Session session) {
        List<Order> output = this.orders.stream().filter(order -> order.getSession().equals(session)).collect(Collectors.toList());
        return output;
    }

    @Override
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate) {
        List<Order> output = this.orders.stream().filter(order -> order.getDayOrder().getDate().isAfter(startDate.minusDays(1))).filter(order -> order.getDayOrder().getDate().isBefore(endDate.plusDays(1))).collect(Collectors.toList());
        return output;
    }

    @Override
    public List<Order> findOrdersByStatusAndDates(OrderStatus status, LocalDate startDate, LocalDate endDate) throws OrderNotFoundException {
        List<Order> output = this.orders.stream().filter(order -> order.getDayOrder().getDate().isAfter(startDate.minusDays(1))).filter(order -> order.getDayOrder().getDate().isBefore(endDate.plusDays(1))).filter(order -> order.getOrderStatus().equals(status)).collect(Collectors.toList());
        if (output.size()==0) {
            throw new OrderNotFoundException("No orders where found with status: " + status + ", between dates " + startDate + " and " + endDate);
        }
        return output;
    }
}
