package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AbisFinancialService implements FinancialService {

    @Autowired OrderService orderService;

    @Override
    public double getTotalPriceForPeriod(LocalDate start, LocalDate end) {

        List<Order> ordersForPeriod = orderService.findOrdersByDates(start, end);

        return getTotalPriceFromListOfOrders(ordersForPeriod);
    }

    @Override
    public double getTotalPriceForSession(Session session) {
        List<Order> ordersForSession = orderService.findOrdersBySession(session);

        return getTotalPriceFromListOfOrders(ordersForSession);
    }

    private double getTotalPriceFromListOfOrders(List<Order> orders){
        return orders.stream()
                .map(order -> order.getSandwich().getPrice())
                .mapToDouble(price -> price.doubleValue())
                .sum();
    }

    @Override
    public double getPricesPerSessionOnDate(LocalDate date) {
        //Map<Session, List<Order>> ordersGroupedBySession = orderService.find
        return 0;
    }

    @Override
    public double getPricesPerSessionForPeriod(LocalDate start, LocalDate end) {
        return 0;
    }
}
