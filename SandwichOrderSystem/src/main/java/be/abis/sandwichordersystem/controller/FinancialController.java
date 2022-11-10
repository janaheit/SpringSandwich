package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.service.FinancialService;
import be.abis.sandwichordersystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/prices")
public class FinancialController {

    @Autowired
    FinancialService financialService;
    @Autowired
    SessionService sessionService;

    @GetMapping("total/period")
    public double getTotalPriceForPeriod(@RequestParam("start") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate startDate,
                                         @RequestParam("end") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate endDate) throws OrderNotFoundException {

        return financialService.calculateTotalPriceForPeriod(startDate, endDate);
    }

    @GetMapping("total/session/{id}")
    public double getTotalPriceForSession(@PathVariable int id) throws SessionNotFoundException, OrderNotFoundException {
        Session s = sessionService.findSessionByID(id);
        return financialService.calculateTotalPriceForSession(s);
    }

    @GetMapping("period")
    public Map<Session, Double> getPricesPerSessionForPeriod(@RequestParam("start") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate startDate,
                                                             @RequestParam("end") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate endDate) throws OrderNotFoundException {
        return financialService.calculatePricesPerSessionForPeriod(startDate, endDate);
    }

    @GetMapping("query")
    public Map<Session, Double> getPricesPerSessionOnDate(@RequestParam("date") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate date) throws OrderNotFoundException {

        return financialService.calculatePricesPerSessionOnDate(date);
    }

    @GetMapping("sandwich-ranking/all/period")
    public Map<Sandwich, Integer> getPopularityForPeriod(@RequestParam("start") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate startDate,
                                                         @RequestParam("end") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate endDate) throws OrderNotFoundException {
        return financialService.getPopularityOfSandwichesByDates(startDate, endDate);
    }

    @GetMapping("sandwich-ranking/top-{num}/period")
    public Map<Sandwich, Integer> getTopPopularityForPeriod(@PathVariable int num, @RequestParam("start") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate startDate,
                                                         @RequestParam("end") @DateTimeFormat(pattern = "d-M-yyyy") LocalDate endDate) throws OrderNotFoundException {
        return financialService.getPopularityOfSandwichesByDates(startDate, endDate).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(num)
                .collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


}
