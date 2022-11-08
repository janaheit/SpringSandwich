package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.NameModel;
import be.abis.sandwichordersystem.dto.OrderModel;
import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
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
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    SandwichShopService sandwichShopService;

    @PostMapping()
    public Order findTodaysOrderByName(@RequestBody NameModel nameModel) throws PersonNotFoundException {
        return orderService.findTodaysOrderByName(nameModel.getName());
    }

    @GetMapping("shop")
    public SandwichShop getTodaysSandwichShop(){
        return orderService.getTodaysSandwichShop();
    }

    @GetMapping("shop/sandwiches")
    public List<Sandwich> getTodaysSandwiches(){
        return orderService.getTodaysSandwichShop().getSandwiches();
    }

    @GetMapping("shop/options")
    public List<Options> getTodaysOptions(){
        return orderService.getTodaysSandwichShop().getOptions();
    }

    @GetMapping("shop/breadtypes")
    public List<BreadType> getTodaysBreadTypes(){
        return orderService.getTodaysSandwichShop().getBreadTypes();
    }


    @PostMapping("{firstName}")
    public void handleOrder(@PathVariable String firstName, @RequestBody OrderModel orderModel) throws PersonNotFoundException, IngredientNotAvailableException, SandwichNotFoundException {

        String fullName = orderModel.getPerson().getFirstName() + " " + orderModel.getPerson().getLastName();
        Order personOrder = orderService.findTodaysOrderByName(fullName);

        // if noSandwich == true (aka person does not want a sandwich)
        if (orderModel.getNoSandwich()) {
            orderService.handleOrder(personOrder, orderModel.getRemark());
        } else { // if noSandwich == false (aka person wants a sandwich)
            orderService.handleOrder(personOrder, orderModel.getSandwich(), orderModel.getBreadType(),
                    orderModel.getOptions(), orderModel.getRemark());
        }
    }

}