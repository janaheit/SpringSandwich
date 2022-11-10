package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.exception.SessionNotFoundException;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.service.FinancialService;
import be.abis.sandwichordersystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
}
