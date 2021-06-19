package com.jiangqi.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangqi.booking.model.AccountBean;
import com.jiangqi.booking.adapter.AccountAdapter;
import com.jiangqi.booking.db.DBManager;
import com.jiangqi.booking.utils.AnalysisUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean>mDatas;   //数据源
    AccountAdapter adapter;    //适配器对象
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this,mDatas);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv);   //设置无数局时，显示的控件
        Context context=getApplicationContext();
        username= AnalysisUtils.readLoginUserName(context);
    }

    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:   //执行搜索的操作
                String msg = searchEt.getText().toString().trim();
//                判断输入内容是否为空，如果为空，就提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始搜索
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg,username);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
