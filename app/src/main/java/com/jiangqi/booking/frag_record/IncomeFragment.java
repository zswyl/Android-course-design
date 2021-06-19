package com.jiangqi.booking.frag_record;
import com.jiangqi.booking.LoginActivity;
import com.jiangqi.booking.model.TypeBean;
import com.jiangqi.booking.R;
import com.jiangqi.booking.db.DBManager;

import java.util.List;
/**
 * 收入记录页面
 */
public class IncomeFragment extends BaseRecordFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean, LoginActivity.getmUsername());
    }
}
