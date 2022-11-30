package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.OrderModel;
import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.PersonService;
import be.abis.sandwichordersystem.service.SandwichShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    SandwichShopService sandwichShopService;
    @Autowired
    PersonService personService;

    // when a person tries to order, they will get back always a new unfilled order in their name,
    // so that they can order twice
    @GetMapping("/unfilled/query")
    public Order findTodaysUnfilledOrderByName(@RequestParam String name) throws PersonNotFoundException {
        return orderService.findTodaysUnfilledOrderByName(name);
    }

    @GetMapping("/filled/query")
    public List<Order> findTodaysFilledOrdersByName(@RequestParam String name) throws OrderNotFoundException, PersonNotFoundException {
        Person p = personService.findPersonByName(name);
        return orderService.findTodaysFilledOrdersForPerson(p);
    }

    @GetMapping("shop")
    public SandwichShop getTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        return orderService.getTodaysSandwichShop();
    }

    @GetMapping("shop/sandwiches")
    public List<Sandwich> getTodaysSandwiches() throws DayOrderDoesNotExistYet {
        return orderService.getTodaysSandwiches();
    }

    @GetMapping("shop/options")
    public List<Options> getTodaysOptions() throws DayOrderDoesNotExistYet {
        return orderService.getTodaysOptions();
    }

    @GetMapping("shop/breadtypes")
    public List<BreadType> getTodaysBreadTypes() throws DayOrderDoesNotExistYet {
        return orderService.getTodaysBreadTypes();
    }


    @PostMapping()
    public void handleOrder(@RequestBody OrderModel orderModel) throws PersonNotFoundException, IngredientNotAvailableException, SandwichNotFoundException {

        String fullName = orderModel.getPerson().getFirstName() + " " + orderModel.getPerson().getLastName();
        Order personOrder = orderService.findTodaysUnfilledOrderByName(fullName);

        // if noSandwich == true (aka person does not want a sandwich)
        if (orderModel.getNoSandwich()) {
            orderService.handleOrder(personOrder, orderModel.getRemark());
        } else { // if noSandwich == false (aka person wants a sandwich)
            orderService.handleOrder(personOrder, orderModel.getSandwich(), orderModel.getBreadType(),
                    orderModel.getOptions(), orderModel.getRemark());
        }
    }
}
