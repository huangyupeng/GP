package com.example.peng.graduationproject.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.peng.graduationproject.R;
import com.example.peng.graduationproject.common.BaseFragment;
import com.example.peng.graduationproject.common.Constants;
import com.example.peng.graduationproject.common.NetInterface;
import com.example.peng.graduationproject.common.NetManager;
import com.example.peng.graduationproject.model.Order;
import com.example.peng.graduationproject.model.OrderManager;
import com.example.peng.graduationproject.ui.adapter.OrderListAdapter;
import com.example.peng.graduationproject.ui.global.HomeActivity;
import com.example.peng.graduationproject.ui.personal.HistoryOrderActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by peng on 2016/4/2.
 */
public class OrderFragment extends BaseFragment{

    private final int EVENT_REFRESH = 11;

    private final int EVENT_UI_REFRESH = 12;

    private SwipeRefreshLayout swipe_ly;
    private ListView order_list;

    private ImageView btn_add;

    private OrderListAdapter adapter;

    //private ArrayList<Order> datalist;

    private int orderType;  //HomeActivity = 1 ; HistoryActivity = 2

    private static boolean firstRefresh = false;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        initView(view);
        setDefaultValues();
        bindEvents();



        return view;
    }

    @Override
    protected void initView(View view){

        swipe_ly = (SwipeRefreshLayout)view.findViewById(R.id.swipe_ly);
        order_list = (ListView)view.findViewById(R.id.order_list);




    }

    @Override
    protected void setDefaultValues() {




        if (getActivity() instanceof HomeActivity){
            orderType = 1;
            btn_add = ((HomeActivity)getActivity()).getBtn_add();
            adapter = new OrderListAdapter(getActivity(), OrderManager.orders);
        }else if (getActivity() instanceof HistoryOrderActivity){
            orderType = 2;
            adapter = new OrderListAdapter(getActivity(), OrderManager.historyOrders);

        }
        order_list.setAdapter(adapter);

        if (orderType == 2 && OrderManager.historyOrders.size() == 0){
            getProcHandler().sendEmptyMessage(EVENT_REFRESH);
        }
        if (orderType == 1 && !firstRefresh){
            getProcHandler().sendEmptyMessage(EVENT_REFRESH);
            firstRefresh = true;
        }

    }

    @Override
    protected void bindEvents() {

        if (orderType == 1) {
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderAddActivity.class);
                    startActivity(intent);
                }
            });
        }

        swipe_ly.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProcHandler().sendEmptyMessage(EVENT_REFRESH);

            }
        });

        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (orderType == 1 && OrderManager.orders.get(position).getState() == 0){
                    Intent intent = new Intent(getActivity(), DemandDetailActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("orderType", orderType);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected boolean procHandlerCallBack(Message msg) {

        switch (msg.what){
            case EVENT_REFRESH:

                try{

                    if (orderType == 1) {
                        JSONObject res = NetManager.doGetJson(NetInterface.GET_ORDERS);

                        if (res!=null){

                            if (res.getInt("code") != 0){
                                showToastOnUiThread("获取失败，错误码:" + res.getInt("code"));
                            }else{
                                //showToastOnUiThread("获取成功");

                                OrderManager.orders = new ArrayList<>();
                                JSONArray list = res.getJSONObject("data").getJSONArray("demands");

                                for (int i = 0; i<list.length(); i++){
                                    Order temp = new Order();
                                    temp.setState(0);
                                    temp.setOrderId(list.getJSONObject(i).getInt("id"));
                                    temp.setLocation(list.getJSONObject(i).getString("location"));
                                    temp.setLatitude(list.getJSONObject(i).getDouble("lat"));
                                    temp.setLongitude(list.getJSONObject(i).getDouble("lng"));
                                    temp.setFarm_type(list.getJSONObject(i).getInt("landType"));
                                    temp.setMachine_type(list.getJSONObject(i).getInt("machineType"));
                                    temp.setSquare(list.getJSONObject(i).getDouble("area"));
                                    temp.setPrice(list.getJSONObject(i).getDouble("price"));
                                    temp.setStart_time(list.getJSONObject(i).getString("startTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setEnd_time(list.getJSONObject(i).getString("endTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setCommit_date(list.getJSONObject(i).getString("createTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setStatus(list.getJSONObject(i).getInt("status"));

                                    OrderManager.orders.add(temp);
                                }

                                list = res.getJSONObject("data").getJSONArray("orders");

                                for (int i = 0; i<list.length(); i++){
                                    Order temp = OrderManager.getOrderById(0, OrderManager.TYPE_ORDERS, list.getJSONObject(i).getInt("demandId"));
                                    if (temp == null){
                                        continue;
                                    }
                                    temp.setState(1);
                                    temp.setOrderId(list.getJSONObject(i).getInt("id"));
                                    temp.setMachineOwnerId(list.getJSONObject(i).getInt("ownerId"));
                                    temp.setPrice(list.getJSONObject(i).getDouble("price"));
                                    temp.setCommit_date(list.getJSONObject(i).getString("createTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setStatus(list.getJSONObject(i).getInt("status"));

                                    OrderManager.orders.add(temp);
                                }

                            }

                        }else{
                            Log.e("myerror", "res == null");
                        }


                    }else if (orderType == 2){
                        JSONObject res = NetManager.doGetJson(NetInterface.GET_HISTORYORDERS);

                        if (res!=null){

                            if (res.getInt("code") != 0){
                                showToastOnUiThread("获取失败，错误码:" + res.getInt("code"));
                            }else{
                                //showToastOnUiThread("获取成功");

                                OrderManager.historyDemands = new ArrayList<>();
                                JSONArray list = res.getJSONObject("data").getJSONArray("demands");

                                for (int i = 0; i<list.length(); i++){
                                    Order temp = new Order();
                                    temp.setState(0);
                                    temp.setOrderId(list.getJSONObject(i).getInt("id"));
                                    temp.setLocation(list.getJSONObject(i).getString("location"));
                                    temp.setLatitude(list.getJSONObject(i).getDouble("lat"));
                                    temp.setLongitude(list.getJSONObject(i).getDouble("lng"));
                                    temp.setFarm_type(list.getJSONObject(i).getInt("landType"));
                                    temp.setMachine_type(list.getJSONObject(i).getInt("machineType"));
                                    temp.setSquare(list.getJSONObject(i).getDouble("area"));
                                    temp.setPrice(list.getJSONObject(i).getDouble("price"));
                                    temp.setStart_time(list.getJSONObject(i).getString("startTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setEnd_time(list.getJSONObject(i).getString("endTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setCommit_date(list.getJSONObject(i).getString("createTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setStatus(list.getJSONObject(i).getInt("status"));

                                    OrderManager.historyDemands.add(temp);
                                }

                                OrderManager.historyOrders = new ArrayList<>();
                                list = res.getJSONObject("data").getJSONArray("orders");

                                for (int i = 0; i<list.length(); i++){
                                    Order temp = OrderManager.getOrderById(0, OrderManager.TYPE_HISTORYDEMANDS, list.getJSONObject(i).getInt("demandId"));
                                    if (temp == null){
                                        continue;
                                    }
                                    temp.setState(1);
                                    temp.setOrderId(list.getJSONObject(i).getInt("id"));
                                    temp.setMachineOwnerId(list.getJSONObject(i).getInt("ownerId"));
                                    temp.setPrice(list.getJSONObject(i).getDouble("price"));
                                    temp.setCommit_date(list.getJSONObject(i).getString("createTime").substring(0, Constants.DATE_LENGTH));
                                    temp.setStatus(list.getJSONObject(i).getInt("status"));

                                    OrderManager.historyOrders.add(temp);
                                }


                            }


                        }else{
                            Log.e("myerror", "res == null");
                        }
                    }



                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("myerror", "Exception");
                }


                getUiHanler().sendEmptyMessage(EVENT_UI_REFRESH);
                break;
        }

        return super.procHandlerCallBack(msg);
    }

    @Override
    protected boolean uiHandlerCallback(Message msg) {
        switch (msg.what){
            case EVENT_UI_REFRESH:
                swipe_ly.setRefreshing(false);
                adapter.notifyDataSetChanged();
                break;
        }

        return super.uiHandlerCallback(msg);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
