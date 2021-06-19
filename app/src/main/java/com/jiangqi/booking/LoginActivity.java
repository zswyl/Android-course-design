package com.jiangqi.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangqi.booking.utils.MD5Utils;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private TextView tv_register;//立即注册、找回密码的控件
    private Button bt_login; //登录按钮
    private EditText et_user_name, et_pwd;//用户名、密码的控件
    private String username, pwd, spPwd;//用户名、密码的控件的获取值
    private CheckBox cb_remeberme;//记住密码
    private boolean IsChecked = false; // 是否记住密码的状态
    private SharedPreferences sps;
    private String USERNAME = "USER_NAME";
    private String PWD = "PWD";
    private String IS_CHECKED = "IS_CHECKED";
    public static String mUsername;
    private RelativeLayout rl_title_bar;//标题布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //初始化控件
        init();
        //回显数据密码
        initDate();
    }

    private void init() {

        //获取监听的输入框
        tv_register = findViewById(R.id.tv_register);
        bt_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_pwd = findViewById(R.id.et_pwd);
        cb_remeberme = findViewById(R.id.cb_remeberme);

        //顶部赋值title
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
//        tv_main_title.setBackgroundColor(Color.argb(0,0,0,0));//透明
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //顶部隐藏返回按钮
        tv_back = findViewById(R.id.tv_back);
        tv_back.setVisibility(View.GONE);

        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //登录按钮点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                username = et_user_name.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                String md5Pwd = MD5Utils.MD5(pwd);
                spPwd = readPwd(username);
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (md5Pwd.equals(spPwd)) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //把登录状态和登录的用户名保存到SharedPreferences里面
                    saveLoginStatus(true, username);
                    //登录成功后通过Intent把登录成功的状态传递到MainActivity.java中
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    setResult(RESULT_OK, data);//setResult为OK，关闭当前页面
                    LoginActivity.this.finish();//在登录的时候，如果用户还没有注册则注册。注册成功后把注册成功后的用户名返回给前一个页面
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else if ((!TextUtils.isEmpty(spPwd) && !md5Pwd.equals(spPwd))) {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(LoginActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //记住密码方法开始,根据监听的控件的变化进行操作
        //第一步，记住用户名
        cb_remeberme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (IsChecked) {
                    if (sps == null) {
                        sps = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    }
                    //实例化SharedPreferences的编辑者对象
                    SharedPreferences.Editor editor = sps.edit();
                    //存储用户名数据
                    editor.putString(USERNAME, et_user_name.getText().toString());
                    editor.commit();
                }
            }
        });
        //第二步，记住密码
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //文本改变之后记录用户密码
            @Override
            public void afterTextChanged(Editable s) {
                if (IsChecked) {
                    if (sps == null) {
                        sps = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    }
                    //实例化SharedPreferences的编辑者对象
                    SharedPreferences.Editor editor = sps.edit();
                    //存储数据
                    editor.putString(PWD, et_pwd.getText().toString());
                    editor.commit();
                }

            }
        });
        //第三步，获取监听的记住密码按钮
        cb_remeberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsChecked = isChecked;
                if (isChecked) {
                    //实例化SharedPre。。。对象
                    if (sps == null) {
                        sps = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    }
                    //实例化。。。的编辑者对象
                    SharedPreferences.Editor editor = sps.edit();
                    //存储数据
                    editor.putString(USERNAME, et_user_name.getText().toString());
                    editor.putString(PWD, et_pwd.getText().toString());
                    //记录记住密码的状态，下次读取
                    editor.putBoolean(IS_CHECKED, isChecked);
                    Toast.makeText(LoginActivity.this, "已记住密码：" + isChecked, Toast.LENGTH_SHORT).show();
                    //提交
                    editor.commit();
                } else {
                    //取消记住密码
                    SharedPreferences.Editor editor = sps.edit();
                    //存储数据
                    editor.putString(USERNAME, null);
                    editor.putString(PWD, null);
                    //记录记住密码的状态，下次读取
                    editor.putBoolean(IS_CHECKED, isChecked);
                    Toast.makeText(LoginActivity.this, "取消记住密码：" + isChecked, Toast.LENGTH_SHORT).show();
                    //提交
                    editor.commit();
                }
            }
        });
    }


    //从SharedPreferences中根据用户名读取密码
    private String readPwd(String username) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(username, "");
    }

    //保存登录状态和登录用户名到SharedPrefarences中
    private void saveLoginStatus(boolean status, String username) {
        this.mUsername = username;
        //loginInfo表示文件名
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", username);//存入登录时的用户名
        editor.commit();//提交修改
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //从注册界面传递过来的用户名
            String username = data.getStringExtra("username");
            if (!TextUtils.isEmpty(username)) {
                et_user_name.setText(username);
                //设置光标的位置上
                et_user_name.setSelection(username.length());
            }
        }
    }

    //记住密码之后下次登录时回显数据
    private void initDate() {
        if (sps == null) {
            sps = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        //下次登录时回显数据
        et_user_name.setText(sps.getString(USERNAME, ""));
        et_pwd.setText(sps.getString(PWD, ""));
        IsChecked = sps.getBoolean(IS_CHECKED, false);
        cb_remeberme.setChecked(IsChecked);
    }

    public static String getmUsername() {
        return mUsername;
    }

}