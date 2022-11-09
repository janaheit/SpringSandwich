package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.Map;

public interface FinancialService {
    double getTotalPriceForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException;
    double getTotalPriceForSession(Session session);
    Map<Session, Double> getPricesPerSessionOnDate(LocalDate date) throws OrderNotFoundException;
    Map<Session, Double> getPricesPerSessionForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException;
    void setOrderService(OrderService orderService);
    OrderService getOrderService();
}
