package com.example.peng.graduationproject.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by peng on 2016/4/23.
 */
public class OrderManager {

    public static final int TYPE_ORDERS = 1;

    public static final int TYPE_HISTORYDEMANDS = 2;

    public static final int TYPE_HISTORYORDERS = 3;

    public static ArrayList<Order> historyDemands = new ArrayList<>();

    public static ArrayList<Order> orders = new ArrayList<>();

    public static ArrayList<Order> historyOrders = new ArrayList<>();

    public static Order getOrderById(int state, int listType, int orderId){

        Order result = null;

        if (listType == TYPE_ORDERS){
            for (int i = 0; i<orders.size(); i++){
                if ((orders.get(i).getState() == 0) && (orders.get(i).getOrderId() == orderId)){

                    try{
                        result = (Order)orders.get(i).clone();

                    }catch(CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                    return result;
                }
            }
        }else if (listType == TYPE_HISTORYDEMANDS){
            for (int i = 0; i<historyDemands.size(); i++){
                if (historyDemands.get(i).getOrderId() == orderId){

                    try{
                        result = (Order)historyDemands.get(i).clone();

                    }catch(CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                    return result;
                }
            }
        }

        return null;
    }

}
