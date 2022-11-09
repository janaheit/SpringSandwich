package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Session;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FinancialServiceTest {

    @Autowired
    FinancialService financialService;
    private List<Order> mockOrders;

    @Mock
    OrderService orderService;
    @Mock
    Order o1;
    @Mock
    Order o2;
    @Mock
    Order o3;
    @Mock
    Session s1;
    @Mock
    Session s2;

    @BeforeEach
    void setUp(){
        financialService.setOrderService(orderService);
        mockOrders = new ArrayList<>();
        mockOrders.add(o1);
        mockOrders.add(o2);
    }



    @Test
    void getTotalPriceForToday() throws OrderNotFoundException {
        when(o1.getPrice()).thenReturn(4.0);
        when(o2.getPrice()).thenReturn(5.0);

        when(orderService.findAllClosedOrdersForDates(any(), any())).thenReturn(mockOrders);
        double price= financialService.calculateTotalPriceForPeriod(LocalDate.now(), LocalDate.now());
        assertEquals(9.0, price);
    }

    @Test
    void getTotalPriceForTodayWithoutAnyOrdersForTodayThrowsException() throws OrderNotFoundException {
        when(orderService.findAllClosedOrdersForDates(any(), any())).thenThrow(new OrderNotFoundException("No Orders found"));

        assertThrows(OrderNotFoundException.class, () -> financialService.calculateTotalPriceForPeriod(LocalDate.now(), LocalDate.now()));
    }

    @Test
    void getTotalPriceForFutureDateThrowsException(){
        fail();
        // TODO needed?
    }

    // we need to remove the orders with no sandwich
    @Test
    void getTotalPriceForSession() throws OrderNotFoundException {
        when(o1.getPrice()).thenReturn(40.0);
        when(o2.getPrice()).thenReturn(50.0);

        when(orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, s1)).thenReturn(mockOrders);
        double price = financialService.calculateTotalPriceForSession(s1);
        assertEquals(90.0, price);
        verify(orderService).findOrdersByStatusAndSession(OrderStatus.HANDELED, s1);
    }

    @Mock Session s3;
    @Test
    void getTotalPriceForNonExistingSession() throws OrderNotFoundException {

        fail();
        // first test method in orderRepo && orderservice
        // TODO implement the Exception handling here
        // when(orderService.findOrdersBySession(s1)).thenThrow(() -> );
    }

    // tests both calculatePricesPerSessionOnDate and calculatePricesPerSessionForPeriod (using same code)
    @Test
    void getPricesPerSessionOnDateWorks() throws OrderNotFoundException {
        when(o1.getPrice()).thenReturn(40.0);
        when(o2.getPrice()).thenReturn(50.0);
        when(o3.getPrice()).thenReturn(20.0);

        when(o1.getSession()).thenReturn(s1);
        when(o2.getSession()).thenReturn(s1);
        when(o3.getSession()).thenReturn(s2);

        mockOrders.add(o3);

        when(orderService.findAllClosedOrdersForDates(LocalDate.of(2022,11,9),
                LocalDate.of(2022,11,9))).thenReturn(mockOrders);

        Map<Session, Double> pricesPerSession = financialService.calculatePricesPerSessionOnDate(LocalDate.of(2022, 11, 9));

        assertEquals(90.0, pricesPerSession.get(s1));
        assertEquals(20.0, pricesPerSession.get(s2));
    }
}
