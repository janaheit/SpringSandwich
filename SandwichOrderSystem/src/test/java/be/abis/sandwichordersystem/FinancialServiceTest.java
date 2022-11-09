package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.service.FinancialService;
import be.abis.sandwichordersystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FinancialServiceTest {

    @Autowired
    FinancialService financialService;

    @Mock
    OrderService orderService;


    @BeforeEach
    void setUp(){
        financialService.setOrderService(orderService);
    }


    @Mock
    Order o1;
    @Mock Order o2;
    @Test
    void getTotalPriceForToday() throws OrderNotFoundException {
        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);

        when(o1.getPrice()).thenReturn(4.0);
        when(o2.getPrice()).thenReturn(5.0);

        when(orderService.findAllClosedOrdersForDates(any(), any())).thenReturn(orders);
        double price= financialService.getTotalPriceForPeriod(LocalDate.now(), LocalDate.now());
        assertEquals(9.0, price);
    }
}
