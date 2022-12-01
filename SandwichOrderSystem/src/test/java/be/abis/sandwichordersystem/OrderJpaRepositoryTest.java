package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderJpaRepositoryTest {

    @Autowired
    OrderJpaRepository orderRepository;

    @Autowired
    PersonJpaRepository personRepository;

    @Autowired
    SessionJpaRepository sessionRepository;

    @Autowired
    SandwichJPARepository sandwichRepository;

    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    private Sandwich sandwich1;
    private Order order1;
    private Order order2;
    private Order order3;
    private Session session;
    private Session session1;
    private Session session2;
    private DayOrder mockDayOrder;

    Student p1;
    Student p2;

    @BeforeEach
    public void setUp() {
        p1 = personRepository.findStudentByID(1);
        p2 = personRepository.findStudentByID(2);
        sandwich1 = sandwichRepository.findById(3);
        List<Options> OptionList1 = new ArrayList<>();
        OptionList1.add(Options.GRILLEDVEGGIES);
        session = p1.getCurrentSession();
        session2 = p2.getCurrentSession();
        order1 = new Order();
        order1.setPerson(p1);
        order1.setSandwich(sandwich1);
        order1.setBreadType(BreadType.GREY);
        order1.setRemark("Very good!");
        order1.setSession(session);
        order1.setDate(LocalDate.now());
        order1.setPrice(3.5);
        order1.setAmount(1);
        order1.setOrderStatus(OrderStatus.ORDERED);
        order1.setSandwichShop(sandwichShopRepository.findShopById(2));
        order2 = new Order(p2, new DayOrder(sandwichShopRepository.findShopById(2), LocalDate.now()));
        order2.setSession(session2);

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @Transactional
    public void addOrderWorks(){
        orderRepository.save(order1);
        assertTrue(orderRepository.getOrders().contains(order1));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @Transactional
    public void deleteOrderWorks() throws OrderNotFoundException {
        orderRepository.save(order1);
        int amountbeforetest = orderRepository.getOrders().size();
        orderRepository.delete(order1);
        assertEquals(amountbeforetest-1, orderRepository.getOrders().size());
    }



    @Test
    @org.junit.jupiter.api.Order(4)
    public void findOrdersBySessionWorks() throws OrderNotFoundException {

        assertEquals(1, orderRepository.findOrdersBySession(1).size());

    }

    @Test
    @Transactional
    public void findOrderByStatusAndDatesReturnsCorrectOrder() throws OrderNotFoundException {

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> myReturnList = orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED.name(), LocalDate.now(), LocalDate.now());

        assertTrue(myReturnList.contains(order1) && !myReturnList.contains(order2));


    }


    @Test
    @Transactional
    public void findOrderByPersonAndDatesReturnsCorrectOrder() throws OrderNotFoundException {

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> myReturnList = orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now());

        assertTrue(myReturnList.contains(order1) && !myReturnList.contains(order2));
    }


    @Test
    @Transactional
    public void findOrdersByStatusAndSessionHappyCase1() throws OrderNotFoundException {

        order2.setSession(session);
        orderRepository.save(order1);
        orderRepository.save(order2);

        // Assert here
        List<Order> myTestList = orderRepository.findOrdersByStatusAndSession(OrderStatus.UNFILLED.name(), session.getSessionNumber());
        assertTrue(!myTestList.contains(order1) && myTestList.contains(order2));

    }

    @Test
    @Transactional
    public void findOrdersByStatusAndSessionHappyCase2() throws OrderNotFoundException {
        order1.setOrderStatus(OrderStatus.UNFILLED);


        orderRepository.save(order1);
        orderRepository.save(order2);

        // Assert here
        List<Order> myTestList = orderRepository.findOrdersByStatusAndSession(OrderStatus.UNFILLED.name(), session.getSessionNumber());
        assertTrue(myTestList.contains(order1) && !myTestList.contains(order2));

    }

    @Test
    @Transactional
    public void findOrdersByStatusAndSessionHappyCase3() throws OrderNotFoundException {

        order1.setOrderStatus(OrderStatus.UNFILLED);
        order2.setSession(session);


        orderRepository.save(order1);
        orderRepository.save(order2);

        // Assert here
        List<Order> myTestList = orderRepository.findOrdersByStatusAndSession(OrderStatus.UNFILLED.name(), session.getSessionNumber());
        assertTrue(myTestList.contains(order1) && myTestList.contains(order2));

    }



}
