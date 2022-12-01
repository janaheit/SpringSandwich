package be.abis.sandwichordersystem.repository;

import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Integer> {
    /* for now we can use the standard save methods?
    public boolean addOrder(Order order);
    public boolean deleteOrder(Order order) throws OrderNotFoundException;
     */
    Order findOrderById(int id);
    @Query(value = "select * from orders", nativeQuery = true)
    public List<Order> getOrders();

    @Query(value = "select * from orders where odate = :date", nativeQuery = true)
    public List<Order> findOrdersByDate(@Param("date") LocalDate date);

    @Query(value = "select * from orders where o_sid = :sId", nativeQuery = true)
    public List<Order> findOrdersBySession(@Param("sId") int sessionId);

    @Query(value = "select * from orders where odate >= :startDate and odate <= :endDate", nativeQuery = true)
    public List<Order> findOrdersByDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select * from orders where odate >= :startDate and odate <= :endDate and ostatus = :status", nativeQuery = true)
    public List<Order> findOrdersByStatusAndDates(@Param("status") String status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select * from orders where ostatus = :status and o_sid = :sid", nativeQuery = true)
    List<Order> findOrdersByStatusAndSession(@Param("status") String status, @Param("sid") int sessionId);

    @Query(value = "select * from orders where odate >= :sdate and odate <= :edate and o_pid = :pid", nativeQuery = true)
    public List<Order> findOrdersByPersonAndDates(@Param("pid") int personId,@Param("sdate") LocalDate startDate,@Param("edate") LocalDate endDate);

    @Query(value = "select * from orders where o_sandid = :sandwichId and obread = :breadtype and oremark = :remark and ostatus = :status and oamount = :amount and oprice = :price and odate = :date and o_shop = :shopid and o_pid = :personid and o_sid = :sessionid", nativeQuery = true)
    Order checkIfOrderExists(@Param("sandwichId") int sandwichId, @Param("breadtype") String breadtype, @Param("remark") String remark, @Param("status") String orderStatus, @Param("amount") int amount, @Param("price") double price, @Param("date") LocalDate date, @Param("shopid") int shopId, @Param("personid") int personId, @Param("sessionid") int sessionId);

    @Query(value = "select * from orders where ostatus!=:status", nativeQuery = true)
    List<Order> findAllUnhandledOrders(@Param("status") String status);

    @Query(value = "select * from orders where ostatus=:status", nativeQuery = true)
    List<Order> findAllUnfilledOrders(@Param("status") String status);
}
