package com.cooffee.myapplication.breakdown.db;

import com.cooffee.myapplication.breakdown.pojo.ThreadInfo;

import java.util.List;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.db
 * 类功能：线程数据库访问接口，数据库的增删改查
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 下午4:24
 * 联系邮箱: wjnovember@icloud.com
 */
public interface ThreadDAO {

    /**
     * 插入线程信息
     * @param threadInfo 线程信息对象
     */
    public void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程
     * @param url 文件下载的地址
     * @param threadId 线程ID
     */
    public void deleteThread(String url, int threadId);

    /**
     * 更新线程进度
     * @param url 文件下载的地址
     * @param threadId 线程编号
     * @param finished 线程完成程度
     */
    public void updateThread(String url, int threadId, int finished);

    /**
     * 查询文件的线程信息
     * @param url 文件下载的地址
     * @return 线程信息列表
     */
    public List<ThreadInfo> getThreads(String url);

    /**
     * 线程信息是否存在
     * @param url 文件下载的地址
     * @return 布尔值
     */
    public boolean isExists(String url, int thread_id);
}
