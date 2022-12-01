package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;
import java.util.Map;

public interface FinancialService {
    double calculateTotalPriceForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException;
    double calculateTotalPriceForSession(Session session) throws OrderNotFoundException;
    Map<Session, Double> calculatePricesPerSessionOnDate(LocalDate date) throws OrderNotFoundException;
    Map<Session, Double> calculatePricesPerSessionForPeriod(LocalDate start, LocalDate end) throws OrderNotFoundException;
    Map<Sandwich, Integer> getPopularityOfSandwichesByDates(LocalDate startDate, LocalDate endDate) throws OrderNotFoundException;
    OrderJPAService getOrderService();
    void setOrderService(OrderJPAService orderService);
    SandwichJPAService getSandwichService();
    void setSandwichService(SandwichJPAService sandwichService);
}
