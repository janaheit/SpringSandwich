package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderRepository;
import be.abis.sandwichordersystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Autowired OrderService orderService;

    @Mock OrderRepository orderRepository;

    @Mock Order o1;
    @Mock Order o2;
    @Mock Order o3;
    @Mock Order o4;


    @BeforeEach
    void setUp(){
        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
        orders.add(o4);

        when(orderRepository.getOrders()).thenReturn(orders);
    }

    @Test
    void addOrderWorks(){

    }

    @Test
    void deleteOrderWorks(){

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
    void handleOrderNoSandwichWithRemark(Order order, Boolean noSandwich, String remark){

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
    void findOrdersBySession(Session session){

    }
    @Test
    void findTodaysOrdersForPerson(Person person){

    }

    @Test
    void findAllUnhandeledOrders(){

    }

    @Test
    void findAllUnfilledOrders(){

    }

    @Test
    void getAllPersonsFromListOfOrders(List<Order> orders){

    }

    @Test
    void generateOrderFile() throws IOException{

    }

    @Test
    void setTodaysSandwichShop(SandwichShop sandwichShop){

    }


}
