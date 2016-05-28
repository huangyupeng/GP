package com.example.peng.graduationproject.ui.personal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.peng.graduationproject.R;
import com.example.peng.graduationproject.common.BaseFragment;
import com.example.peng.graduationproject.common.Constants;
import com.example.peng.graduationproject.common.DownloadTask;
import com.example.peng.graduationproject.model.User;
import com.example.peng.graduationproject.model.UserInfoManager;
import com.example.peng.graduationproject.ui.dialog.CommonDialog;
import com.example.peng.graduationproject.ui.global.loginActivity;
import com.example.peng.graduationproject.ui.view.RoundImageView;

import java.io.File;

/**
 * Created by peng on 2016/4/3.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener{

    private static final int EVENT_GET_VERSION = 18;
    private static final int EVENT_SHOW_CONFIRM_DIALOG = 19;
    private static final int EVENT_DOWNLOAD = 20;
    private static final int EVENT_SHOW_OK_DIALOG = 21;

    private ProgressDialog mProgressDialog;

    private String downloadUrl;
    private String versionName;
    private String versionCode;

    private DownloadTask downloadTask;

    private SwipeRefreshLayout swipe_layout;

    private RoundImageView face;
    private TextView name;
    private LinearLayout personal_info;

    private RelativeLayout history_order;
    private RelativeLayout advice;
    private RelativeLayout update;
    private RelativeLayout about;
    private Button signout;

    private User currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_personal, container, false);



        initView(view);
        setDefaultValues();
        bindEvents();
        return view;
    }

    @Override
    protected void initView(View view){

        face =(RoundImageView)view.findViewById(R.id.face);
        name = (TextView)view.findViewById(R.id.name);
        personal_info = (LinearLayout)view.findViewById(R.id.personal_info);
        history_order = (RelativeLayout)view.findViewById(R.id.history_order);
        advice = (RelativeLayout)view.findViewById(R.id.advice);
        update = (RelativeLayout)view.findViewById(R.id.update);
        about = (RelativeLayout)view.findViewById(R.id.about);
        signout = (Button)view.findViewById(R.id.signout);
        swipe_layout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);







    }

    @Override
    protected void setDefaultValues() {
        currentUser = UserInfoManager.getCurrentUser();


        if (currentUser == null){
            showToast("连接失败，请尝试重新登录");
        }else{
            name.setText(currentUser.getName());

            try {
                Bitmap bitmap = null;
                File file = new File(Constants.FILE_DIRECTORY + "/"
                        + currentUser.getMobile() + "/face.jpg");

                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(Constants.FILE_DIRECTORY + "/"
                            + currentUser.getMobile() + "/face.jpg");
                    face.setImageBitmap(bitmap);
                } else
                    face.setImageResource(R.mipmap.defaultface);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }





        //TODO update

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Downloading");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        downloadTask = new DownloadTask(getActivity(), mProgressDialog);

    }

    @Override
    protected void bindEvents() {

        history_order.setOnClickListener(this);
        advice.setOnClickListener(this);
        update.setOnClickListener(this);
        about.setOnClickListener(this);
        signout.setOnClickListener(this);
        personal_info.setOnClickListener(this);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });







        //TODO update


        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){

            case R.id.personal_info:
                intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.history_order:
                intent = new Intent(getActivity(), HistoryOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.advice:
                intent = new Intent(getActivity(), AdviceActivity.class);
                startActivity(intent);
                break;
            case R.id.update://TODO update
                getProcHandler().sendEmptyMessage(EVENT_GET_VERSION);
                break;
            case R.id.about:

                break;
            case R.id.signout:
                intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }


    //TODO below is update




    @Override
    protected boolean uiHandlerCallback(Message msg) {

        switch (msg.what){
            case EVENT_SHOW_CONFIRM_DIALOG:
                final CommonDialog confirmDialog = new CommonDialog(getActivity());
                confirmDialog.setDialogTitle("Tips");
                confirmDialog.setTxt_tips("当前最新版本为" + versionName + ",是否下载并更新");
                confirmDialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.setOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getProcHandler().sendEmptyMessage(EVENT_DOWNLOAD);
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.show();
                break;
            case EVENT_SHOW_OK_DIALOG:
                final CommonDialog okDialog = new CommonDialog(getActivity());
                okDialog.setDialogTitle("Tips");
                okDialog.setTxt_tips("当前版本已是最新版本");
                okDialog.setOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        okDialog.dismiss();
                    }
                });
                okDialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        okDialog.dismiss();
                    }
                });

        }


        return super.uiHandlerCallback(msg);
    }

    @Override
    protected boolean procHandlerCallBack(Message msg) {

        switch (msg.what){
            case EVENT_GET_VERSION:
                //TODO interface get version

                break;
            case EVENT_DOWNLOAD:
                downloadTask.execute(downloadUrl);
                break;

        }
        return super.procHandlerCallBack(msg);
    }


}
