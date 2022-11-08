package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.NameModel;
import be.abis.sandwichordersystem.dto.OrderModel;
import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SandwichShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class ManagementController {

    @Autowired
    OrderService orderService;
    @Autowired
    SandwichShopService sandwichShopService;


    // only for testing purposes
    @GetMapping("startup")
    public void startDay() throws SandwichShopNotFoundException {
        // set Vleugels as currentSandwichShop
        orderService.setTodaysSandwichShop(sandwichShopService.getSandwichShopRepository().getShops().get(0));
        orderService.createOrdersForEveryoneToday();
    }


    @GetMapping()
    public List<Order> getAllOrdersToday() {
        return orderService.findOrdersByDate(LocalDate.now());
    }





}
