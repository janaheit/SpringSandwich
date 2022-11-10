package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.model.Order;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.model.Session;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import be.abis.sandwichordersystem.service.FinancialService;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SandwichShopService;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class FinancialServiceTest {

    @Autowired
    FinancialService financialService;
    private List<Order> mockOrders;

    @Mock
    OrderService orderService;
    @Mock
    SandwichShopService sandwichShopService;
    @Mock
    SandwichShopRepository sandwichShopRepository;
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
        financialService.setSandwichShopService(sandwichShopService);
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

        verify(o1).getPrice();
        verify(o2).getPrice();
        verify(orderService).findAllClosedOrdersForDates(any(), any());
    }

    @Test
    void getTotalPriceForTodayWithoutAnyOrdersForTodayThrowsException() throws OrderNotFoundException {
        when(orderService.findAllClosedOrdersForDates(any(), any())).thenThrow(new OrderNotFoundException("No Orders found"));

        assertThrows(OrderNotFoundException.class, () -> financialService.calculateTotalPriceForPeriod(LocalDate.now(), LocalDate.now()));
        verify(orderService).findAllClosedOrdersForDates(any(), any());
    }

    @Test
    void getTotalPriceForFutureDateThrowsException(){
        fail();
        // TODO needed?
    }

    @Test
    void getTotalPriceForSession() throws OrderNotFoundException {
        when(o1.getPrice()).thenReturn(40.0);
        when(o2.getPrice()).thenReturn(50.0);

        when(orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, s1)).thenReturn(mockOrders);
        double price = financialService.calculateTotalPriceForSession(s1);
        assertEquals(90.0, price);

        verify(o1).getPrice();
        verify(o2).getPrice();
        verify(orderService).findOrdersByStatusAndSession(OrderStatus.HANDELED, s1);
    }

    @Mock Session s3;
    @Test
    void getTotalPriceForNonExistingSession() throws OrderNotFoundException {

        when(orderService.findOrdersByStatusAndSession(OrderStatus.HANDELED, s3)).thenThrow(new OrderNotFoundException("No orders found."));
        assertThrows(OrderNotFoundException.class, () -> financialService.calculateTotalPriceForSession(s3));

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

        verify(o1).getPrice();
        verify(o2).getPrice();
        verify(o3).getPrice();
        verify(o1).getSession();
        verify(o2).getSession();
        verify(o3).getSession();
        verify(orderService).findAllClosedOrdersForDates(any(), any());
    }

    @Test
    void getPricesPerSessionForPeriodWorks() throws OrderNotFoundException {
        when(o1.getPrice()).thenReturn(30.0);
        when(o2.getPrice()).thenReturn(50.0);
        when(o3.getPrice()).thenReturn(20.0);

        when(o1.getSession()).thenReturn(s1);
        when(o2.getSession()).thenReturn(s1);
        when(o3.getSession()).thenReturn(s2);

        mockOrders.add(o3);
        
        when(orderService.findAllClosedOrdersForDates(LocalDate.of(2022,11,9),
                LocalDate.of(2022,11,15))).thenReturn(mockOrders);

        Map<Session, Double> pricesPerSession = financialService.calculatePricesPerSessionForPeriod(
                LocalDate.of(2022, 11, 9), LocalDate.of(2022, 11, 15));

        assertEquals(80.0, pricesPerSession.get(s1));
        assertEquals(20.0, pricesPerSession.get(s2));

        verify(o1).getPrice();
        verify(o2).getPrice();
        verify(o3).getPrice();
        verify(o1).getSession();
        verify(o2).getSession();
        verify(o3).getSession();
        verify(orderService).findAllClosedOrdersForDates(any(), any());
    }

    @Mock
    Sandwich sand1;
    @Mock Sandwich sand2;
    //@Mock Sandwich sand3;
    @Mock
    SandwichShop sandwichShop1;
    @Mock
    SandwichShop sandwichShop2;
    @Test
    void getPopularity() throws OrderNotFoundException {
        when(o1.getSandwich()).thenReturn(sand1);
        when(o2.getSandwich()).thenReturn(sand1);
        when(o3.getSandwich()).thenReturn(sand2);
        mockOrders.add(o3);

        List<SandwichShop> shops = new ArrayList<>();
        shops.add(sandwichShop1);
        shops.add(sandwichShop2);

        List<Sandwich> shop1sandwich = new ArrayList<>();
        shop1sandwich.add(sand1);
        List<Sandwich> shop2sandwich = new ArrayList<>();
        shop2sandwich.add(sand2);

        when(sandwichShopService.getSandwichShopRepository()).thenReturn(sandwichShopRepository);
        when(sandwichShopRepository.getShops()).thenReturn(shops);
        when(sandwichShop1.getSandwiches()).thenReturn(shop1sandwich);
        when(sandwichShop2.getSandwiches()).thenReturn(shop2sandwich);
        when(orderService.findOrdersByStatusAndDates(OrderStatus.HANDELED, LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1))).thenReturn(mockOrders);

        Map<Sandwich, Integer> pop = financialService.getPopularityOfSandwichesByDates(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));

        assertEquals(1, pop.get(sand2));
        assertEquals(2, pop.get(sand1));
    }

    @Test
    void getPopularityThrowsException() throws OrderNotFoundException {
        when(orderService.findOrdersByStatusAndDates(OrderStatus.HANDELED, LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1))).thenThrow(new OrderNotFoundException("No orders found"));

        assertThrows(OrderNotFoundException.class, () -> financialService.getPopularityOfSandwichesByDates(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1)));

    }

}
