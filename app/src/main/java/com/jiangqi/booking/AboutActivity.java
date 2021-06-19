package com.jiangqi.booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangqi.booking.utils.AnalysisUtils;


public class AboutActivity extends Activity {
    private TextView et_user_name;//标题
    private ImageView tv_back;
    private RelativeLayout rl_modify_psw,rl_exit_login;
    public static  AboutActivity instance=null;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Context context=getApplicationContext();
        username= AnalysisUtils.readLoginUserName(context);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        instance = this;
        init();
    }

    //    获取界面控件
    private void init() {
        // TODO Auto-generated method stub
        et_user_name = findViewById(R.id.et_user_name);
        et_user_name.setText(username);
        tv_back= findViewById(R.id.tv_back);
        rl_modify_psw=findViewById(R.id.rl_modify_pwd);
        rl_exit_login=findViewById(R.id.rl_exit_login);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });

        //修改密码的点击事件
        rl_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改密码的界面
                Intent intent=new Intent(AboutActivity.this, ModifyPwdActivity.class);
                startActivity(intent);

            }
        });

        //退出登录的点击事件
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this,"退出登录成功",Toast.LENGTH_SHORT).show();
                clearLoginStatus();//清除登录状态和登录时的用户名
                //退出登录成功后把退出成功状态传递到MainActivity中
                Intent data = new Intent();
                data.putExtra("isLogin",false);
                setResult(RESULT_OK,data);
                AboutActivity.this.finish();
                Intent intent=new Intent(AboutActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }

    //	清除SharePrederences中的登录状态和登录时的登录名
    private void clearLoginStatus() {
        // TODO Auto-generated method stub
        SharedPreferences sp = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();  //获取编辑器
        editor.putBoolean("isLogin",false);        //清除登录状态
        editor.putString("loginUserName","");    //清除用户名
        editor.commit();
    }
}
