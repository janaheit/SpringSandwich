package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.DayOrder;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Person;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.OrderRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;

    @Mock private Order order1;
    @Mock private Order order2;
    @Mock private Order order3;
    @Mock private Session session;
    @Mock private Session session1;
    @Mock private Session session2;
    @Mock private DayOrder mockDayOrder;
    @Mock Person p1;
    @Mock Person p2;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void addOrderWorks(){
        orderRepository.addOrder(order1);
        assertTrue(orderRepository.getOrders().contains(order1));
        orderRepository.getOrders().remove(order1);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void deleteOrderWorks() throws OrderNotFoundException {
        orderRepository.addOrder(order1);
        orderRepository.deleteOrder(order1);
        assertFalse(orderRepository.getOrders().contains(order1));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void deleteNonExistingOrderThrowsException(){
        assertThrows(OrderNotFoundException.class, () -> orderRepository.deleteOrder(order1));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void findOrdersBySessionWorks() throws OrderNotFoundException {
        when(order1.getSession()).thenReturn(session);
        when(order2.getSession()).thenReturn(session1);
        when(order3.getSession()).thenReturn(session2);

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);
        orderRepository.addOrder(order3);

        assertEquals(1, orderRepository.findOrdersBySession(session).size());

        orderRepository.deleteOrder(order1);
        orderRepository.deleteOrder(order2);
        orderRepository.deleteOrder(order3);

    }

    @Test
    public void findOrderByStatusAndDatesReturnsCorrectOrder() throws OrderNotFoundException {
        when(order1.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order1.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(order2.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order2.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);

        List<Order> myReturnList = orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED, LocalDate.now(), LocalDate.now());

        assertTrue(myReturnList.contains(order1) && !myReturnList.contains(order2));

        orderRepository.deleteOrder(order1);
        orderRepository.deleteOrder(order2);

    }

    @Test
    public void findOrderByStatusAndDatesThrowsException() {
        assertThrows(OrderNotFoundException.class, () -> orderRepository.findOrdersByStatusAndDates(OrderStatus.HANDELED, LocalDate.now().minusYears(10), LocalDate.now().minusYears(9)));
    }

    @Test
    public void findOrderByPersonAndDatesReturnsCorrectOrder() throws OrderNotFoundException {
        when(order1.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order1.getPerson()).thenReturn(p1);
        when(order2.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order2.getPerson()).thenReturn(p2);

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);

        List<Order> myReturnList = orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now());

        assertTrue(myReturnList.contains(order1) && !myReturnList.contains(order2));

        orderRepository.deleteOrder(order1);
        orderRepository.deleteOrder(order2);

    }

    @Test
    public void findOrdersByPersonAndDatesThrowsException() throws OrderNotFoundException {
        when(order1.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order1.getPerson()).thenReturn(p2);
        when(order2.getDayOrder()).thenReturn(mockDayOrder);
        when(mockDayOrder.getDate()).thenReturn(LocalDate.now());
        when(order2.getPerson()).thenReturn(p2);

        orderRepository.addOrder(order1);
        orderRepository.addOrder(order2);

        assertThrows(OrderNotFoundException.class, () -> orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now()));

        orderRepository.deleteOrder(order1);
        orderRepository.deleteOrder(order2);
    }

}
