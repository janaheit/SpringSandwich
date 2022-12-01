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
    OrderJpaRepository orderRepository;
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
    @Transactional
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
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        Order myOrder = cut.createOrder(p1);
        assertEquals(p1, myOrder.getPerson());

    }

    @Test
    @Transactional
    void handleOrderWithNoSandwich() throws DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);
        cut.handleOrder(testOrder, "test");
        assertEquals(OrderStatus.NOSANDWICH, orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0).getOrderStatus());
    }


    @Test
    @Transactional
    void handleOrderWithSandwichWorks() throws IngredientNotAvailableException, SandwichNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);
        cut.handleOrder(testOrder, 3, BreadType.GREY, sandwichService.getOptionsForShop(2), "handleOrderWithSandwichWorks JUNIT test");
        assertEquals(OrderStatus.ORDERED, orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0).getOrderStatus());
    }

    @Test
    @Transactional
    void handleOrderWithSandwichThrowsExceptionWithUnavailableSandwich() throws DayOrderDoesNotExistYet, OrderAlreadyExistsException, SandwichNotFoundException, IngredientNotAvailableException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);

        assertThrows(SandwichNotFoundException.class, () -> cut.handleOrder(testOrder, 51, BreadType.GREY, sandwichService.getOptionsForShop(2), "handleOrderWithSandwichWorks JUNIT test"));
    }

    @Test
    @Transactional
    void handleOrderWithSandwichThrowsExceptionWithUnavailableOptions() throws DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);
        assertThrows(IngredientNotAvailableException.class, () -> cut.handleOrder(testOrder, 5, BreadType.GREY, sandwichService.getOptionsForShop(3), ""));
    }

    @Test
    void findOrdersByDate(){

        assertEquals(2, cut.findOrdersByDate(LocalDate.of(2022, 11, 30)).size());

    }

    @Test
    void findOrdersBetweenDates(){

        assertTrue(cut.findOrdersByDates(LocalDate.of(2022, 10, 1), LocalDate.of(2022, 11, 1)).get(0).getOrderNum() == 3);
    }

    @Test
    @Transactional
    void findOrdersBySession() throws SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        List<Order> myOrderList = cut.findOrdersBySession(p1.getCurrentSession());
        assertEquals(3, myOrderList.size());
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


    @Test
    void findAllUnhandeledOrders() throws DayOrderDoesNotExistYet {

        assertEquals(3, cut.findAllUnhandeledOrders().size());

    }


    @Test
    void findAllUnfilledOrders() throws DayOrderDoesNotExistYet {
        assertEquals(2, cut.findAllUnfilledOrders().size());
    }

    @Test
    @Transactional
    void getAllPersonsFromListOfOrders() throws DayOrderDoesNotExistYet {
        List<Order> littleOrderList = orderRepository.getOrders();

        List<Person> myPersonList = cut.getAllPersonsFromListOfOrders(littleOrderList);
        assertTrue(myPersonList.contains(p1) && myPersonList.contains(p2));
    }

    @Test
    void generateOrderFile() throws IOException{
        //TODO find out how to implement this test!

    }

    @Test
    void setTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        DayOrder before = cut.getDayOrder();
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(0));
        DayOrder after = cut.getDayOrder();
        assertNotEquals(before, after);
    }

    @Test
    @Transactional
    void getTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        SandwichShop mySandwichShop = sandwichService.getSandwichShops().get(0);
        cut.setTodaysSandwichShop(mySandwichShop);
        assertEquals(mySandwichShop, cut.getTodaysSandwichShop());
    }

    @Test
    @Transactional
    public void findTodaysOrderByNameWorksTest() throws PersonNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);

        assertEquals(testOrder.getOrderNum(), cut.findTodaysUnfilledOrderByName(p1.getFirstName()+p1.getLastName()).getOrderNum());

    }



    @Test
    public void findTodaysOrderByNameThrowsException() throws DayOrderDoesNotExistYet {

        assertThrows(PersonNotFoundException.class, () -> cut.findTodaysUnfilledOrderByName("Henkie Penkie"));

    }


    @Test
    @Transactional
    public void findTodaysOrderByNameMakesSecondOrder() throws PersonNotFoundException, DayOrderDoesNotExistYet, OrderAlreadyExistsException, SandwichNotFoundException, IngredientNotAvailableException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);
        cut.handleOrder(testOrder, 3, BreadType.GREY, sandwichService.getOptionsForShop(2), "findTodaysOrderByName JUNIT test");

        assertTrue(cut.findTodaysUnfilledOrderByName(p1.getFirstName()+p1.getLastName()).getOrderStatus().equals(OrderStatus.UNFILLED));

    }


    @Test
    @Transactional
    public void findClosedOrdersByDatesWorks() throws OrderNotFoundException, OrderAlreadyExistsException, SandwichNotFoundException, IngredientNotAvailableException {
        cut.setTodaysSandwichShop(sandwichService.getSandwichShops().get(1));
        testOrder = cut.createOrder(p1);
        cut.handleOrder(testOrder, 3, BreadType.GREY, sandwichService.getOptionsForShop(2), "findClosedOrdersByDates JUNIT test");
        testOrder.setOrderStatus(OrderStatus.HANDELED);
        orderRepository.updateHandleOrder(testOrder.getOrderNum(), testOrder.getSandwich().getSandwichID(), testOrder.getBreadType().name(), testOrder.getRemark(), testOrder.getOrderStatus().name(), testOrder.getAmount(), testOrder.getPrice(), testOrder.getDate(), testOrder.getSandwichShop().getSandwichShopID(), testOrder.getPerson().getPersonNr(), testOrder.getSession().getSessionNumber());


        Order foundOrder = cut.findAllClosedOrdersForDates(LocalDate.now().minusDays(1), LocalDate.now()).get(0);
        assertEquals(testOrder, foundOrder);

    }

    // From here down i checked
    @Test
    @Transactional
    public void setTodaysFilledOrdersToHandledTest() throws OrderNotFoundException, NothingToHandleException, SandwichShopNotFoundException, OrderAlreadyExistsException, SandwichNotFoundException, IngredientNotAvailableException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();

        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0), 3, BreadType.GREY, sandwichService.getOptionsForShop(2), "findClosedOrdersByDates JUNIT test");

        cut.setTodaysFilledOrdersToHandeled();
        List<Order> myOrderList = cut.findTodaysOrdersForPerson(p1);
        assertEquals(OrderStatus.HANDELED, myOrderList.get(0).getOrderStatus());

    }

    @Test
    @Transactional
    public void setTodaysFilledOrdersToHandledThrowsException() throws OrderNotFoundException, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        assertThrows(NothingToHandleException.class, () -> cut.setTodaysFilledOrdersToHandeled());
    }

    @Test
    @Transactional
    public void deleteAllUnfilledOrdersOfTheDayWorks() throws OrderNotFoundException, OperationNotAllowedException, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        int amountbefore = orderRepository.getOrders().size();
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        int amountafter = orderRepository.getOrders().size();
        assertEquals(amountbefore-4, amountafter);

    }

    @Test
    @Transactional
    public void deleteAllUnfilledOrdersOfTheDayWorksForNoSandwich() throws OrderNotFoundException, OperationNotAllowedException, SandwichShopNotFoundException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0), "No sandwich");
        int amountbefore = orderRepository.getOrders().size();
        cut.deleteAllUnfilledOrdersOfDay(LocalDate.now());
        int amountafter = orderRepository.getOrders().size();
        assertEquals(amountbefore-4, amountafter);
    }

    @Test
    @Transactional
    public void findWhoStillHasToOrderTodayHappyCase() throws OrderNotFoundException, PersonNotFoundException, SandwichShopNotFoundException, OrderAlreadyExistsException {

        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0), "No sandwich");

        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertEquals(3, check.size());

    }

    @Test
    @Transactional
    public void findWhoStillHasToOrderTodayHappyCaseWithDoubleOrders() throws OrderNotFoundException, PersonNotFoundException, SandwichShopNotFoundException, SandwichNotFoundException, IngredientNotAvailableException, OrderAlreadyExistsException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();
        cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0), 7, BreadType.GREY, new ArrayList<Options>(), "All good");
        cut.createOrder(p1);


        List<Person> check = cut.findWhoStillHasToOrderToday();
        //System.out.println(check);
        assertFalse(check.contains(p1));

    }


    @Test
    public void findWhoStillHasToOrderTodayHappyCaseNoOneHasToOrder() throws OrderNotFoundException {


        assertThrows(PersonNotFoundException.class, () -> cut.findWhoStillHasToOrderToday());

    }


    @Test
    @Transactional
    public void findAllFilledOrdersForTodayTest() throws OrderNotFoundException, OrderAlreadyExistsException, SandwichShopNotFoundException, SandwichNotFoundException, IngredientNotAvailableException {
        cut.setTodaysSandwichShop(sandwichShopRepository.findShopById(2));
        cut.createOrdersForEveryoneToday();

        Order myOrder = cut.handleOrder(orderRepository.findOrdersByPersonAndDates(p1.getPersonNr(), LocalDate.now(), LocalDate.now()).get(0), 3, BreadType.GREY, sandwichService.getOptionsForShop(2), "findAllFilledOrdersForToday JUNIT test");

        List<Order> myOrderList = cut.findAllFilledOrdersForToday();

        assertTrue(myOrderList.contains(myOrder));

    }




}
