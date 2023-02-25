package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String, Order> Ordermap;
    HashMap<String, DeliveryPartner> Partnermap;
    HashMap<String, List<String>> OrderPartnermap;
    HashMap<String, String> assigned;
    OrderRepository(){
        Ordermap = new HashMap<String, Order>();
        Partnermap = new HashMap<String, DeliveryPartner>();
        OrderPartnermap = new HashMap<String, List<String>>();
        assigned = new HashMap<String, String>();
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
        assigned.put(OrderId, partnerId);
    }

    public Order getOrder(String OrderId){
        if(Ordermap.containsKey(OrderId))
            return Ordermap.get(OrderId);
        return null;
    }

    public DeliveryPartner getPartner(String partnerId){
        if(Partnermap.containsKey(partnerId))
            return Partnermap.get(partnerId);
        return null;
    }

    public int getNoOfOrdersToPartner(String partnerId){
        int orders = OrderPartnermap.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersOfPartner(String partnerId){
        List<String> orders = OrderPartnermap.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders(){
        return new ArrayList<>(Ordermap.keySet());
    }

    public int OrdersNotAssigned(){
        int count = Ordermap.size()-assigned.size();
        return count;
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
        String time = "";
        if(OrderPartnermap.containsKey(partner_Id)) {
            for (String s : OrderPartnermap.get(partner_Id)) {
                max = Math.max(max, Ordermap.get(s).getDeliveryTime());
            }
        }
        int hour = max / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }

        int min = max % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;
    }

    public void deletePartnerAndHisOrders(String partnerId){
//        List<String> orders = new ArrayList<>();
//        if(OrderPartnermap.containsKey(partnerId)){
//            Partnermap.remove(partnerId);
//            orders = OrderPartnermap.get(partnerId);
//            for(String order : orders){
//                Partnermap.remove(order);
//            }
//        }
//        OrderPartnermap.remove(partnerId);
        OrderPartnermap.remove(partnerId);

        List<String> list = OrderPartnermap.getOrDefault(partnerId, new ArrayList<>());
        for (String s : list) {
            assigned.remove(s);
        }
//        tIterator<String> itr = list.listIterator();
//        while (itr.hasNext()) {
//            String s = itr.next();
//            assigned.remove(s);
//        }
        OrderPartnermap.remove(partnerId);
    }

    public void deleteOrderAndUnassigned(String OrderId){
        Ordermap.remove(OrderId);
        String partnerId = assigned.get(OrderId);
        assigned.remove(OrderId);
        List<String> list = OrderPartnermap.get(partnerId);
        for(String s : list){
            if(s.equals(OrderId))
                list.remove(OrderId);
        }
        OrderPartnermap.put(partnerId, list);
    }
}
