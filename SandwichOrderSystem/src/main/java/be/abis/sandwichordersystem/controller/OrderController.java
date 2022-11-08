package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.NameModel;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping()
    public Order findTodaysOrderByName(@RequestBody NameModel nameModel) throws PersonNotFoundException {
        return orderService.findTodaysOrderByName(nameModel.getName());
    }

    @GetMapping()
    public SandwichShop getTodaysSandwichShop(){
        return orderService.getTodaysSandwichShop();
    }

    public void handleOrder()


}
