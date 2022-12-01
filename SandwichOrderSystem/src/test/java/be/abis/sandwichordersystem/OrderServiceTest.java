package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.*;
import be.abis.sandwichordersystem.service.OrderJPAService;
import be.abis.sandwichordersystem.service.OrderService;
import be.abis.sandwichordersystem.service.SandwichJPAService;
import be.abis.sandwichordersystem.service.SessionService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderJPAService cut;
    @Autowired
    PersonJpaRepository personRepository;
    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    @Autowired
    SandwichJPAService sandwichService;

    @Autowired
    SessionService sessionService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SandwichJPARepository sandwichRepository;

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
    Student p3;

    Order testOrder;


    @BeforeEach
    void setUp(){
        cut.setDayOrder(null);
        testOrder = null;

        p1 = personRepository.findStudentByID(1);
        p2 = personRepository.findStudentByID(2);
        p3 = personRepository.findStudentByID(6);
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
    @Transactional
    void addOrderWorks() throws OrderAlreadyExistsException {
        cut.addOrder(order1);
        assertTrue(orderRepository.getOrders().contains(order1));
        //System.out.println(orderService.getOrderRepository().getOrders());
    }

    @Test
    @Transactional
    void deleteOrderWorks() throws OrderNotFoundException, OrderAlreadyExistsException {
        cut.addOrder(order1);
        int amountBeforeTest = orderRepository.getOrders().size();
        cut.deleteOrder(order1);
        assertEquals(amountBeforeTest-1, orderRepository.getOrders().size());
    }


    @Test
    void createOrdersForEveryoneTodayThrowsExceptionIfNoSandwichShopIsSetTest() {
        assertThrows(SandwichShopNotFoundException.class, () -> cut.createOrdersForEveryoneToday());
    }

    @Test
    @Transactional
    void createOrdersForEveryoneToday() throws SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));

        cut.createOrdersForEveryoneToday();

        List<Order> myOrders = orderRepository.findOrdersByDate(LocalDate.now());
        assertEquals(4, myOrders.size());

    }

    @Test
    @Transactional
    void createOrderForPerson() throws OrderAlreadyExistsException {

        Order myOrder = cut.createOrder(p1);
        assertEquals(p1, myOrder.getPerson());

    }

    @Test
    void handleOrderWithNoSandwich() throws DayOrderDoesNotExistYet {
        testOrder = new Order(p1, cut.getDayOrder());
        cut.handleOrder(testOrder, "test");
        assertEquals(OrderStatus.NOSANDWICH, testOrder.getOrderStatus());
    }


    @Test
    void handleOrderWithSandwichWorks() throws IngredientNotAvailableException, SandwichNotFoundException, DayOrderDoesNotExistYet {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = new Order(p1, cut.getDayOrder());
        cut.handleOrder(testOrder, sandwichService.getSandwichesForShop(2).get(1), BreadType.GREY, sandwichService.getOptionsForShop(2), "");
        assertEquals(OrderStatus.ORDERED, testOrder.getOrderStatus());
    }

    @Test
    void handleOrderWithSandwichThrowsExceptionWithUnavailableSandwich() throws DayOrderDoesNotExistYet {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = new Order(p1, cut.getDayOrder());
        assertThrows(SandwichNotFoundException.class, () -> cut.handleOrder(testOrder, sandwichService.getSandwichesForShop(2).get(1), BreadType.GREY, sandwichService.getOptionsForShop(2), ""));
    }

    @Test
    void handleOrderWithSandwichThrowsExceptionWithUnavailableOptions() throws DayOrderDoesNotExistYet {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = new Order(p1, cut.getDayOrder());
        assertThrows(IngredientNotAvailableException.class, () -> cut.handleOrder(testOrder, sandwichService.getSandwichesForShop(2).get(1), BreadType.GREY, sandwichService.getOptionsForShop(2), ""));
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
    @Transactional
    void findOrdersBySession() throws SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        List<Order> myOrderList = cut.findOrdersBySession(p1.getCurrentSession());
        assertEquals(2, myOrderList.size());
    }
    @Test
    @Transactional
    void findTodaysOrdersForPersonWithOrderTodayWorks() throws DayOrderDoesNotExistYet, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        List<Order> myOrderList = cut.findTodaysOrdersForPerson(p1);
        assertEquals(1, myOrderList.size());
    }

    @Test
    @Transactional
    void findTodaysOrdersForPersonWithoutOrderTodayWorks() throws DayOrderDoesNotExistYet, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        List<Order> myOrderList = cut.findTodaysOrdersForPerson(p3);
        assertEquals(0, myOrderList.size());
    }

    //TODO FROM HERE ON I SHOULD CHECK, NO PROPER TESTING YET
    @Test
    void findAllUnhandeledOrders() throws DayOrderDoesNotExistYet {
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
    void findAllUnfilledOrders() throws DayOrderDoesNotExistYet {
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
    void getAllPersonsFromListOfOrders() throws DayOrderDoesNotExistYet {
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
    void setTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        DayOrder before = cut.getDayOrder();
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(0));
        DayOrder after = cut.getDayOrder();
        assertNotEquals(before, after);
    }

    @Test
    void getTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        SandwichShop mySandwichShop = sandwichService.getSandwichShops().get(0);
        cut.setTodaysSandwichShop(mySandwichShop);
        assertEquals(mySandwichShop, cut.getTodaysSandwichShop());
    }

    @Test
    public void findTodaysOrderByNameWorksTest() throws PersonNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        //thirdTestOrder.setOrderStatus(OrderStatus.ORDERED);
        //System.out.println(thirdTestOrder.getOrderNum());
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);

        //Order returnOrder = cut.findTodaysUnfilledOrderByName("p1 lastname");
        //System.out.println(cut.findTodaysUnfilledOrderByName("p1 lastname").getOrderNum());
        assertEquals(thirdTestOrder, cut.findTodaysUnfilledOrderByName("p1 lastname"));

        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findTodaysOrderByNameThrowsException() throws DayOrderDoesNotExistYet {
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertThrows(PersonNotFoundException.class, () -> cut.findTodaysUnfilledOrderByName("Henkie Penkie"));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findTodaysOrderByNameMakesSecondOrder() throws PersonNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        System.out.println(cut.getDayOrder());
        List<Order> littleOrderList = new ArrayList<>();
        Order thirdTestOrder = new Order(p1, cut.getDayOrder());
        thirdTestOrder.setOrderStatus(OrderStatus.ORDERED);
        Order testOrder2 = new Order(p2, cut.getDayOrder());
        littleOrderList.add(thirdTestOrder);
        littleOrderList.add(testOrder2);
        when(orderRepository.findOrdersByDate(LocalDate.now())).thenReturn(littleOrderList);
        assertTrue(cut.findTodaysUnfilledOrderByName("p1 lastname").getOrderStatus().equals(OrderStatus.UNFILLED));
        verify(orderRepository).findOrdersByDate(LocalDate.now());
    }

    @Test
    public void findClosedOrdersByDatesWorks() throws OrderNotFoundException {
        when(orderRepository.findOrdersByStatusAndDates(any(), any(), any())).thenReturn(new ArrayList<Order>());
        cut.findAllClosedOrdersForDates(LocalDate.now().minusDays(1), LocalDate.now());
        verify(orderRepository).findOrdersByStatusAndDates(any(), any(), any());
    }

    // From here down i checked
    @Test
    @Transactional
    public void setTodaysFilledOrdersToHandledTest() throws OrderNotFoundException, NothingToHandleException, SandwichShopNotFoundException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now()).get(0), "no");
        cut.setTodaysFilledOrdersToHandeled();
        List<Order> myOrderList = cut.findTodaysOrdersForPerson(p1);
        assertEquals(OrderStatus.HANDELED, myOrderList.get(0).getOrderStatus());

    }

    @Test
    @Transactional
    public void setTodaysFilledOrdersToHandledThrowsException() throws OrderNotFoundException, SandwichShopNotFoundException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        assertThrows(NothingToHandleException.class, () -> cut.setTodaysFilledOrdersToHandeled());
    }

    @Test
    @Transactional
    public void deleteAllUnfilledOrdersOfTheDayWorks() throws OrderNotFoundException, OperationNotAllowedException, SandwichShopNotFoundException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        int amountbefore = orderRepository.getOrders().size();
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        int amountafter = orderRepository.getOrders().size();
        assertEquals(amountbefore-4, amountafter);

    }

    @Test
    @Transactional
    public void deleteAllUnfilledOrdersOfTheDayWorksForNoSandwich() throws OrderNotFoundException, OperationNotAllowedException, SandwichShopNotFoundException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now()).get(0), "No sandwich");
        int amountbefore = orderRepository.getOrders().size();
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        int amountafter = orderRepository.getOrders().size();
        assertEquals(amountbefore-4, amountafter);
    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCase() throws OrderNotFoundException, PersonNotFoundException, SandwichShopNotFoundException, OrderAlreadyExistsException {

        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now()).get(0), "No sandwich");

        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertEquals(3, check.size());

    }

    @Test
    @Transactional
    public void findWhoStillHasToOrderTodayHappyCaseWithDoubleOrders() throws OrderNotFoundException, PersonNotFoundException, SandwichShopNotFoundException, SandwichNotFoundException, IngredientNotAvailableException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1, LocalDate.now(), LocalDate.now()).get(0), sandwichService.getSandwichesForShop(2).get(2), BreadType.GREY, new ArrayList<Options>(), "All good");
        cut.createOrder(p1);


        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertFalse(check.contains(p1));

    }

    @Test
    @Transactional
    public void findWhoStillHasToOrderTodayHappyCaseDoubleEmptyOrder() throws OrderNotFoundException, PersonNotFoundException, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.createOrder(p1);

        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertTrue(check.contains(p1));

    }

    @Test
    public void findWhoStillHasToOrderTodayHappyCaseNoOneHasToOrder() throws OrderNotFoundException {


        assertThrows(PersonNotFoundException.class, () -> cut.findWhoStillHasToOrderToday());

    }


    @Test
    public void findAllFilledOrdersForTodayTest() {
        //TODO still have to implement this test!
        fail();
    }




}
