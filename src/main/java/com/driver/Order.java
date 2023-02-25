package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        this.id =id;
        String[] HourMin = deliveryTime.split(":");
        int HH = Integer.parseInt(HourMin[0]);
        int MM = Integer.parseInt(HourMin[1]);
        this.deliveryTime = HH*60 + MM;
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public static String getDeliveryTimeAsString(int deliveryTime){
        int hours = deliveryTime/60;
        int min = deliveryTime%60;

        String hrStr="";
        String minStr="";
        if(hours<10){
            hrStr="0"+hours;
        }
        else{
            hrStr=""+hours;
        }

        if(min<10){
            minStr="0"+min;
        }
        else{
            minStr=""+min;
        }

        return hrStr+":"+minStr;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
