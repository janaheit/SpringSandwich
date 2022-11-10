package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    SandwichShopService sandwichShopService;

    private DayOrder dayOrder;

    @Value("${filepath.orderfile}")
    private String filePath = "";

    // Method implementations

    @Override
    public boolean addOrder(Order order) {
        return orderRepository.addOrder(order);
    }

    @Override
    public boolean deleteOrder(Order order) throws OrderNotFoundException {
        return orderRepository.deleteOrder(order);
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

        for (Session s:sessionsToday){
            if (s.getStudents().contains(person)){
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

        if(!sandwichShopService.checkSandwich(sandwich, mySandwichShop)) {
            throw new SandwichNotFoundException("Sandwich " + sandwich.getName() + " not available at " + mySandwichShop.getName());
        }
        order.setSandwich(sandwich);

        if(!sandwichShopService.checkBreadType(breadType, mySandwichShop)) {
            throw new IngredientNotAvailableException("Breadtype " + breadType.getBreadType() + " not available at " + mySandwichShop.getName());
        }
        order.setBreadType(breadType);
        order.setOptions(sandwichShopService.checkOptions(options, mySandwichShop));
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
        return orderRepository.findOrdersBySession(session);
    }

    @Override
    public List<Order> findTodaysOrdersForPerson(Person person) {
        return orderRepository.findOrdersByDate(LocalDate.now()).stream().filter(order -> order.getPerson().equals(person)).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllUnhandeledOrders() {
        return orderRepository.getOrders().stream().filter(order -> order.getOrderStatus() != OrderStatus.HANDELED).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllUnfilledOrders() {
        return orderRepository.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.UNFILLED).collect(Collectors.toList());
    }

    @Override
    public List<Order> findOrdersByStatusAndSession(OrderStatus status, Session session) throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndSession(status, session);
    }

    @Override
    public List<Order> findTodaysFilledOrdersForPerson(Person person) throws OrderNotFoundException {
        return orderRepository.findOrdersByPersonAndDates(person, LocalDate.now(), LocalDate.now()).stream()
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
    public SandwichShop getTodaysSandwichShop() {
        return this.dayOrder.getCurrentSandwichShop();
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
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED, LocalDate.now(), LocalDate.now());
    }

    // Simple getters and setters
    public DayOrder getDayOrder() {
        return dayOrder;
    }

    public OrderRepository getOrderRepository(){
        return this.orderRepository;
    }

    @Override
    public void setOrderRepository(OrderRepository orderRepository) {
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
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.HANDELED, startDate, endDate);
    }

    @Override
    public void setTodaysFilledOrdersToHandeled() throws NothingToHandleException {
        try {
            List<Order> myOrderList = orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED, LocalDate.now(), LocalDate.now());
            for (Order order : myOrderList) {
                order.setOrderStatus(OrderStatus.HANDELED);
            }
        } catch(OrderNotFoundException ex) {
            throw new NothingToHandleException("No orders were found that could be handled today");
        }
    }

    @Override
    public void deleteAllUnfilledOrdersOfDay(LocalDate date) throws OrderNotFoundException {
        List<Order> myOrderList = orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, date, date);
        for (Order order : myOrderList) {
            orderRepository.deleteOrder(order);
        }
        try {
            List<Order> noSandwichList = orderRepository.findOrdersByStatusAndDates(OrderStatus.NOSANDWICH, date, date);
            for (Order order : noSandwichList) {
                orderRepository.deleteOrder(order);
            }
        } catch(OrderNotFoundException ex) {
            System.out.println("Everyone wants a Sandwich today, nothing to worry about.");
        }
    }

    @Override
    public List<Person> findWhoStillHasToOrderToday() throws PersonNotFoundException {
        try {
            List<Person> unfilledOrders = orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED, LocalDate.now(), LocalDate.now()).stream().map(order -> order.getPerson()).distinct().collect(Collectors.toList());
            List<Person> output = new ArrayList<>();
            // Check for double orders
            for (Person p : unfilledOrders) {
                try {
                    List<Order> personsOrderOfToday = orderRepository.findOrdersByPersonAndDates(p, LocalDate.now(), LocalDate.now());
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
                } catch (OrderNotFoundException e) {
                    // Doesn't work, just throws PersonNotFoundException below. But nvm
                    throw new OrderNotFoundException("Something went wrong in checking for double orders: " + e.getMessage());
                }
            }
            return output;
        } catch (OrderNotFoundException e) {
            throw new PersonNotFoundException("No Persons found that still have to order today!");
        }

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
}
