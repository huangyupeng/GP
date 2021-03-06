package com.example.peng.graduationproject.ui.global;

/**
 * Created by cuinaitian on 2016/3/30 0030.
 */

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.example.peng.graduationproject.R;
import com.example.peng.graduationproject.common.BaseActivity;
import com.example.peng.graduationproject.common.NetInterface;
import com.example.peng.graduationproject.common.NetManager;
import com.example.peng.graduationproject.model.User;
import com.example.peng.graduationproject.model.UserInfoManager;


public class loginActivity extends BaseActivity implements OnClickListener{

    private static final int EVENT_LOGIN = 1;

    private SharedPreferences mPref;

    private Button login;
    private TextView get_password;
    private TextView register;

    private EditText log_name;
    private EditText log_password;

    private String mobile;
    private String password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_login);
        setNoTitle(true);

        initView();
        setDefaultValues();
        bindEvents();


    }

    @Override
    protected void initView(){
        login=(Button)findViewById(R.id.log_login);
        register=(TextView)findViewById(R.id.log_register);
        get_password=(TextView)findViewById(R.id.get_password);
        log_name=(EditText)findViewById(R.id.log_name);
        log_password=(EditText)findViewById(R.id.log_password);
    }

    @Override
    protected void setDefaultValues(){

        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        log_name.setText(mPref.getString("userid", ""));
        log_password.setText(mPref.getString("password", ""));

    }

    @Override
    protected void bindEvents(){

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        get_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.log_login:
                mobile = log_name.getText().toString();
                password = log_password.getText().toString();
                if(mobile.equals(""))
                {
                    showToast("手机号码不能为空");
                }
                else if(password.equals(""))
                {
                    showToast("密码不能为空");
                }
                else if(!NetManager.isConnect(this))
                {
                    showToast("网络未连接");
                }
                else
                {
                    getProcHandler().sendEmptyMessage(EVENT_LOGIN);

                }
                break;
            case R.id.log_register:
                Intent m = new Intent(loginActivity.this,registerActivity.class);
                startActivity(m);
                break;
            case R.id.get_password:
                Intent i=new Intent(loginActivity.this,getPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected boolean procHandlerCallBack(Message msg) {

        switch (msg.what){
            case EVENT_LOGIN:
                try {
                    JSONObject params = new JSONObject();
                    params.put("mobile",mobile);
                    params.put("loginPassword",password);
                    JSONObject res = NetManager.doPostJson(NetInterface.LOGIN, params);

                    if (res != null){

                        if (res.getInt("code") != 0){
                            showToastOnUiThread("登录失败，错误码:" + res.getInt("code"));
                        }else{
                            showToastOnUiThread("登录成功");
                            NetManager.token = ((JSONObject)res.get("data")).getString("token");

                            User user = new User();

                            user.setMobile(mobile);
                            user.setPassword(password);

                            UserInfoManager.setCurrentUser(user);

                            Intent intent= new Intent(loginActivity.this, HomeActivity.class);
                            startActivity(intent);

                            SharedPreferences.Editor editor = mPref.edit();
                            editor.putString("userid",mobile);
                            editor.putString("password",password);
                            editor.commit();
                            finish();
                        }

                    }else{
                        Log.e("myerror","httpres = null");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
                break;

        }

        return super.procHandlerCallBack(msg);
    }
}