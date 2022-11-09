package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.model.Session;

import java.time.LocalDate;

public interface FinancialService {
    double getTotalPriceForPeriod(LocalDate start, LocalDate end);
    double getTotalPriceForSession(Session session);
    double getPricesPerSessionOnDate(LocalDate date);
    double getPricesPerSessionForPeriod(LocalDate start, LocalDate end);
}
