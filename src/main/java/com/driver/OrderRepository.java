package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String, Order> Ordermap;
    HashMap<String, DeliveryPartner> Partnermap;
    HashMap<String, List<String>> OrderPartnermap;
    OrderRepository(){
        Ordermap = new HashMap<String, Order>();
        Partnermap = new HashMap<String, DeliveryPartner>();
        OrderPartnermap = new HashMap<String, List<String>>();
    }

    public void addOrder(Order order){
        Ordermap.put(order.getId(), order);
    }

    public void addPartner(String partner){
        Partnermap.put(partner,new DeliveryPartner(partner));
    }

    public void AssignOrderToPartner(String OrderId, String partnerId){
        if(Ordermap.containsKey(OrderId) && Partnermap.containsKey(partnerId)){
            Ordermap.put(OrderId, Ordermap.get(OrderId));
            Partnermap.put(partnerId, Partnermap.get(partnerId));
            List<String> count = new ArrayList<>();
            if(OrderPartnermap.containsKey(partnerId)){
                count = OrderPartnermap.get(partnerId);
            }
            count.add(OrderId);
            OrderPartnermap.put(partnerId, count);
            DeliveryPartner p = Partnermap.get(partnerId);
            p.setNumberOfOrders(count.size());
            Partnermap.put(partnerId, p);
        }
    }

    public Order  getOrder(String OrderId){
        return Ordermap.get(OrderId);
    }

    public DeliveryPartner getPartner(String partnerId){
        return Partnermap.get(partnerId);
    }

    public int getNoOfOrdersToPartner(String partnerId){
        if(OrderPartnermap.containsKey(partnerId))
            return OrderPartnermap.get(partnerId).size();
        return 0;
    }

    public List<String> getOrdersOfPartner(String partnerId){
        if(OrderPartnermap.containsKey(partnerId))
            return OrderPartnermap.get(partnerId);
        return new ArrayList<>();
    }

    public List<String> getAllOrders(){
        return new ArrayList<>(Ordermap.keySet());
    }

    public int OrdersNotAssigned(){
        HashSet<String> set = new HashSet<>();
        for(Map.Entry<String, List<String>> s:OrderPartnermap.entrySet()){
            set.addAll(s.getValue());
        }
        return Ordermap.size()-set.size();
    }

    public int OrdersLeftByPartner(String Time, String partnerId){
        String[] HourMin = Time.split(":");
        int HH = Integer.parseInt(HourMin[0]);
        int MM = Integer.parseInt(HourMin[1]);
        int t = HH*60 + MM;
        int count = 0;
        for(String s : OrderPartnermap.get(partnerId)){
            if(Ordermap.get(s).getDeliveryTime() > t)
                count++;
        }
        return count;
    }

    public String lastDeliverOfPartner(String partner_Id){
        int max = 0;
        if(OrderPartnermap.containsKey(partner_Id)) {
            for (String s : OrderPartnermap.get(partner_Id)) {
                max = Math.max(max, Ordermap.get(s).getDeliveryTime());
            }
        }
        return Order.getDeliveryTimeAsString(max);
    }

    public void deletePartnerAndHisOrders(String partnerId){
        List<String> orders = new ArrayList<>();
        if(OrderPartnermap.containsKey(partnerId)){
            Partnermap.remove(partnerId);
            orders = OrderPartnermap.get(partnerId);
            for(String order : orders){
                Partnermap.remove(order);
            }
        }
        OrderPartnermap.remove(partnerId);
    }

    public void deleteOrderAndUnassigned(String OrderId){
        if(Ordermap.containsKey(OrderId)){
            for(Map.Entry<String, List<String>> s : OrderPartnermap.entrySet()){
                String partnerId = s.getKey();
                List<String> curr = s.getValue();
                for(String Id:s.getValue()){
                    if(Id.equals(OrderId)){
                        curr.remove(OrderId);
                    }
                    Partnermap.get(partnerId).setNumberOfOrders(curr.size());
                    Ordermap.remove(OrderId);
                    OrderPartnermap.put(partnerId, curr);
                }
            }
        }
    }
}
