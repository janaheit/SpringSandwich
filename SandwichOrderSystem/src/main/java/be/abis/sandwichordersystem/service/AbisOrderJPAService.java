package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderJpaRepository;
import be.abis.sandwichordersystem.repository.OrderRepository;
import be.abis.sandwichordersystem.repository.SessionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbisOrderJPAService implements OrderJPAService {

    // Possible temporarily given acces to session repo
    @Autowired
    SessionJpaRepository sessionRepository;

    @Autowired
    OrderJpaRepository orderRepository;

    @Autowired
    SessionService sessionService;
    @Autowired
    SandwichJPAService sandwichJPAService;

    private DayOrder dayOrder;

    @Value("${filepath.orderfile}")
    private String filePath = "";

    // Method implementations

    //TODO do checks if order already exists etc.
    @Override
    public boolean addOrder(Order order) {
        orderRepository.save(order);
        return true;
    }

    //TODO OrderNotFoundException should be thrown
    @Override
    public boolean deleteOrder(Order order) throws OrderNotFoundException {
        orderRepository.delete(order);
        return true;
    }

    @Override
    public Order findOrderById(int id) throws OrderNotFoundException {
        Order existing = orderRepository.findOrderById(id);
        if (existing == null) throw new OrderNotFoundException("Order does not exist");
        return existing;
    }

    //TODO OrderNotFoundException should be thrown
    @Transactional
    @Override
    public boolean deleteOrderByID(int id) throws OrderNotFoundException {
        findOrderById(id);
        orderRepository.deleteById(id);
        return true;
    }

    @Override
    public void createOrdersForEveryoneToday() throws SandwichShopNotFoundException {
        if (this.dayOrder == null || this.dayOrder.getCurrentSandwichShop() == null) {
            throw new SandwichShopNotFoundException("No sandwichshop selected for this day");
        } else {
            List<Person> personsFollowingSessionToday = sessionService.findAllPersonsFollowingSessionToday();
            for (Person p : personsFollowingSessionToday) {
                this.createOrder(p);
            }
        }
    }

    // TODO
    @Override
    public Order createOrder(Person person) {
        Order thisOrder = new Order(person, this.dayOrder);
        addOrder(thisOrder);

        // set session on order
        List<Session> sessionsToday = sessionService.findSessionsToday();

        //TODO get students doesn't exist anymore, so fixed with seperate query, TEST!
        for (Session s:sessionsToday){
            if (sessionService.findAllPersonsFollowingSession(s.getSessionNumber()).contains(person)){
                thisOrder.setSession(s);
            }
        }

        return thisOrder;
    }

    @Override
    public void handleOrder(Order order, String remark) {
        order.setOrderStatus(OrderStatus.NOSANDWICH);
        order.setRemark(remark);
    }

    @Override
    public void handleOrder(Order order, Sandwich sandwich, BreadType breadType, List<Options> options, String remark) throws IngredientNotAvailableException, SandwichNotFoundException {
        SandwichShop mySandwichShop = order.getDayOrder().getCurrentSandwichShop();

        if(!sandwichJPAService.checkIfSandwichInShop(sandwich.getSandwichID(), mySandwichShop.getSandwichShopID())) {
            throw new SandwichNotFoundException("Sandwich " + sandwich.getName() + " not available at " + mySandwichShop.getName());
        }
        order.setSandwich(sandwich);

        if(!sandwichJPAService.checkIfBreadTypeInShop(breadType, mySandwichShop.getSandwichShopID())) {
            throw new IngredientNotAvailableException("Breadtype " + breadType.getBreadType() + " not available at " + mySandwichShop.getName());
        }
        order.setBreadType(breadType);
        order.setOptions(sandwichJPAService.checkIfOptionsInShop(options, mySandwichShop.getSandwichShopID()));
        if(order.getOptions().size() != options.size()) {
            throw new IngredientNotAvailableException("Not all options were available!");
        }
        order.setRemark(remark);
        order.setOrderStatus(OrderStatus.ORDERED);
    }

    @Override
    public List<Order> findOrdersByDate(LocalDate date) {
        return orderRepository.findOrdersByDate(date);
    }


    @Override
    public List<Order> findOrdersByDates(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findOrdersByDates(startDate, endDate);
    }

    @Override
    public List<Order> findOrdersBySession(Session session) {
        return orderRepository.findOrdersBySession(session.getSessionNumber());
    }

    //TODO This should probably be implemented a bit better with a separate query
    @Override
    public List<Order> findTodaysOrdersForPerson(Person person) {
        return orderRepository.findOrdersByDate(LocalDate.now()).stream().filter(order -> order.getPerson().equals(person)).collect(Collectors.toList());
    }

    //TODO This should also get its own query
    @Override
    public List<Order> findAllUnhandeledOrders() {
        return orderRepository.getOrders().stream().filter(order -> order.getOrderStatus() != OrderStatus.HANDELED).collect(Collectors.toList());
    }

    //TODO This should also get its own query
    @Override
    public List<Order> findAllUnfilledOrders() {
        return orderRepository.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.UNFILLED).collect(Collectors.toList());
    }

    @Override
    public List<Order> findOrdersByStatusAndSession(OrderStatus status, Session session) throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndSession(status.name(), session.getSessionNumber());
    }

    @Override
    public List<Order> findTodaysFilledOrdersForPerson(Person person) throws OrderNotFoundException {
        return orderRepository.findOrdersByPersonAndDates(person.getPersonNr(), LocalDate.now(), LocalDate.now()).stream()
                .filter(o -> o.getOrderStatus().equals(OrderStatus.ORDERED) || o.getOrderStatus().equals(OrderStatus.NOSANDWICH))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getAllPersonsFromListOfOrders(List<Order> orders) {
        return orders.stream().map(order -> order.getPerson()).collect(Collectors.toList());
    }

    @Override
    public void generateOrderFile() throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String filename = filePath + dayOrder.getDate().format(fmt) + ".txt";

        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.print(todaysOrdersForShop());
        out.close();
    }

    @Override
    public void setTodaysSandwichShop(SandwichShop sandwichShop) {
        if (this.dayOrder == null || !this.dayOrder.getDate().equals(LocalDate.now())) {
            this.dayOrder = new DayOrder(sandwichShop, LocalDate.now());
        }
        else {
            this.dayOrder.setCurrentSandwichShop(sandwichShop);
        }
    }

    @Override
    public List<Order> findOrdersByStatusAndDates(OrderStatus status, LocalDate startDate, LocalDate endDate) throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(status.name(), startDate, endDate);
    }

    @Override
    public SandwichShop getTodaysSandwichShop() throws DayOrderDoesNotExistYet {
        if (this.dayOrder == null) throw new DayOrderDoesNotExistYet("The sandwich shop has not yet been selected.");
        return this.dayOrder.getCurrentSandwichShop();
    }

    @Override
    public List<Sandwich> getTodaysSandwiches() throws DayOrderDoesNotExistYet {
        return sandwichJPAService.getSandwichesForShop(getTodaysSandwichShop().getSandwichShopID());
    }

    @Override
    public List<Options> getTodaysOptions() throws DayOrderDoesNotExistYet {
        return sandwichJPAService.getOptionsForShop(getTodaysSandwichShop().getSandwichShopID());
    }

    @Override
    public List<BreadType> getTodaysBreadTypes() throws DayOrderDoesNotExistYet {
        return sandwichJPAService.getBreadTypesForShop(getTodaysSandwichShop().getSandwichShopID());
    }

    @Override
    public Order findTodaysUnfilledOrderByName(String name) throws PersonNotFoundException {
        List<Order> myOrderList = orderRepository.findOrdersByDate(LocalDate.now()).stream().filter(order -> (order.getPerson().getFirstName() + " " + order.getPerson().getLastName()).equalsIgnoreCase(name)).collect(Collectors.toList());
        if (myOrderList.size()==0) {
            throw new PersonNotFoundException("This person was not found in a session today");
        } else {
            if (myOrderList.get(myOrderList.size()-1).getOrderStatus()==OrderStatus.UNFILLED) {
                return myOrderList.get(myOrderList.size()-1);
            } else {
                return createOrder(myOrderList.get(myOrderList.size()-1).getPerson());
            }
        }
    }

    @Override
    public List<Order> findAllFilledOrdersForToday() throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED.name(), LocalDate.now(), LocalDate.now());
    }

    // Simple getters and setters
    public DayOrder getDayOrder() {
        //if (dayOrder == null) throw new DayOrderDoesNotExistYet("The day order was not initialised yet.");
        return dayOrder;
    }

    public OrderJpaRepository getOrderRepository(){
        return this.orderRepository;
    }

    @Override
    public void setOrderRepository(OrderJpaRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void setDayOrder(DayOrder dayOrder) {
        this.dayOrder = dayOrder;
    }

    // Other helper methods
    public String todaysOrdersForShop() {
        StringBuilder output = new StringBuilder();
        List<Order> todaysOrders = this.findOrdersByDate(LocalDate.now()).stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.ORDERED)
                .sorted(Comparator.comparing(o -> o.getPerson().getFirstName()))
                .sorted(Comparator.comparing(o -> o.getSession().getSessionNumber()))
                .collect(Collectors.toList());

        String currentSession = "";
        for (Order order : todaysOrders) {
            if(order.getSession().getCourse().getTitle() != currentSession) {
                currentSession = order.getSession().getCourse().getTitle();
                output.append("\n").append(currentSession.toUpperCase()).append(":\n-------------------------------------------------------------------------------\n");
            }
            StringBuilder options = new StringBuilder();
            for (Options option : order.getOptions()) {
                options.append(option.getOption()).append(" ");
            }
            output.append(String.format("%1$-10s%2$-40s%3$-8s%4$-15s\n",order.getPerson().getFirstName(), order.getSandwich().getName(), order.getBreadType().getBreadType().toUpperCase(), options));
            if (order.getRemark().length() > 0) {
                output.append(String.format("%2$-10s%1$-7s\n", order.getRemark().toUpperCase(), ""));
            }
        }

        return output.toString();

    }

    @Override
    public List<Order> findAllClosedOrdersForDates(LocalDate startDate, LocalDate endDate) throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.HANDELED.name(), startDate, endDate);
    }

    @Override
    public void setTodaysFilledOrdersToHandeled() throws NothingToHandleException {

            List<Order> myOrderList = orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED.name(), LocalDate.now(), LocalDate.now());
            if (myOrderList.size() == 0) {
                throw new NothingToHandleException("No orders were found that could be handled today");
            }

            for (Order order : myOrderList) {
                order.setOrderStatus(OrderStatus.HANDELED);
            }


    }

    @Override
    public void deleteAllUnfilledOrdersOfDay(LocalDate date) throws OrderNotFoundException {
        List<Order> myOrderList = orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED.name(), date, date);
        for (Order order : myOrderList) {
            orderRepository.delete(order);
        }

            List<Order> noSandwichList = orderRepository.findOrdersByStatusAndDates(OrderStatus.NOSANDWICH.name(), date, date);
            for (Order order : noSandwichList) {
                orderRepository.delete(order);
            }
    }

    @Override
    public List<Person> findWhoStillHasToOrderToday() throws PersonNotFoundException {

            List<Person> unfilledOrders = orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED.name(), LocalDate.now(), LocalDate.now()).stream().map(order -> order.getPerson()).distinct().collect(Collectors.toList());
            List<Person> output = new ArrayList<>();

            if (unfilledOrders.size() == 0) {
                throw new PersonNotFoundException("No Persons found that still have to order today!");
            }

            // Check for double orders
            for (Person p : unfilledOrders) {

                    List<Order> personsOrderOfToday = orderRepository.findOrdersByPersonAndDates(p.getPersonNr(), LocalDate.now(), LocalDate.now());
                    //System.out.println(personsOrderOfToday.size());
                    if (personsOrderOfToday.size() == 1) {
                        output.add(p);
                    } else {
                        boolean stat = true;
                        for (Order o : personsOrderOfToday) {
                            if (o.getOrderStatus() != OrderStatus.UNFILLED) {
                                stat = false;
                                break;
                            }
                        }
                        if(stat) {
                            output.add(p);
                        }
                    }

            }
            return output;


    }

    // Other methods for mock testing
    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public Order findOrder(Order order) {
        return orderRepository.checkIfOrderExists(order.getSandwich().getSandwichID(), order.getBreadType().name(), order.getRemark(), order.getOrderStatus().name(), order.getAmount(), order.getPrice(), order.getDate(), order.getSandwichShop().getSandwichShopID(), order.getPerson().getPersonNr(), order.getSession().getSessionNumber());
    }
}
