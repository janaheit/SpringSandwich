package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.OrderRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
