package com.jiangqi.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangqi.booking.utils.MD5Utils;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_back;        //返回按钮
    private RelativeLayout rl_title_bar;//标题布局
    private Button btn_register;    //注册按钮
    private EditText et_user_name, et_pwd, et_pwd_again;//用户名、密码、再次输入的密码的控件
    private String username, pwd, pwd_again;//用户名、密码、再次输入的密码的控件的获取值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        //从main_title_bar.xml页面布局中获取对应的UI控件
        //抽取成员变量ctrl+alt+F
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back = findViewById(R.id.tv_back);
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);

        //从activity_register.xml页面布局中获取对应的UI控件
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override//关闭页面的点击事件
            public void onClick(View view) {//设置按钮可以关闭当前页面
                RegisterActivity.this.finish();
            }
        });
        //注册按钮点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后获取输入在响应控件中的字符串
                getEditstring();
                //判断字符串是否为空
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwd_again)) {
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwd.equals(pwd_again)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isExistUserName(username)) {
                    Toast.makeText(RegisterActivity.this, "此用户已经存在", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //把用户名和密码保存到SharedPreferences里面
                    saveRegisterInfo(username, pwd);
                    //注册成功后通过Intent把用户名传递到LoginActivity.java中
                    Intent data = new Intent();
                    data.putExtra("username", username);
                    setResult(RESULT_OK, data);//setResult为OK，关闭当前页面
                    RegisterActivity.this.finish();//在登录的时候，如果用户还没有注册则注册。注册成功后把注册成功后的用户名返回给前一个页面
                }
            }
        });
    }

    private void saveRegisterInfo(String username, String pwd) {
        String md5Pwd = MD5Utils.MD5(pwd);//把密码用MD5加密
        //loginInfo是sp的文件名
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);//通过getSharedPreferences传入loginInfo注册登录相关的信息
        SharedPreferences.Editor editor = sp.edit();//通过sp.edit()获取到sp的编辑器对象
        //username作为key，密码作为value
        editor.putString(username, md5Pwd);
        editor.commit();//提交修改
    }

    /**
     * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有用户名
     */
    private boolean isExistUserName(String username) {
        boolean has_userName = false;//表示是否有用户名
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPwd = sp.getString(username, ""); //通过sp.getString传值用户名获取到密码
        if (!TextUtils.isEmpty(spPwd)) { //判断这个密码是否为空
            has_userName = true;//该用户是否保存了这一个密码
        }
        return has_userName;
    }

    /**
     * 获取控件中的字符串
     */
    private void getEditstring() {
        username = et_user_name.getText().toString().trim();
        pwd = et_pwd.getText().toString();
        pwd_again = et_pwd_again.getText().toString().trim();
    }
}

