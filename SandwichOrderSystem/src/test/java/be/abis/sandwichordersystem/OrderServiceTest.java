package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderRepository;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SessionService;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired OrderService cut;
    @Autowired
    SandwichShopRepository sandwichShopRepository;

    @Mock SessionService sessionService;
    @Mock OrderRepository orderRepository;

    @Mock Order o5;
    @Mock Order mockOrder2;

    @Mock Session mockSession;

    Order testOrder;

    Person p1;
    Person p2;
    List<Person> people = new ArrayList<>();


    @BeforeEach
    void setUp(){
        cut.setOrderRepository(orderRepository);
        cut.setSessionService(sessionService);
        cut.setDayOrder(null);

        testOrder = null;


        // Setup of sessionServiceMock
        p1 = new Person("p1", "lastname");
        p2 = new Person("p2", "lastname");
        people.add(p1);
        people.add(p2);
        when(sessionService.findAllPersonsFollowingSessionToday()).thenReturn(people);

    }

    @Test
    void addOrderWorks(){
        when(orderRepository.addOrder(o5)).thenReturn(true);
        assertTrue(cut.addOrder(o5));
        verify(orderRepository).addOrder(o5);
        //System.out.println(orderService.getOrderRepository().getOrders());
    }

    @Test
    void deleteOrderWorks() throws OrderNotFoundException {
        when(orderRepository.deleteOrder(o5)).thenReturn(true);
        assertTrue(cut.deleteOrder(o5));
    }


    @Test
    void createOrdersForEveryoneTodayThrowsExceptionIfNoSandwichShopIsSetTest() {
        assertThrows(SandwichShopNotFoundException.class, () -> cut.createOrdersForEveryoneToday());
    }

    @Test
    void createOrdersForEveryoneToday() throws SandwichShopNotFoundException {
        //TODO should be implemented
        cut.setTodaysSandwichShop(sandwichShopRepository.getShops().get(0));
        when(orderRepository.addOrder(any())).thenReturn(true);
        List<Session> locoSession = new ArrayList<Session>();
        locoSession.add(mockSession);
        when(sessionService.findSessionsToday()).thenReturn(locoSession);
        when(sessionService.findAllPersonsFollowingSession(any())).thenReturn(people);
        cut.createOrdersForEveryoneToday();
        verify(sessionService).findSessionsToday();
        verify(sessionService).findAllPersonsFollowingSession(any());
        verify(orderRepository, times(people.size())).addOrder(any());
    }

    @Test
    void createOrderForPerson(){
        when(orderRepository.addOrder(any())).thenReturn(true);
        //System.out.println(cut.getDayOrder());
        Order myOrder = cut.createOrder(p1);
        //System.out.println(myOrder.getDayOrder());
        assertEquals(p1, myOrder.getPerson());
        verify(orderRepository).addOrder(myOrder);
    }

    @Test
    void handleOrderWithNoSandwich(){
        testOrder = new Order(p1, cut.getDayOrder());
        cut.handleOrder(testOrder, "test");
        assertEquals(OrderStatus.NOSANDWICH, testOrder.getOrderStatus());
    }


    @Test
    void handleOrderWithSandwichWorks() throws IngredientNotAvailableException, SandwichNotFoundException {
        cut.setTodaysSandwichShop(sandwichShopRepository.getShops().get(0));
        testOrder = new Order(p1, cut.getDayOrder());
        cut.handleOrder(testOrder, sandwichShopRepository.getShops().get(0).getSandwiches().get(1), BreadType.GREY, sandwichShopRepository.getShops().get(0).getOptions(), "");
        assertEquals(OrderStatus.ORDERED, testOrder.getOrderStatus());
    }

    @Test
    void handleOrderWithSandwichThrowsExceptionWithUnavailableSandwich(){
        cut.setTodaysSandwichShop(sandwichShopRepository.getShops().get(0));
        testOrder = new Order(p1, cut.getDayOrder());
        assertThrows(SandwichNotFoundException.class, () -> cut.handleOrder(testOrder, sandwichShopRepository.getShops().get(1).getSandwiches().get(1), BreadType.GREY, sandwichShopRepository.getShops().get(0).getOptions(), ""));
    }

    @Test
    void handleOrderWithSandwichThrowsExceptionWithUnavailableOptions(){
        cut.setTodaysSandwichShop(sandwichShopRepository.getShops().get(0));
        testOrder = new Order(p1, cut.getDayOrder());
        assertThrows(IngredientNotAvailableException.class, () -> cut.handleOrder(testOrder, sandwichShopRepository.getShops().get(0).getSandwiches().get(1), BreadType.GREY, sandwichShopRepository.getShops().get(1).getOptions(), ""));
    }

    @Test
    void findOrdersByDate(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, null);
        littleOrderList.add(testOrder);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertEquals(littleOrderList, cut.findOrdersByDate(LocalDate.now()));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    void findOrdersBetweenDates(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, null);
        littleOrderList.add(testOrder);
        LocalDate date1 = LocalDate.now().minusDays(10);
        LocalDate date2 = LocalDate.now().minusDays(5);
        when(orderRepository.findOrdersByDates(date1, date2)).thenReturn(littleOrderList);
        assertEquals(littleOrderList, cut.findOrdersByDates(date1, date2));
        verify(orderRepository).findOrdersByDates(date1, date2);
    }

    @Test
    void findOrdersBySession(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, null);
        littleOrderList.add(testOrder);
        when(orderRepository.findOrdersBySession(mockSession)).thenReturn(littleOrderList);
        assertEquals(littleOrderList, cut.findOrdersBySession(mockSession));
        verify(orderRepository).findOrdersBySession(mockSession);
    }
    @Test
    void findTodaysOrdersForPersonWithOrderTodayWorks(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, cut.getDayOrder());
        littleOrderList.add(testOrder);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertTrue(cut.findTodaysOrdersForPerson(p1).contains(testOrder));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    void findTodaysOrdersForPersonWithoutOrderTodayWorks(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, cut.getDayOrder());
        littleOrderList.add(testOrder);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertTrue(!cut.findTodaysOrdersForPerson(p2).contains(testOrder));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    void findAllUnhandeledOrders(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, cut.getDayOrder());
        testOrder.setOrderStatus(OrderStatus.HANDELED);
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(testOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.getOrders()).thenReturn(littleOrderList);
        assertTrue(cut.findAllUnhandeledOrders().contains(testOrder2) && !cut.findAllUnhandeledOrders().contains(testOrder));
        verify(orderRepository, times(2)).getOrders();
    }

    @Test
    void findAllUnfilledOrders(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, cut.getDayOrder());
        testOrder.setOrderStatus(OrderStatus.ORDERED);
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(testOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.getOrders()).thenReturn(littleOrderList);
        assertTrue(cut.findAllUnfilledOrders().contains(testOrder2) && !cut.findAllUnfilledOrders().contains(testOrder));
        verify(orderRepository, times(2)).getOrders();
    }

    @Test
    void getAllPersonsFromListOfOrders(){
        List<Order> littleOrderList = new ArrayList<>();
        testOrder = new Order(p1, cut.getDayOrder());
        testOrder.setOrderStatus(OrderStatus.ORDERED);
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(testOrder);
        littleOrderList.add(testOrder2);
        List<Person> myPersonList = cut.getAllPersonsFromListOfOrders(littleOrderList);
        assertTrue(myPersonList.contains(p1) && myPersonList.contains(p2));
    }

    @Test
    void generateOrderFile() throws IOException{
        //TODO find out how to implement this test!
        fail();
    }

    @Test
    void setTodaysSandwichShop(){
        DayOrder before = cut.getDayOrder();
        cut.setTodaysSandwichShop(sandwichShopRepository.getShops().get(0));
        DayOrder after = cut.getDayOrder();
        assertNotEquals(before, after);
    }

    @Test
    void getTodaysSandwichShop() {
        SandwichShop mySandwichShop = sandwichShopRepository.getShops().get(0);
        cut.setTodaysSandwichShop(mySandwichShop);
        assertEquals(mySandwichShop, cut.getTodaysSandwichShop());
    }

    @Test
    public void findTodaysOrderByNameWorksTest() throws PersonNotFoundException {
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        //thirdTestOrder.setOrderStatus(OrderStatus.ORDERED);
        //System.out.println(thirdTestOrder.getOrderNum());
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);

        //Order returnOrder = cut.findTodaysOrderByName("p1 lastname");
        //System.out.println(cut.findTodaysOrderByName("p1 lastname").getOrderNum());
        assertEquals(thirdTestOrder, cut.findTodaysOrderByName("p1 lastname"));

        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findTodaysOrderByNameThrowsException() {
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertThrows(PersonNotFoundException.class, () -> cut.findTodaysOrderByName("Henkie Penkie"));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findTodaysOrderByNameMakesSecondOrder() throws PersonNotFoundException {
        System.out.println(cut.getDayOrder());
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        thirdTestOrder.setOrderStatus(OrderStatus.ORDERED);
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertTrue(cut.findTodaysOrderByName("p1 lastname").getOrderStatus().equals(OrderStatus.UNFILLED));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findClosedOrdersByDatesWorks() throws OrderNotFoundException {
        when(orderRepository.findOrdersByStatusAndDates(any(), any(), any())).thenReturn(new ArrayList<Order>());
        cut.findAllClosedOrdersForDates(LocalDate.now().minusDays(1), LocalDate.now());
        verify(orderRepository).findOrdersByStatusAndDates(any(), any(), any());
    }

    @Test
    public void setTodaysFilledOrdersToHandledTest() throws OrderNotFoundException, NothingToHandleException {
        List<Order> ol = new ArrayList<>();
        ol.add(o5);
        o5.setOrderStatus(OrderStatus.ORDERED);
        when(orderRepository.findOrdersByStatusAndDates(any(), any(), any())).thenReturn(ol);
        cut.setTodaysFilledOrdersToHandeled();
        verify(o5).setOrderStatus(OrderStatus.HANDELED);
    }

    @Test
    public void setTodaysFilledOrdersToHandledThrowsException() throws OrderNotFoundException {
        when(orderRepository.findOrdersByStatusAndDates(any(), any(), any())).thenThrow(OrderNotFoundException.class);
        assertThrows(NothingToHandleException.class, () -> cut.setTodaysFilledOrdersToHandeled());
    }

    @Test
    public void deleteAllUnfilledOrdersOfTheDayWorks() throws OrderNotFoundException {
        List<Order> ol = new ArrayList<>();
        ol.add(o5);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenReturn(ol);
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        verify(orderRepository).deleteOrder(o5);
    }

    @Test
    public void deleteAllUnfilledOrdersOfTheDayWorksForNoSandwich() throws OrderNotFoundException {
        List<Order> ol = new ArrayList<>();
        ol.add(o5);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.NOSANDWICH, LocalDate.now(), LocalDate.now())).thenReturn(ol);
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        verify(orderRepository).deleteOrder(o5);
    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCase() throws OrderNotFoundException, PersonNotFoundException {
        List<Order> olBig = new ArrayList<>();
        List<Order> olSmall = new ArrayList<>();
        olBig.add(o5);
        olBig.add(mockOrder2);
        olSmall.add(o5);
        when(o5.getPerson()).thenReturn(p1);
        when(mockOrder2.getPerson()).thenReturn(p1);
        when(o5.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);
        when(mockOrder2.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenReturn(olSmall);
        when(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now())).thenReturn(olBig);
        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertFalse(check.contains(p1));
        verify(orderRepository).findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now());
        verify(orderRepository).findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now());
    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCaseNoDoubleOrders() throws OrderNotFoundException, PersonNotFoundException {
        List<Order> olBig = new ArrayList<>();
        List<Order> olSmall = new ArrayList<>();
        olBig.add(o5);
        olBig.add(mockOrder2);
        olSmall.add(o5);
        when(o5.getPerson()).thenReturn(p1);
        when(mockOrder2.getPerson()).thenReturn(p2);
        when(o5.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);
        when(mockOrder2.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenReturn(olSmall);
        when(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now())).thenReturn(olSmall);
        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertTrue(check.contains(p1));
        verify(orderRepository).findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now());
        verify(orderRepository).findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now());
    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCaseDoubleEmptyOrder() throws OrderNotFoundException, PersonNotFoundException {
        List<Order> olBig = new ArrayList<>();
        List<Order> olSmall = new ArrayList<>();
        olBig.add(o5);
        olBig.add(mockOrder2);
        olSmall.add(o5);
        when(o5.getPerson()).thenReturn(p1);
        when(mockOrder2.getPerson()).thenReturn(p1);
        when(o5.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);
        when(mockOrder2.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenReturn(olBig);
        when(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now())).thenReturn(olBig);
        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertTrue(check.contains(p1));
        verify(orderRepository).findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now());
        verify(orderRepository).findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now());
    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCaseNoOneHasToOrder() throws OrderNotFoundException {
        List<Order> olBig = new ArrayList<>();
        List<Order> olSmall = new ArrayList<>();
        olBig.add(o5);
        olBig.add(mockOrder2);
        olSmall.add(o5);
        when(o5.getPerson()).thenReturn(p1);
        when(mockOrder2.getPerson()).thenReturn(p1);
        when(o5.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(mockOrder2.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenThrow(OrderNotFoundException.class);
        when(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now())).thenReturn(olBig);

        assertThrows(PersonNotFoundException.class, () -> cut.findWhoStillHasToOrderToday());

        verify(orderRepository).findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now());
    }

    @Test
    public void findWhoStillHasToOrderTodaySomethingGoesReallyWrong() throws OrderNotFoundException, PersonNotFoundException {
        List<Order> olBig = new ArrayList<>();
        List<Order> olSmall = new ArrayList<>();
        olBig.add(o5);
        olBig.add(mockOrder2);
        olSmall.add(o5);
        when(o5.getPerson()).thenReturn(p1);
        when(mockOrder2.getPerson()).thenReturn(p1);
        when(o5.getOrderStatus()).thenReturn(OrderStatus.UNFILLED);
        when(mockOrder2.getOrderStatus()).thenReturn(OrderStatus.ORDERED);
        when(orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now())).thenReturn(olSmall);
        when(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now())).thenThrow(OrderNotFoundException.class);

        assertThrows(PersonNotFoundException.class, () -> cut.findWhoStillHasToOrderToday());

        verify(orderRepository).findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now());
        verify(orderRepository).findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now());
    }


}
