package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.*;
import be.abis.sandwichordersystem.model.*;
import be.abis.sandwichordersystem.repository.OrderJpaRepository;
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
    PersonService personService;

    @Autowired
    SessionService sessionService;
    @Autowired
    SandwichJPAService sandwichJPAService;

    private DayOrder dayOrder;

    @Value("${filepath.orderfile}")
    private String filePath = "";

    // Method implementations

    //TODO do checks if order already exists etc.
    @Transactional
    @Override
    public Order addOrder(Order order) throws OrderAlreadyExistsException {
        try {
            findOrder(order);
            throw new OrderAlreadyExistsException("This order already exists");
        } catch (OrderNotFoundException e) {
            return orderRepository.save(order);
        }
    }

    @Transactional
    @Override
    public boolean deleteOrder(Order order) throws OrderNotFoundException {
        findOrder(order);
        orderRepository.delete(order);
        return true;
    }

    @Override
    public Order findOrderById(int id) throws OrderNotFoundException {
        Order existing = orderRepository.findOrderById(id);
        if (existing == null) throw new OrderNotFoundException("Order does not exist");
        return existing;
    }

    @Transactional
    @Override
    public boolean deleteOrderByID(int id) throws OrderNotFoundException {
        findOrderById(id);
        orderRepository.deleteById(id);
        return true;
    }

    @Override
    public void createOrdersForEveryoneToday() throws SandwichShopNotFoundException, OrderAlreadyExistsException {
        if (this.dayOrder == null || this.dayOrder.getCurrentSandwichShop() == null) {
            throw new SandwichShopNotFoundException("No sandwichshop selected for this day");
        } else {
            List<Person> personsFollowingSessionToday = sessionService.findAllPersonsFollowingSessionToday();
            for (Person p : personsFollowingSessionToday) {
                try {
                    this.createOrder(p);
                } catch (OrderAlreadyExistsException e){
                    System.out.println("This order already exists");
                }
            }
        }
    }

    @Override
    public Order createOrder(Person person) throws OrderAlreadyExistsException {
        Order thisOrder = new Order(person, this.dayOrder);

        // set session on order
        Session s;
        if (person instanceof Student) {
            s = ((Student) person).getCurrentSession();
        } else if (person instanceof Instructor){
            s = ((Instructor) person).getCurrentSession();
        } else {
            // TODO Exception??
            System.out.println("You should not be able to order, you are not an instructor or a student");
            return null;
        }
        thisOrder.setSession(s);
        addOrder(thisOrder);

        /*
        // set session on order
        List<Session> sessionsToday = sessionService.findSessionsToday();

        for (Session s:sessionsToday){
            if (sessionService.findAllPersonsFollowingSession(s.getSessionNumber()).contains(person)){
                thisOrder.setSession(s);
            }
        }

         */

        return thisOrder;
    }

    @Override
    public Order handleOrder(Order order, String remark) {
        order.setOrderStatus(OrderStatus.NOSANDWICH);
        order.setRemark(remark);
        return orderRepository.save(order);
    }


    @Override
    @Transactional
    public Order handleOrder(Order order, int sandwichID, BreadType breadType, List<Options> options, String remark, int amount) throws IngredientNotAvailableException, SandwichNotFoundException {
        SandwichShop mySandwichShop = order.getDayOrder().getCurrentSandwichShop();
        System.out.println("started handle order");

        if(!sandwichJPAService.checkIfSandwichInShop(sandwichID, mySandwichShop.getSandwichShopID())) {
            throw new SandwichNotFoundException("Sandwich " + sandwichID + " not available at " + mySandwichShop.getName());
        }
        Sandwich s = sandwichJPAService.findSandwichById(sandwichID);
        order.setSandwich(s);

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
        order.setAmount(amount);
        System.out.println("order handeled");
        orderRepository.updateHandleOrder(order.getOrderNum(), order.getSandwich().getSandwichID(), order.getBreadType().name(), order.getRemark(), order.getOrderStatus().name(), order.getAmount(), order.getPrice(), order.getDate(), order.getSandwichShop().getSandwichShopID(), order.getPerson().getPersonNr(), order.getSession().getSessionNumber());
        //return orderRepository.save(order);
        return order;
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

    @Override
    public List<Order> findTodaysOrdersForPerson(Person person) {
        return orderRepository.findOrdersByPersonAndDates(person.getPersonNr(), LocalDate.now(), LocalDate.now());

    }

    @Override
    public List<Order> findOrdersForPersonAndDates(Person person, LocalDate startDate, LocalDate endDate) {
        return orderRepository.findOrdersByPersonAndDates(person.getPersonNr(), startDate, endDate);
    }

    @Override
    public List<Order> findAllUnhandeledOrders() {
        // takes in the HANDELED status and in the repository it queries on NOT handled
        return orderRepository.findAllUnhandledOrders(OrderStatus.HANDELED.name());
        //return orderRepository.getOrders().stream().filter(order -> order.getOrderStatus() != OrderStatus.HANDELED).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllUnfilledOrders() {
        return orderRepository.findAllUnfilledOrders(OrderStatus.UNFILLED.name());
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
        return orders.stream().map(Order::getPerson).collect(Collectors.toList());
    }

    @Override
    public String generateOrderFile() throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String filename = filePath + LocalDate.now().format(fmt) + ".txt";

        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.print(todaysOrdersForShop());
        out.close();
        return filename;
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
    @Transactional
    public Order findTodaysUnfilledOrderByName(String name) throws PersonNotFoundException, OrderAlreadyExistsException, DayOrderDoesNotExistYet {
            // make sure day order is set
            if (this.dayOrder == null){
                throw new DayOrderDoesNotExistYet("Day order is null");
            }

        Person myPerson = personService.findPersonByName(name);
        List<Order> myOrderList = orderRepository.findOrdersByPersonAndDates(myPerson.getPersonNr(), LocalDate.now(), LocalDate.now());//.stream()
                //.filter(order -> (order.getPerson().getFirstName() + order.getPerson().getLastName()).equalsIgnoreCase(name))
                //.collect(Collectors.toList());
        if (myOrderList.size()==0) {
            throw new PersonNotFoundException("This person was not found in a session today");
        } else {
            List<Order> myUnfilledOrderList = myOrderList.stream().filter(order -> (order.getOrderStatus() == OrderStatus.UNFILLED)).collect(Collectors.toList());
            if (myUnfilledOrderList.size()>0) {
                System.out.println("There was an unfilled order that is returned: " + myUnfilledOrderList.get(myUnfilledOrderList.size()-1).getOrderNum());
                return myUnfilledOrderList.get(myUnfilledOrderList.size()-1);
            } else {
                System.out.println("There wasn't an unfiled order, new one is made: " + myOrderList);
                return createOrder(myOrderList.get(myOrderList.size()-1).getPerson());
            }
        }
    }

    @Override
    public List<Order> findAllFilledOrdersForToday() throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED.name(), LocalDate.now(), LocalDate.now());
    }

    @Override
    public List<Order> findOrdersWithoutSandwichToday() throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.NOSANDWICH.name(), LocalDate.now(), LocalDate.now());
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
                .filter(o -> o.getOrderStatus() == OrderStatus.HANDELED)
                .sorted(Comparator.comparing(o -> o.getPerson().getFirstName()))
                .sorted(Comparator.comparing(o -> o.getSession().getSessionNumber()))
                .collect(Collectors.toList());

        System.out.println(todaysOrders);

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
            if (order.getRemark() != null) {
                output.append(String.format("%2$-10s%1$-7s\n", order.getRemark().toUpperCase(), ""));
            }
        }

        return output.toString();

    }

    @Override
    public List<Order> findAllClosedOrdersForDates(LocalDate startDate, LocalDate endDate) throws OrderNotFoundException {
        return orderRepository.findOrdersByStatusAndDates(OrderStatus.HANDELED.name(), startDate, endDate);
    }

    @Transactional
    @Override
    public void setTodaysFilledOrdersToHandeled() throws NothingToHandleException {

        List<Order> myOrderList = orderRepository.findOrdersByStatusAndDates(OrderStatus.ORDERED.name(), LocalDate.now(), LocalDate.now());
        System.out.println(myOrderList);
        if (myOrderList.size() == 0) {
            throw new NothingToHandleException("No orrtheders were found that could be handled today");
        }

        for (Order order : myOrderList) {
            order.setOrderStatus(OrderStatus.HANDELED);
            orderRepository.save(order);
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

            List<Person> unfilledOrders = orderRepository.findOrdersByStatusAndDates(OrderStatus.UNFILLED.name(), LocalDate.now(), LocalDate.now())
                    .stream()
                    .map(order -> order.getPerson()).distinct().collect(Collectors.toList());
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
    public Order findOrder(Order order) throws OrderNotFoundException {

        Order existing;
        // if order status no sandwich
        if (order.getOrderStatus()==OrderStatus.UNFILLED){
            //System.out.println("findorder unfilled");
          existing = orderRepository.checkIfOrderExists(order.getOrderStatus().name(), order.getDate(),
                  order.getSandwichShop().getSandwichShopID(), order.getPerson().getPersonNr(), order.getSession().getSessionNumber());
        } else if (order.getOrderStatus() == OrderStatus.NOSANDWICH){
            existing = orderRepository.checkIfOrderExists(order.getRemark(), order.getOrderStatus().name(), order.getDate(),
                    order.getSandwichShop().getSandwichShopID(), order.getPerson().getPersonNr(),
                    order.getSession().getSessionNumber());
        } else {
            // if everything filled in
            System.out.println("CHECK EVERYTHING FILLED IN!");
            existing =orderRepository.checkIfOrderExists(order.getSandwich().getSandwichID(), order.getBreadType().name(), order.getRemark(),
                    order.getOrderStatus().name(), order.getAmount(), order.getPrice(), order.getDate(), order.getSandwichShop().getSandwichShopID(),
                    order.getPerson().getPersonNr(), order.getSession().getSessionNumber());
        }

        if (existing == null) throw new OrderNotFoundException("This order does not exist");
        //System.out.println(existing.getOrderNum());
        return existing;
    }
}
