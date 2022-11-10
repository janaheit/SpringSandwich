package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.service.FinancialService;
import be.abis.sandwichordersystem.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/prices")
public class FinancialController {

    @Autowired
    FinancialService financialService;

    @GetMapping("total/period")
    public double getTotalPriceForPeriod(@RequestParam("start") String start, @RequestParam("end") String end) throws OrderNotFoundException {
        LocalDate startDate = DateUtil.parseStringToDate(start);
        LocalDate endDate = DateUtil.parseStringToDate(end);

        return financialService.calculateTotalPriceForPeriod(startDate, endDate);
    }

}
