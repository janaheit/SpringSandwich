package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AbisFinancialService implements FinancialService {

    @Autowired OrderService orderService;

    @Override
    public double getTotalPriceForPeriod(LocalDate start, LocalDate end) {
        return 0;
    }

    @Override
    public double getTotalPriceForSession(Session session) {
        return 0;
    }

    @Override
    public double getPricesPerSessionOnDate(LocalDate date) {
        return 0;
    }

    @Override
    public double getPricesPerSessionForPeriod(LocalDate start, LocalDate end) {
        return 0;
    }
}
