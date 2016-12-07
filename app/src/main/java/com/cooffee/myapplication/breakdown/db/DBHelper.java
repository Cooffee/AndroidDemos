package com.cooffee.myapplication.breakdown.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cooffee.myapplication.utils.Constant;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.db
 * 类功能：数据库帮助类，帮助实现数据库的创建和更新
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 下午4:12
 * 联系邮箱: wjnovember@icloud.com
 */
public class DBHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DB_NAME = "download.db";
    // 数据库版本号
    private static final int VERSION = 1;
    // 数据库表创建语句
    private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
            "thread_id integer, url text, start integer, end integer, finished integer)";
    // 数据库表删除语句
    private static final String SQL_DROP = "drop table if exists thread_info";

    // 帮助类实例
    private static DBHelper mInstance;

    /**
     * 数据库帮助类构造方法
     * @param context 上下文
     */
    private DBHelper(Context context) {
        // 调用父类构造方法
        super(context, DB_NAME, null, VERSION);
        Log.i(Constant.TAG, "sql_create is " + SQL_CREATE);
    }

    /**
     * 获取帮助类实例
     */
    public static DBHelper getInstance(Context context) {
        // 如果帮助类没有实例化
        if (mInstance == null) {
            // 在同步条件下
            synchronized (DBHelper.class) {
                // 如果帮助类没有实例化
                if (mInstance == null) {
                    // 实例化帮助类
                    mInstance = new DBHelper(context);
                }
            }
        }
        // 返回实例
        return mInstance;
    }

    /**
     * 数据库创建时调用
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(Constant.TAG, "dbhelper onCreate -- " + SQL_CREATE);
        // 执行数据库表创建语句
        db.execSQL(SQL_CREATE);
    }

    /**
     * 数据库更新的时候调用
     * @param db 数据库类
     * @param oldVersion 老版本号
     * @param newVersion 新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 执行数据库表删除语句
        db.execSQL(SQL_DROP);
        // 执行数据库表创建语句
        db.execSQL(SQL_CREATE);
    }
}
