package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderJpaRepository;
import be.abis.sandwichordersystem.repository.OrderRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface OrderJPAService {

    boolean addOrder(Order order);
    boolean deleteOrder(Order order) throws OrderNotFoundException;
    boolean deleteOrderByID(int id) throws OrderNotFoundException;
    void createOrdersForEveryoneToday() throws SandwichShopNotFoundException;
    Order createOrder(Person person);

    void handleOrder(Order order, String remark);
    void handleOrder(Order order, Sandwich sandwich, BreadType breadType, List<Options> options, String remark) throws IngredientNotAvailableException, SandwichNotFoundException;

    Order findOrderById(int id) throws OrderNotFoundException;
    List<Order> findOrdersByDate(LocalDate date);
    List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate);
    List<Order> findOrdersBySession(Session session);
    List<Order> findTodaysOrdersForPerson(Person person);
    List<Order> findAllUnhandeledOrders();
    List<Order> findAllUnfilledOrders();
    List<Order> findAllClosedOrdersForDates(LocalDate startDate, LocalDate endDate) throws OrderNotFoundException;
    List<Person> getAllPersonsFromListOfOrders(List<Order> orders);
    List<Order> findOrdersByStatusAndSession(OrderStatus status, Session session) throws OrderNotFoundException;
    List<Order> findAllFilledOrdersForToday() throws OrderNotFoundException;

    List<Person> findWhoStillHasToOrderToday() throws PersonNotFoundException;

    void generateOrderFile() throws IOException;

    void setTodaysSandwichShop(SandwichShop sandwichShop);

    SandwichShop getTodaysSandwichShop() throws DayOrderDoesNotExistYet;
    List<Sandwich> getTodaysSandwiches() throws DayOrderDoesNotExistYet;
    List<Options> getTodaysOptions() throws DayOrderDoesNotExistYet;
    List<BreadType> getTodaysBreadTypes() throws DayOrderDoesNotExistYet;

    OrderJpaRepository getOrderRepository();
    void setOrderRepository(OrderJpaRepository orderRepository);
    void setSessionService(SessionService sessionService);
    void setDayOrder(DayOrder dayOrder);
    DayOrder getDayOrder();

    Order findTodaysUnfilledOrderByName(String name) throws PersonNotFoundException;

    public void setTodaysFilledOrdersToHandeled() throws NothingToHandleException;
    public void deleteAllUnfilledOrdersOfDay(LocalDate date) throws OrderNotFoundException, OperationNotAllowedException;
    List<Order> findTodaysFilledOrdersForPerson(Person person) throws OrderNotFoundException;
    List<Order> findOrdersByStatusAndDates(OrderStatus status, LocalDate startDate, LocalDate endDate) throws OrderNotFoundException;


}
