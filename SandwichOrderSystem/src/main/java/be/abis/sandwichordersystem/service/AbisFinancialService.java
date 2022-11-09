package be.abis.sandwichordersystem.service;

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
    public double getTotalPriceForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException {

        List<Order> ordersForPeriod = orderService.findAllClosedOrdersForDates(start, end);

        return getTotalPriceFromListOfOrders(ordersForPeriod);
    }

    @Override
    public double getTotalPriceForSession(Session session) {
        List<Order> ordersForSession = orderService.findOrdersBySession(session);

        return getTotalPriceFromListOfOrders(ordersForSession);
    }

    private double getTotalPriceFromListOfOrders(List<Order> orders){
        return orders.stream()
                .map(order -> order.getPrice())
                .mapToDouble(price -> price.doubleValue())
                .sum();
    }

    @Override
    public Map<Session, Double> getPricesPerSessionOnDate(LocalDate date) throws OrderNotFoundException {
        return getPricesPerSessionForPeriod(date, date);
    }

    @Override
    public Map<Session, Double> getPricesPerSessionForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException {
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
