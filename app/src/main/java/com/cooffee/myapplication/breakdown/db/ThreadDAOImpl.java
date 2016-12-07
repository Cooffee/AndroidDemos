package com.cooffee.myapplication.breakdown.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cooffee.myapplication.breakdown.pojo.ThreadInfo;
import com.cooffee.myapplication.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.db
 * 类功能：线程数据访问接口实现，实现线程的增删改查操作
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 下午4:30
 * 联系邮箱: wjnovember@icloud.com
 */
public class ThreadDAOImpl implements ThreadDAO {

    // 定义数据库
    private DBHelper mHelper = null;

    /**
     * 有参构造方法
     *
     * @param context 上下文
     */
    public ThreadDAOImpl(Context context) {
        // 实例化数据库帮助类
        mHelper = DBHelper.getInstance(context);
    }

    /**
     * 插入线程信息
     *
     * @param threadInfo 线程信息对象
     */
    @Override
    public void insertThread(ThreadInfo threadInfo) {
        // 实例化数据库对象
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 执行数据库插入语句
        db.execSQL(
                "insert into thread_info(thread_id, url, start, end, finished) values (?, ?, ?, ?, ?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(),
                        threadInfo.getEnd(), threadInfo.getFinished()});
        // 关闭数据库
        db.close();
    }

    /**
     * 删除线程
     *
     * @param url      文件下载的地址
     * @param threadId 线程ID
     */
    @Override
    public void deleteThread(String url, int threadId) {
        // 实例化数据库对象
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 执行数据库删除语句
        db.execSQL(
                "delete from thread_info where url = ? and thread_id = ?",
                new Object[]{url, threadId});
        // 关闭数据库
        db.close();
    }

    /**
     * 更新线程信息
     * @param url      文件下载的地址
     * @param threadId 线程编号
     * @param finished 线程完成程度
     */
    @Override
    public void updateThread(String url, int threadId, int finished) {
        // 实例化数据库对象
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 执行数据库修改语句
        db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
                new Object[]{finished, url, threadId});
        // 关闭数据库
        db.close();
    }

    /**
     * 查询线程
     * @param url 文件下载的地址
     * @return 线程列表
     */
    @Override
    public List<ThreadInfo> getThreads(String url) {
        // 通过数据库助手类得到数据库对象（可读写）
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 线程列表
        List<ThreadInfo> list = new ArrayList<>();
        // 执行数据库查询语句，获取游标
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        // 遍历游标
        while (cursor.moveToNext()) {
            // 定义一个线程信息对象
            ThreadInfo thread = new ThreadInfo();
            // 通过游标获取线程信息对象的属性
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            thread.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            // 将线程信息对象添加到列表中
            list.add(thread);
        }
        // 关闭游标
        cursor.close();
        // 关闭数据库
        db.close();
        // 返回线程列表
        return list;
    }

    /**
     * 判断线程信息是否存在
     * @param url 文件下载的地址
     * @return
     */
    @Override
    public boolean isExists(String url, int thread_id) {
        // 通过数据库助手类得到数据库对象（可读写）
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 执行数据库查询语句，获取游标
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?",
                new String[]{url, String.valueOf(thread_id)});
        // 判断游标是否有数据指向
        boolean exists = cursor.moveToNext();
        // 关闭游标
        cursor.close();
        // 关闭数据库
        db.close();
        return exists;
    }
}
