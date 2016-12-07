package com.cooffee.myapplication.breakdown.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cooffee.myapplication.breakdown.db.ThreadDAO;
import com.cooffee.myapplication.breakdown.db.ThreadDAOImpl;
import com.cooffee.myapplication.breakdown.pojo.FileInfo;
import com.cooffee.myapplication.breakdown.pojo.ThreadInfo;
import com.cooffee.myapplication.utils.Constant;
import com.cooffee.myapplication.utils.MyPreferenceUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.service
 * 类功能：下载任务类，对文件进行下载
 * 创建人：cooffee
 * 创建时间：2016 16/7/8 上午9:31
 * 联系邮箱: wjnovember@icloud.com
 */
public class DownloadTask {
    // 定义上下文
    private Context mContext;
    // 定义文件信息对象
    private FileInfo mFileInfo;
    // 定义数据库对象
    private ThreadDAO mDao;
    // 文件总的已完成的长度
    private int mFinished = 0;
    // 线程是否暂停
    public boolean isPause = false;
    // 线程是否取消
    public boolean isCancel = false;

    /**
     * 有参构造方法
     *
     * @param context  上下文
     * @param fileInfo 文件信息
     */
    public DownloadTask(Context context, FileInfo fileInfo) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mDao = new ThreadDAOImpl(mContext);
    }

    /**
     * 下载
     */
    public void download() {
        // 读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDao.getThreads(mFileInfo.getUrl());
        // 定义线程信息对象
        ThreadInfo threadInfo = null;
        // 对线程信息集合是否存在进行判断
        if (threadInfos.size() == 0) { // 如果线程信息集合没有线程信息
            // 实例化线程信息对象
            threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else { // 如果有线程信息
            // 获取线程信息对象
            threadInfo = threadInfos.get(0);
        }
        // 创建子线程进行下载
        new DownloadThread(threadInfo).start();
    }

    /**
     * 检查下载进度
     */
    public void checkProcess() {
        // 读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDao.getThreads(mFileInfo.getUrl());
        // 定义线程信息对象
        ThreadInfo threadInfo = null;
        // 意图 更新进度
        Intent intent = new Intent(DownloadService.ACTION_UPDATE);
        // 获取下载文件的大小
        int length = mFileInfo.getLength();
        // 文件已下载的大小
        int finished = 0;
        // 文件下载进度百分比
        int percent = 0;
        // 对线程信息集合是否存在进行判断
        if (threadInfos.size() == 0) { // 如果线程信息集合没有线程信息
            // 实例化线程信息对象
            threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else { // 如果有线程信息
            // 获取线程信息对象
            threadInfo = threadInfos.get(0);
            // 获取文件已经下载的大小
            finished = threadInfo.getFinished();
            if (length == 0) {
                percent = 0;
            } else {
                // 获取文件下载进度百分比
                percent = finished * 100 / length;
            }
        }
        // intent传参 下载进度(百分比)
        intent.putExtra(Constant.KEY_FINISHED, percent);
        // 发送广播
        mContext.sendBroadcast(intent);
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        // 读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDao.getThreads(mFileInfo.getUrl());
        // 定义线程信息对象
        ThreadInfo threadInfo = null;
        // 意图 更新进度
        Intent intent = new Intent(DownloadService.ACTION_UPDATE);
        // 对线程信息集合是否存在进行判断
        if (threadInfos.size() > 0) { // 如果线程信息集合有线程信息
            // 获取线程信息对象
            threadInfo = threadInfos.get(0);
            mDao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
        }
        // intent传参 下载进度(百分比)
        intent.putExtra(Constant.KEY_FINISHED, 0);
        // 发送广播
        mContext.sendBroadcast(intent);
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        // 定义线程信息
        private ThreadInfo mThreadInfo;

        /**
         * 有参构造方法
         *
         * @param threadInfo 线程信息
         */
        public DownloadThread(ThreadInfo threadInfo) {
            // 线程信息
            this.mThreadInfo = threadInfo;
        }

        public void run() {
            // -向数据库插入线程信息
            // 如果数据库不存在
            if (!mDao.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
                // 将线程信息插入数据库
                mDao.insertThread(mThreadInfo);
            }
            // 定义网络连接对象
            HttpURLConnection conn = null;
            // 定义文件随机访问流
            RandomAccessFile raf = null;
            // 定义文件输出流
            InputStream input = null;
            try {
                // 设置下载位置
                URL url = new URL(mThreadInfo.getUrl());
                // 连接网络
                conn = (HttpURLConnection) url.openConnection();
                // 设置连接超时时间
                conn.setConnectTimeout(3000);
                // 设置网络连接请求方式 GET
                conn.setRequestMethod("GET");
                // 设置下载开始位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                // 设置请求属性
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                // 设置写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                // 随机访问文件流
                raf = new RandomAccessFile(file, "rwd");
                // 找到写入位置
                raf.seek(start);

                // 定义意图，设置动作为信息(下载进度)更新
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                // 设置已完成的下载进度
                mFinished += mThreadInfo.getFinished();
                // 开始下载
                if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    // 读取数据
                    input = conn.getInputStream();
                    // 定义字节缓冲
                    byte[] buffer = new byte[1024];
                    // 定义读取的长度
                    int len = -1;
                    while ((len = input.read(buffer)) != -1) {
                        // 取消下载
                        if (isCancel) {
                            Log.d(Constant.TAG, "enter cancel");
                            // intent传值下载进度为0
                            intent.putExtra(Constant.KEY_FINISHED, 0);
                            // 发送广播
                            mContext.sendBroadcast(intent);
                            // 退出循环
                            break;
                        }
                        // 写入文件
                        raf.write(buffer, 0, len);
                        // 把下载进度发送广播给Activity
                        mFinished += len;
                        // intent传值下载进度(百分比形式)
                        intent.putExtra(Constant.KEY_FINISHED, mFinished * 100 / mFileInfo.getLength());
                        // 发送广播
                        mContext.sendBroadcast(intent);


                        // 如果线程处于暂停状态
                        if (isPause) {
                            // 下载暂停时候保存下载进度
                            mDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            // 退出方法
                            return;
                        }
                    }
                    // 删除线程信息
                    mDao.deleteThread(mThreadInfo.getUrl(), mThreadInfo.getId());
                    Log.d(Constant.TAG, "delete");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        // 关闭输出流
                        input.close();
                    }
                    if (conn != null) {
                        // 断掉网络连接
                        conn.disconnect();
                    }
                    if (raf != null) {
                        // 关闭文件流
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
