package com.jiangqi.booking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiangqi.booking.model.AccountBean;
import com.jiangqi.booking.model.BarChartItemBean;
import com.jiangqi.booking.model.ChartItemBean;
import com.jiangqi.booking.model.TypeBean;
import com.jiangqi.booking.utils.FloatUtils;

import java.util.ArrayList;
import java.util.List;

/*
* 负责管理数据库的类
*   主要对于表当中的内容进行操作，增删改查
* */
public class DBManager {

    private static SQLiteDatabase db;
    /* 初始化数据库对象*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //得到帮助类对象
        db = helper.getWritableDatabase();      //得到数据库对象
    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     *   kind :表示收入或者支出
     * */
    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
//        循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    /*
    * 向记账表当中插入一条元素
    * */
    public static void insertItemToAccounttb(AccountBean bean, String username){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        values.put("username",username);
        db.insert("accounttb",null,values);
    }

    /**
     * 向预算表中插入预算金额
     */
    public static void insertItemToBudgettb(float money,String username){
        ContentValues values = new ContentValues();
        values.put("money",money);
        values.put("username",username);
        db.insert("budgettb",null,values);
    }

    /**
     * 向预算表中获取预算金额
     */
    public static float getBudgettbMoney(String username){
        String sql = "select * from budgettb where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username+""});
        float money=0;
        if (cursor.moveToNext()) {
            money= cursor.getFloat(cursor.getColumnIndex("money"));
        }
        return money;
    }
    /**
     * 向预算表中修改预算金额
     */
    public static void updateBudgettb(float money,String username){
        String sql = "update budgettb set money=? where username=?";
        db.execSQL(sql,new String[]{money+"",username+""});
    }

    /*
    * 获取记账表当中某一天的所有支出或者收入情况
    * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day,String username){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? and username=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "",username + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month,String username){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and username=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "",username +""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 获取某一天的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind,String username){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=? and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + "",username+""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * 获取某一月的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind,String username){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + "",username + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /** 统计某月份支出或者收入情况有多少条  收入-1   支出-0*/
    public static int getCountItemOneMonth(int year,int month,int kind,String username){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=? and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + "",username + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }
    /**
     * 获取某一年的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneYear(int year,int kind, String username){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=? and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + "",username + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
    * 根据传入的id，删除accounttb表当中的一条数据
    * */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=? ", new String[]{id + ""});
        return i;
    }

    public static float getItemFromAccounttbById(int id){
        String sql="select money from accounttb where id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id+""});
        float money = 0;
        if (cursor.moveToFirst()){
            money=cursor.getFloat(cursor.getColumnIndex("money"));
        }
        return money;
    }
    /**
     * 根据备注搜索收入或者支出的情况列表
     * */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu,String username){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%' and username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 查询记账的表当中有几个年份信息
     * */
    public static List<Integer>getYearListFromAccounttb(String username){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb where username=? order by year asc";
        Cursor cursor = db.rawQuery(sql, new String[]{username + ""});
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /*
    * 删除accounttb表格当中的数据
    * */
    public static void deleteAllAccount(String username){
        db.delete("accounttb", "username=? ", new String[]{username});
        db.delete("budgettb", "username=? ", new String[]{username});
    }

    /**
     * 查询指定年份和月份的收入或者支出每一种类型的总钱数
     * */
    public static List<ChartItemBean>getChartListFromAccounttb(int year, int month, int kind, String username){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind,username);  //求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? and username=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + "",username +""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    /**
    * 获取这个月当中某一天收入支出最大的金额，金额是多少
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind,String  username){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? and username=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + "",username +""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /** 根据指定月份每一日收入或者支出的总钱数的集合*/
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year, int month, int kind, String username){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? and username=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + "",username + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }
}