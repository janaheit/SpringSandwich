package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ListOrderRepository implements OrderRepository {

    // Attributes
    private List<Order> orders;

    // Method implementations
    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public void deleteOrder(Order order) {
        if(orders.contains(order)) {
            orders.remove(order);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orders;
    }

    @Override
    public List<Order> findOrdersByDate(LocalDate date) {
        List<Order> output = this.orders.stream().filter(order -> order.getDayOrder().getDate().isEqual(date)).collect(Collectors.toList());
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
}
