package be.abis.sandwichordersystem.service;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.exception.IngredientNotAvailableException;
import be.abis.sandwichordersystem.exception.OptionNotAvailableRuntimeException;
import be.abis.sandwichordersystem.exception.OrderNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
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
import java.util.Comparator;
import java.util.List;
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
    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    @Override
    public void deleteOrder(Order order) throws OrderNotFoundException {
        orderRepository.deleteOrder(order);
    }

    @Override
    public void createOrdersForEveryoneToday() throws SandwichShopNotFoundException {
        if (this.dayOrder == null || this.dayOrder.getCurrentSandwichShop() == null) {
            throw new SandwichShopNotFoundException("No sandwichshop selected for this day");
        } else {
            List<Person> peopleOfToday = sessionService.findAllPersonsFollowingSessionToday();
            for (Person p : peopleOfToday) {
                this.createOrder(p);
            }
        }
    }

    @Override
    public void createOrder(Person person) {
        Order thisOrder = new Order(person, this.dayOrder);
        addOrder(thisOrder);
    }

    @Override
    public void handleOrder(Order order, Boolean noSandwich) {
        order.setOrderStatus(OrderStatus.NOSANDWICH);
    }

    @Override
    public void handleOrder(Order order, Boolean noSandwich, String remark) {
        order.setOrderStatus(OrderStatus.NOSANDWICH);
        order.setRemark(remark);
    }

    @Override
    public void handleOrder(Order order, Sandwich sandwich, BreadType breadType, List<Options> options, String remark) throws IngredientNotAvailableException {
        SandwichShop mySandwichShop = order.getDayOrder().getCurrentSandwichShop();
        if(!sandwichShopService.checkSandwich(sandwich, mySandwichShop)) {
            throw new IngredientNotAvailableException("Sandwich" + sandwich.getName() + " not available at " + mySandwichShop.getName());
        }
        order.setSandwich(sandwich);
        if(!sandwichShopService.checkBreadType(breadType, mySandwichShop)) {
            throw new IngredientNotAvailableException("Breadtype " + breadType.getBreadType() + " not available at " + mySandwichShop.getName());
        }
        order.setBreadType(breadType);
        order.setOptions(sandwichShopService.checkOptions(options, mySandwichShop));
        if(order.getOptions().size() != options.size()) {
            throw new OptionNotAvailableRuntimeException("Not all options were available!");
        }
        order.setRemark(remark);
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
        return orderRepository.getAllOrders().stream().filter(order -> order.getOrderStatus() != OrderStatus.HANDELED).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllUnfilledOrders() {
        return orderRepository.getAllOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.UNFILLED).collect(Collectors.toList());
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

    // Simple getters and setters
    public DayOrder getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(DayOrder dayOrder) {
        this.dayOrder = dayOrder;
    }

    // Other helper methods
    public String todaysOrdersForShop() {
        StringBuilder output = new StringBuilder();
        List<Order> todaysOrders = this.findOrdersByDate(LocalDate.now()).stream().filter(o -> o.getOrderStatus() == OrderStatus.ORDERED).sorted(Comparator.comparing(o -> o.getPerson().getFirstName())).sorted(Comparator.comparing(o -> o.getSession().getSessionNumber())).collect(Collectors.toList());

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
            output.append(String.format("%1$-10s%2$-40s%3$-8s%4$-15s\n",order.getPerson().getFirstName(), Integer.toString(order.getAmount()) + " " + order.getSandwich().getName(), order.getBreadType().getBreadType().toUpperCase(), options));
            if (order.getRemark().length() > 0) {
                output.append(String.format("%2$-10s%1$-7s\n", order.getRemark().toUpperCase(), ""));
            }
        }

        return output.toString();

    }

}