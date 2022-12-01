package be.abis.sandwichordersystem.controller;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.service.*;
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
    OrderJPAService orderService;
    @Autowired
    SandwichJPAService sandwichJPAService;
    @Autowired
    SessionService sessionService;
    @Autowired
    PersonService personService;

    //TODO reimplement with new order service
    @GetMapping("startup")
    public void startDay() throws SandwichShopNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        // set Vleugels as currentSandwichShop
        orderService.setTodaysSandwichShop(sandwichJPAService.findShopByName("Vleugels"));
        System.out.println("SandwichShop set to: "+ orderService.getTodaysSandwichShop().getName());
        orderService.createOrdersForEveryoneToday();
    }

    //TODO reimplement with new order service
    //GET /orders/today -> all filled orders for today (grouped by session)
    @GetMapping("today")
    public ResponseEntity<? extends Object> findAllFilledOrdersToday() throws OrderNotFoundException {
        List<Order> orderList = orderService.findAllFilledOrdersForToday();
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    //TODO reimplement with new order service
    // GET /orders/missing -> get all unfilled orders / who still has to order
    @GetMapping("missing")
    public ResponseEntity<? extends Object> findWhoStillHasToOrder() throws PersonNotFoundException {
        List<Person> personList = orderService.findWhoStillHasToOrderToday();
        return new ResponseEntity<List<Person>>(personList, HttpStatus.OK);
    }

    //TODO reimplement with new order service
    // GET /orders/period?start=12-12-2022&end=30-12-2022 → get all closed orders for period grouped by session
    @GetMapping("period")
    public ResponseEntity<? extends Object> getClosedOrdersPerPeriod(@RequestParam("start") @DateTimeFormat(pattern="d-M-yyyy") LocalDate startDate, @RequestParam("end") @DateTimeFormat(pattern="d-M-yyyy")LocalDate endDate) throws OrderNotFoundException {
        List<Order> orderlist = orderService.findAllClosedOrdersForDates(startDate, endDate);
        return new ResponseEntity<List<Order>>(orderlist, HttpStatus.OK);
    }

    //TODO reimplement with new order service
    // GET /orders/session?id={id} → get all orders for session
    @GetMapping("session")
    public ResponseEntity<? extends Object> getAllClosedOrdersForSessionId(@RequestParam("id") int id) throws SessionNotFoundException, OrderNotFoundException {
        Session mySession = sessionService.findSessionByID(id);
        List<Order> orderList = orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, mySession);
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    //TODO reimplement with new order service
    // GET /orders/query?date={date} → get all orders for date grouped by session
    @GetMapping("query")
    public ResponseEntity<? extends Object> getAllClosedOrdersForDate(@RequestParam("date") @DateTimeFormat(pattern="d-M-yyyy") LocalDate date) throws OrderNotFoundException {
        List<Order> orderList = orderService.findAllClosedOrdersForDates(date, date);
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    // GET /orders/sessions/period?start=12-12-2022&end=30-12-2022 → get all sessions during period
    @GetMapping("sessions/period")
    public ResponseEntity<? extends Object> getAllSessionsDuringPeriod(@RequestParam("start") @DateTimeFormat(pattern="d-M-yyyy") LocalDate startDate, @RequestParam("end") @DateTimeFormat(pattern="d-M-yyyy")LocalDate endDate) {
        List<Session> sessionList = sessionService.findSessionsByPeriod(startDate, endDate);
        return new ResponseEntity<List<Session>>(sessionList, HttpStatus.OK);
    }

    // GET /orders/sessions/instructor?name=Sandy → get all sessions taught by sandy
    @GetMapping("sessions/instructor")
    public ResponseEntity<? extends Object> getAllSessionsOfInstructor(@RequestParam("name") String fullName) throws PersonNotFoundException {
        Instructor myInstructor = personService.findInstructorByName(fullName);
        List<Session> sessionList = sessionService.findSessionsByInstructor(myInstructor.getPersonNr());
        return new ResponseEntity<List<Session>>(sessionList, HttpStatus.OK);
    }
    //TODO POST /orders/startup → start day with selecting shop (param); creating orders for everyone

    //TODO reimplement with new order service
    // POST /orders/close → close all orders // when you ordered
    @PostMapping("close")
    public ResponseEntity<? extends Object> closeOrdersOfDay() throws IOException, NothingToHandleException, OrderNotFoundException, OperationNotAllowedException {
        orderService.generateOrderFile();
        orderService.setTodaysFilledOrdersToHandeled();
        // Also deletes the noSandwich orders.
        orderService.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        return new ResponseEntity<String>("all Good", HttpStatus.OK);
    }

    // GET /orders/shops → get all sandwich shops
    @GetMapping("shops")
    public ResponseEntity<? extends Object> getAllSandwichShops() {
        // Returns List of Sandwich Shop (with id and name!!! no lists)
        List<SandwichShop> myList = sandwichJPAService.getSandwichShops();
        return new ResponseEntity<List<SandwichShop>>(myList, HttpStatus.OK);
    }

    //POST /orders/shops/{shopID}/sandwiches → add sandwich to sandwichshop
    @PostMapping("shops/{id}/sandwiches")
    public ResponseEntity<? extends Object> addSandwichToShop(@PathVariable("id") int id, @RequestBody Sandwich sandwich) throws SandwichShopNotFoundException, SandwichAlreadyExistsException {
        sandwichJPAService.addSandwichToShop(sandwich, id);
        return new ResponseEntity<String>("Added", HttpStatus.OK);
    }

    //DELETE /orders/shops/{shopID}/sandwiches/{sandwichID} → delete sandwich from sandwichshop
    @DeleteMapping("shops/{shopID}/sandwiches/{sandwichID}")
    public ResponseEntity<? extends Object> deleteSandwichFromShop(@PathVariable("shopID") int shopID, @PathVariable("sandwichID") int sandwichID) throws SandwichShopNotFoundException, SandwichNotFoundException, OperationNotAllowedException {

        SandwichShop shop = sandwichJPAService.findShopByID(shopID);
        SandwichShop findShop = sandwichJPAService.findShopForSandwich(sandwichID);
        if(!findShop.equals(shop)) {
            throw new OperationNotAllowedException("This Sandwich does not belong to this sandwichshop!");
        }

        sandwichJPAService.deleteSandwichByID(sandwichID);
        return new ResponseEntity<String>("Deleten", HttpStatus.OK);
    }


    // TODO reimplement adding and deleting shops
    /*
    // POST /orders/shops → add sandwichshop
    @PostMapping("shops")
    public ResponseEntity<? extends Object> addSandwichShop(@RequestBody SandwichShopDTOModel sandwichShopDTO) {
        SandwichShop myShop = new SandwichShop(sandwichShopDTO.getName());
        if(sandwichShopDTO.getSandwiches() != null) {
            myShop.setSandwiches(sandwichShopDTO.getSandwiches());
        }
        if(sandwichShopDTO.getBreadTypes() != null) {
            myShop.setBreadTypes(sandwichShopDTO.getBreadTypes());
        }
        if(sandwichShopDTO.getOptions() != null) {
            myShop.setOptions(sandwichShopDTO.getOptions());
        }
        sandwichShopService.getSandwichShopRepository().addShop(myShop);
        return new ResponseEntity<String>("Very good! My name Borat!", HttpStatus.OK);
    }



    //DELETE /orders/shops/{shopID} → delete sandwichshop
    @DeleteMapping("shops/{id}")
    public ResponseEntity<? extends Object> deleteSandwichShop(@PathVariable("id") int id) throws SandwichShopNotFoundException {
        SandwichShop shop = sandwichShopService.getSandwichShopRepository().findSandwichShopById(id);
        sandwichShopService.getSandwichShopRepository().deleteShop(shop);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }
     */

    // TODO update implement in Service
    /*
    //PUT /orders/shops/{shopID}/sandwiches/{sandwichID} → update sandwich at sandwichshop (generic update)
    @PutMapping("shops/{shopID}/sandwiches/{sandwichID}")
    public ResponseEntity<? extends Object> updateSandwichInShop(@PathVariable("shopID") int shopID, @PathVariable("sandwichID") int sandwichID, @RequestBody Sandwich sandwich) throws OperationNotAllowedException, SandwichNotFoundException, SandwichShopNotFoundException {
        SandwichShop shop = sandwichShopService.getSandwichShopRepository().findSandwichShopById(shopID);
        Sandwich sandwichToUpdate = sandwichShopService.findSandwichById(sandwichID);
        SandwichShop findShop = sandwichShopService.findShopForSandwich(sandwichToUpdate);
        if(findShop!=shop) {
            throw new OperationNotAllowedException("This Sandwich does not belong to this sandwichshop!");
        }
        if (sandwich.getName() != null) {
            sandwichToUpdate.setName(sandwich.getName());
        }
        if (sandwich.getCategory() != null) {
            sandwichToUpdate.setCategory(sandwich.getCategory());
        }
        if (sandwich.getDescription() != null) {
            sandwichToUpdate.setDescription(sandwich.getDescription());
        }
        if (sandwich.getPrice() != 0.0) {
            sandwichToUpdate.setPrice(sandwich.getPrice());
        }
        return new ResponseEntity<String>("All good in the hood", HttpStatus.OK);
    }

     */
}
