package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order orderId){
        orderRepository.addOrder(orderId);
    }

    public void addPartner(String partnerId){
        orderRepository.addPartner(partnerId);
    }

    public void addOrderToPartner(String orderId, String partnerId){
        orderRepository.AssignOrderToPartner(orderId, partnerId);
    }

    public Order getOrder(String orderId){
        return orderRepository.getOrder(orderId);
    }

    public DeliveryPartner getPartner(String partnerId){
        return orderRepository.getPartner(partnerId);
    }

    public int getNoOfOrdersToPartner(String partnerId){
        return orderRepository.getNoOfOrdersToPartner(partnerId);
    }

    public List<String> getOrdersOfPartner(String partnerId){
        return orderRepository.getOrdersOfPartner(partnerId);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }

    public int OrdersNotAssigned(){
        return orderRepository.OrdersNotAssigned();
    }

    public int OrdersLeftByPartner(String Time, String partnerId){
        return orderRepository.OrdersLeftByPartner(Time, partnerId);
    }

    public String lastDeliverOfPartner(String partnerId){
        return orderRepository.lastDeliverOfPartner(partnerId);
    }

    public void deletedPartnerAndHisOrders(String partnerId){
        orderRepository.deletePartnerAndHisOrders(partnerId);
    }

    public void deleteOrderAndUnassigned(String orderId){
        orderRepository.deleteOrderAndUnassigned(orderId);
    }
}
