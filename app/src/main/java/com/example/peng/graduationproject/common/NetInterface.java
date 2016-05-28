package com.example.peng.graduationproject.common;

/**
 * Created by peng on 2016/5/27.
 */
public class NetInterface {

    private final static String baseIP = "http://192.168.199.193:8080";

    public final static String LOGIN = baseIP + "/user/login";   //testing

    public final static String GETPASSWORD = baseIP + "/user/forgetPassword";

    public final static String REGISTER = baseIP + "/user/signup";

    public final static String GET_ORDERS = baseIP + "/demandAndOrder/getDemandAndOrder";

    public final static String GET_HISTORYORDERS = baseIP + "/demandAndOrder/getDemandAndOrderIsOK";

    public final static String ADD_DEMAND = baseIP + "/demand/addDemand";
}
