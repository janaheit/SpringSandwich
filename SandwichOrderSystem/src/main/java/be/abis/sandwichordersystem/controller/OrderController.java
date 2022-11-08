package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.NameModel;
import be.abis.sandwichordersystem.dto.OrderModel;
import be.abis.sandwichordersystem.exception.PersonNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders")
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

    @PostMapping("order")
    public void handleOrder(@RequestBody OrderModel orderModel) throws PersonNotFoundException {

        String fullName = orderModel.getPerson().getFirstName() + " " + orderModel.getPerson().getLastName();
        Order personOrder = orderService.findTodaysOrderByName(fullName);

        // if no sandwich
        if (orderModel.getNoSandwich()) {
            orderService.handleOrder(personOrder, orderModel.getRemark());
        }


        // if sandwich yes

        //orderService.handleOrder(order);
    }


}
