package com.jiangqi.booking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangqi.booking.utils.AnalysisUtils;
import com.jiangqi.booking.utils.MD5Utils;


public class ModifyPwdActivity extends Activity {

    private TextView tv_main_title,tv_back;
    private EditText et_original_psw,et_new_psw,et_new_psw_again;
    private Button btn_save;
    private String originalPsw,newPsw,newPswAgain,userName;
    private RelativeLayout rl_title_bar;//标题布局


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        init();
        userName= AnalysisUtils.readLoginUserName(this);
    }

    //获取界面控件并处理相关控件的点击事件
    private void init() {
        // TODO Auto-generated method stub
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        tv_main_title.setBackgroundColor(Color.parseColor("#78A4FA"));
        tv_back=findViewById(R.id.tv_back);
        et_original_psw= findViewById(R.id.et_originnal_pwd);
        et_new_psw= findViewById(R.id.et_new_pwd);
        et_new_psw_again= findViewById(R.id.et_new_pwd_again);
        btn_save= findViewById(R.id.btn_save);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyPwdActivity.this.finish();
            }
        });
        //保存按钮的点击事件
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(originalPsw)) {
                    Toast.makeText(ModifyPwdActivity.this, "请输入原始密码", Toast.LENGTH_SHORT).show();
                    return;
                    //当点击“保存”按钮时需要验证保存密码是否正确
                } else if (!MD5Utils.MD5(originalPsw).equals(readPsw())) {
                    Toast.makeText(ModifyPwdActivity.this, "输入的密码与原始密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if(MD5Utils.MD5(newPsw).equals(readPsw())){
                    Toast.makeText(ModifyPwdActivity.this, "输入的新密码与原始密码不能一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPsw)) {
                    Toast.makeText(ModifyPwdActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPswAgain)) {
                    Toast.makeText(ModifyPwdActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                    //验证新输入的密码是否相同
                } else if (!newPsw.equals(newPswAgain)) {
                    Toast.makeText(ModifyPwdActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(ModifyPwdActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
                    //修改登录成功时保存在SharedPreferences中的密码
                    modifyPsw(newPsw);
                    Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AboutActivity.instance.finish();
                    ModifyPwdActivity.this.finish();
                }
            }
        });
    }

    //获取控件上的字符串
    private void getEditString() {
        // TODO Auto-generated method stub
        originalPsw=et_original_psw.getText().toString().trim();
        newPsw=et_new_psw.getText().toString().trim();
        newPswAgain=et_new_psw_again.getText().toString().trim();
    }

    //将SharedPreferences中的密码修改为密码
    private void modifyPsw(String newPsw) {
        // TODO Auto-generated method stub
        String md5Psw= MD5Utils.MD5(newPsw);//把密码用MD5加密
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putString(userName, md5Psw);//保存新密码
        editor.commit();//提交修改
    }

    //从SharedPreferences中读取原始密码
    private Object readPsw() {
        // TODO Auto-generated method stub
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw=sp.getString(userName, "");
        return spPsw;
    }
}
