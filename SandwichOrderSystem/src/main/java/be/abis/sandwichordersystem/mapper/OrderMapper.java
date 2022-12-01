package be.abis.sandwichordersystem.mapper;

import be.abis.sandwichordersystem.dto.OrderDTO;
import be.abis.sandwichordersystem.enums.OrderStatus;
import be.abis.sandwichordersystem.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderDTO toDTO(Order o){

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderID(o.getOrderNum());
        orderDTO.setOrderStatus(o.getOrderStatus());
        orderDTO.setPersonID(o.getPerson().getPersonNr());
        orderDTO.setPersonName(o.getPerson().getFirstName() + " " + o.getPerson().getLastName());
        orderDTO.setSandwichShopID(o.getSandwichShop().getSandwichShopID());
        orderDTO.setSandwichShopName(o.getSandwichShop().getName());
        orderDTO.setSessionID(o.getSession().getSessionNumber());
        orderDTO.setSessionName(o.getSession().getCourse().name());
        orderDTO.setDate(o.getDate());

        if (o.getOrderStatus() != OrderStatus.UNFILLED) {
            orderDTO.setSandwichID(o.getSandwich().getSandwichID());
            orderDTO.setSandwichName(o.getSandwich().getName());
            orderDTO.setBreadType(o.getBreadType());
            orderDTO.setOptions(o.getOptions());
            orderDTO.setRemark(o.getRemark());
        }

        return orderDTO;
    }
}
