package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderRepository;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Autowired OrderService orderService;

    @Mock SessionService sessionService;
    @Mock OrderRepository orderRepository;

    @Mock Order o5;


    @BeforeEach
    void setUp(){
        orderService.setOrderRepository(orderRepository);
    }

    @Test
    void addOrderWorks(){
        when(orderRepository.addOrder(o5)).thenReturn(true);
        assertTrue(orderService.addOrder(o5));
        verify(orderRepository).addOrder(o5);
        //System.out.println(orderService.getOrderRepository().getOrders());
    }

    @Test
    void deleteOrderWorks() throws OrderNotFoundException {
        when(orderRepository.deleteOrder(o5)).thenReturn(true);
        assertTrue(orderService.deleteOrder(o5));
    }

    @Test
    void createOrdersForEveryoneToday() throws SandwichShopNotFoundException {

    }

    @Test
    void createOrderForPerson(){

    }

    @Test
    void handleOrderWithNoSandwich(){

    }

    @Test
    void handleOrderNoSandwichWithRemark(){

    }

    @Test
    void handleOrderWithSandwich() throws IngredientNotAvailableException{

    }

    @Test
    void findOrdersByDate(){

    }

    @Test
    void findOrdersBetweenDates(){

    }

    @Test
    void findOrdersBySession(){

    }
    @Test
    void findTodaysOrdersForPerson(){

    }

    @Test
    void findAllUnhandeledOrders(){

    }

    @Test
    void findAllUnfilledOrders(){

    }

    @Test
    void getAllPersonsFromListOfOrders(){

    }

    @Test
    void generateOrderFile() throws IOException{

    }

    @Test
    void setTodaysSandwichShop(){

    }


}
