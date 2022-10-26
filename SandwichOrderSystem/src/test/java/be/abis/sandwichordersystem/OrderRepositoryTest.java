package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;

    @Mock private Order order1;
    @Mock private Order order2;
    @Mock private Order order3;

    @Test
    public void addOrderWorks(){
        orderRepository.addOrder(order1);
        assertTrue(orderRepository.getAllOrders().contains(order1));
        orderRepository.getAllOrders().remove(order1);
    }

    @Test
    public void deleteOrderWorks(){
        orderRepository.addOrder(order1);
        orderRepository.getAllOrders().remove(order1);
        assertFalse(orderRepository.getAllOrders().contains(order1));
    }

}
