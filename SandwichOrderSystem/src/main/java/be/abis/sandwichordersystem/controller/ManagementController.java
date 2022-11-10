package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.dto.NameModel;
import be.abis.sandwichordersystem.dto.OrderModel;
import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SandwichShopService;
import be.abis.sandwichordersystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class ManagementController {

    @Autowired
    OrderService orderService;
    @Autowired
    SandwichShopService sandwichShopService;
    @Autowired
    SessionService sessionService;


    // Old methods copied from ordercontroller, see below for new
    // only for testing purposes

    @GetMapping("startup")
    public void startDay() throws SandwichShopNotFoundException {
        // set Vleugels as currentSandwichShop
        orderService.setTodaysSandwichShop(sandwichShopService.findShopByName("Vleugels"));
        System.out.println("SandwichShop set to: "+ orderService.getTodaysSandwichShop().getName());
        orderService.createOrdersForEveryoneToday();
    }
    /*
    // only get filled orders (excluding noSandwich)
    // today
    @GetMapping("/today")
    public List<Order> getAllOrdersToday() {
        return orderService.findOrdersByDate(LocalDate.now());
    }
    

    // quick and dirty testing
    @GetMapping("orderfile")
    public void makeFile() throws IOException {
        orderService.generateOrderFile();
    }
     */


    //TODO GET /orders/today -> all filled orders for today (grouped by session)
    @GetMapping("today")
    public ResponseEntity<? extends Object> findAllFilledOrdersToday() throws OrderNotFoundException {
        List<Order> orderList = orderService.findAllFilledOrdersForToday();
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    //TODO GET /orders/missing -> get all unfilled orders / who still has to order
    @GetMapping("missing")
    public ResponseEntity<? extends Object> findWhoStillHasToOrder() throws PersonNotFoundException {
        List<Person> personList = orderService.findWhoStillHasToOrderToday();
        return new ResponseEntity<List<Person>>(personList, HttpStatus.OK);
    }

    //TODO GET /orders/period?start=12-12-2022&end=30-12-2022 → get all closed orders for period grouped by session
    @GetMapping("period")
    public ResponseEntity<? extends Object> getClosedOrdersPerPeriod(@RequestParam("start") @DateTimeFormat(pattern="d-M-yyyy") LocalDate startDate, @RequestParam("end") @DateTimeFormat(pattern="d-M-yyyy")LocalDate endDate) throws OrderNotFoundException {
        List<Order> orderlist = orderService.findAllClosedOrdersForDates(startDate, endDate);
        return new ResponseEntity<List<Order>>(orderlist, HttpStatus.OK);
    }


    //TODO GET /orders/session?id={id} → get all orders for session
    @GetMapping("session")
    public ResponseEntity<? extends Object> getAllClosedOrdersForSessionId(@RequestParam("id") int id) throws SessionNotFoundException, OrderNotFoundException {
        Session mySession = sessionService.findSessionByID(id);
        List<Order> orderList = orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, mySession);
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    //TODO GET /orders/query?date={date} → get all orders for date grouped by session

    @GetMapping("query")
    public ResponseEntity<? extends Object> getAllClosedOrdersForDate(@RequestParam("date") @DateTimeFormat(pattern="d-M-yyyy") LocalDate date) throws OrderNotFoundException {
        List<Order> orderList = orderService.findAllClosedOrdersForDates(date, date);
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }


    //TODO GET /orders/sessions/period?start=12-12-2022&end=30-12-2022 → get all sessions during period
    @GetMapping("sessions/period")
    public ResponseEntity<? extends Object> getAllSessionsDuringPeriod(@RequestParam("start") @DateTimeFormat(pattern="d-M-yyyy") LocalDate startDate, @RequestParam("end") @DateTimeFormat(pattern="d-M-yyyy")LocalDate endDate) {
        List<Session> sessionList = sessionService.findSessionsByPeriod(startDate, endDate);
        return new ResponseEntity<List<Session>>(sessionList, HttpStatus.OK);
    }

    //TODO GET /orders/sessions/instructor?name=Sandy → get all sessions taught by sandy

    //TODO POST /orders/startup → start day with selecting shop (param); creating orders for everyone

    //TODO POST /orders/close → close all orders // when you ordered
    @PostMapping("close")
    public ResponseEntity<? extends Object> closeOrdersOfDay() throws IOException, NothingToHandleException, OrderNotFoundException, OperationNotAllowedException {
        orderService.generateOrderFile();
        orderService.setTodaysFilledOrdersToHandeled();
        // Also deletes the noSandwich orders.
        orderService.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        return new ResponseEntity<String>("all Good", HttpStatus.OK);
    }

    //TODO GET /orders/shops → get all sandwich shops

    //TODO POST /orders/shops → add sandwichshop

    //TODO DELETE /orders/shops/{shopID} → delete sandwichshop

    //TODO POST /orders/shops/{shopID}/sandwiches → add sandwich to sandwichshop

    //TODO DELETE /orders/shops/{shopID}/sandwiches/{sandwichID} → add sandwich to sandwichshop

    //TODO PUT /orders/shops/{shopID}/sandwiches/{sandwichID} → update sandwich at sandwichshop (generic update)

}
