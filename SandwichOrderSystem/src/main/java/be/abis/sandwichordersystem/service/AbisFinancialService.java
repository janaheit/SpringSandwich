package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AbisFinancialService implements FinancialService {

    @Autowired OrderService orderService;

    public AbisFinancialService() {
    }

    @Override
    public double calculateTotalPriceForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException {

        List<Order> ordersForPeriod = orderService.findAllClosedOrdersForDates(start, end);

        return getTotalPriceFromListOfOrders(ordersForPeriod);
    }

    @Override
    public double calculateTotalPriceForSession(Session session) throws OrderNotFoundException {
        List<Order> ordersForSession = orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, session);

        return getTotalPriceFromListOfOrders(ordersForSession);
    }

    private double getTotalPriceFromListOfOrders(List<Order> orders){
        return orders.stream()
                .map(order -> order.getPrice())
                .mapToDouble(price -> price.doubleValue())
                .sum();
    }

    @Override
    public Map<Session, Double> calculatePricesPerSessionOnDate(LocalDate date) throws OrderNotFoundException {
        return calculatePricesPerSessionForPeriod(date, date);
    }

    @Override
    public Map<Session, Double> calculatePricesPerSessionForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException {
        List<Order> ordersForDate = orderService.findAllClosedOrdersForDates(start, end);
        Map<Session, List<Order>> groupedBySession = ordersForDate.stream()
                .collect(Collectors.groupingBy(Order::getSession));

        Map<Session, Double> pricesPerSession = new HashMap<>();

        for(Map.Entry<Session, List<Order>> entry: groupedBySession.entrySet()){
            double priceForSession = entry.getValue().stream().collect(Collectors.summingDouble(Order::getPrice));
            pricesPerSession.put(entry.getKey(), priceForSession);
        }

        return pricesPerSession;
    }

    // getter and setter


    @Override
    public OrderService getOrderService() {
        return orderService;
    }

    @Override
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
