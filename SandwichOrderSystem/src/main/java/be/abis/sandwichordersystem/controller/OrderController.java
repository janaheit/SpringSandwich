package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.OrderCreationDTO;
import be.abis.sandwichordersystem.dto.OrderDTO;
import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.mapper.OrderMapper;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderJPAService orderService;
    @Autowired
    SandwichJPAService sandwichService;
    @Autowired
    PersonService personService;

    // when a person tries to order, they will get back always a new unfilled order in their name,
    // so that they can order twice
    @GetMapping("/unfilled/query")
    public OrderDTO findTodaysUnfilledOrderByName(@RequestParam String name) throws PersonNotFoundException, OrderAlreadyExistsException {
        return OrderMapper.toDTO(orderService.findTodaysUnfilledOrderByName(name));
    }

    @Transactional
    @GetMapping("/filled/query")
    public List<OrderDTO> findTodaysFilledOrdersByName(@RequestParam String name) throws OrderNotFoundException, PersonNotFoundException {
        Person p = personService.findPersonByName(name);
        return orderService.findTodaysFilledOrdersForPerson(p).stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
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
    public void handleOrder(@RequestBody OrderCreationDTO orderCreationDTO) throws PersonNotFoundException, IngredientNotAvailableException, SandwichNotFoundException, OrderAlreadyExistsException {

        String fullName = orderCreationDTO.getPersonFullName();
        Order personOrder = orderService.findTodaysUnfilledOrderByName(fullName);

        // if noSandwich == true (aka person does not want a sandwich)
        if (orderCreationDTO.getNoSandwich()) {
            orderService.handleOrder(personOrder, orderCreationDTO.getRemark());
        } else { // if noSandwich == false (aka person wants a sandwich)
            orderService.handleOrder(personOrder, orderCreationDTO.getSandwichID(), orderCreationDTO.getBreadType(),
                    orderCreationDTO.getOptions(), orderCreationDTO.getRemark());
        }
    }
}
