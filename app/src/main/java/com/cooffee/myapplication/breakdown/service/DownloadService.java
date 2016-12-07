package com.cooffee.myapplication.breakdown.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cooffee.myapplication.breakdown.pojo.FileInfo;
import com.cooffee.myapplication.utils.Constant;
import com.cooffee.myapplication.utils.MyPreferenceUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.service
 * 类功能：下载服务，后台实现文件的下载，实现断点续传
 * 记录文件长度，记录上次下载点的长度位置，并保存在数据库中
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 上午10:59
 * 联系邮箱: wjnovember@icloud.com
 */
public class DownloadService extends Service {

    // 动作 开始
    public static final String ACTION_START = "ACTION_START";
    // 动作 结束
    public static final String ACTION_STOP = "ACTION_STOP";
    // 动作 发送进度给Activity（信息更新）
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    // 动作 取消
    public static final String ACTION_CANCEL = "ACTION_CANCEL";
    // 动作 查看进度
    public static final String ACTION_CHECK_PROCESS = "ACTION_CHECK_PROCESS";
    // 下载的文件的保存位置
    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/downloads/";

    // Handler消息初始化标记
    public static final int MSG_DOWNLOAD = 0x00;
    // Handler消息查看进度标记
    public static final int MSG_CHECK_PROCESS = 0x01;
    // Handler消息取消下载标记
    public static final int MSG_CANCEL = 0x02;

    // 定义当前操作
    private int mOperation = MSG_DOWNLOAD;

    // 定义下载任务
    private DownloadTask mTask = null;

    /**
     * onStart方法是在Android2.0之前的平台使用的搜索.
     * 在2.0及其之后，则需重写onStartCommand方法
     * 同时，旧的onStart方法则不会再被直接调用
     * （外部调用onStartCommand，而onStartCommand里会再调用 onStart。
     * 在2.0之后，推荐覆盖onStartCommand方法
     * 而为了向前兼容，在onStartCommand依然会调用onStart方法。
     *
     * @param intent  意图
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     *                有关开始请求的相关数据
     *                START_STICKY
     *                如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
     *                随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(
     *                Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
     *                <p/>
     *                START_NOT_STICKY
     *                “非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
     *                <p/>
     *                START_REDELIVER_INTENT
     *                重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，
     *                并将Intent的值传入。
     *                <p/>
     *                START_STICKY_COMPATIBILITY
     *                START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     *                请求开始的id
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 根据意图的动作执行不同的操作
        // 获取Activity传来的参数
        if (ACTION_START.equals(intent.getAction())) { // 开始动作
            // 获取文件信息
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Constant.FILE_INFO);
            // 定义当前操作为下载
            mOperation = MSG_DOWNLOAD;
            // 初始化线程开启
            new InitThread(fileInfo).start();
        } else if (ACTION_STOP.equals(intent.getAction())) { // 结束动作
            Log.d(Constant.TAG, "stop");
            // 获取文件信息
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Constant.FILE_INFO);
            // 暂停线程下载任务
            if (mTask != null) {
                // 设置下载任务为暂停下载状态
                mTask.isPause = true;
            }
        } else if (ACTION_CANCEL.equals(intent.getAction())) { // 取消动作
            // 获取文件信息
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Constant.FILE_INFO);
            // 取消线程下载任务
            if (mTask != null) {
                // 针对是否在下载中，取消下载有不同的操作
                if (mTask.isPause) { // 如果在下载暂停时取消下载任务
                    // 定义当前操作为取消下载
                    mOperation = MSG_CANCEL;
                    // 初始化线程开启
                    new InitThread(fileInfo).start();
                } else { // 如果在下载中取消下载任务
                    // 设置线程下载任务为取消下载状态
                    mTask.isCancel = true;
                }
            }

        } else if (ACTION_CHECK_PROCESS.equals(intent.getAction())) { // 查看进度动作
            // 获取文件信息
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(Constant.FILE_INFO);
            // 定义当前操作为查看进度
            mOperation = MSG_CHECK_PROCESS;
            // 初始化线程开启
            new InitThread(fileInfo).start();
        }
        // 调用并返回父类onStartCommand方法
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DOWNLOAD:
                    // 获取文件信息对象
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    // 启动线程下载任务
                    mTask = new DownloadTask(DownloadService.this, fileInfo);
                    // 开始下载
                    mTask.download();
                    break;
                case MSG_CHECK_PROCESS:
                    // 获取文件信息对象
                    FileInfo fileInfo1 = (FileInfo) msg.obj;
                    Log.d(Constant.TAG, "in handler the file info is " + fileInfo1.toString());
                    // 启动线程下载任务
                    mTask = new DownloadTask(DownloadService.this, fileInfo1);
                    // 开始下载
                    mTask.checkProcess();
                    break;
                case MSG_CANCEL:
                    // 获取文件信息对象
                    FileInfo fileInfo2 = (FileInfo) msg.obj;
                    Log.d(Constant.TAG, "in handler the file info is " + fileInfo2.toString());
                    // 启动线程下载任务
                    mTask = new DownloadTask(DownloadService.this, fileInfo2);
                    // 开始下载
                    mTask.cancelDownload();
                    break;
            }
        }
    };

    /**
     * 初始化线程类
     */
    class InitThread extends Thread {

        // 文件信息对象的声明
        private FileInfo mFileInfo = null;

        /**
         * 有参构造方法
         *
         * @param fileInfo 文件信息
         */
        public InitThread(FileInfo fileInfo) {
            this.mFileInfo = fileInfo;
        }

        public void run() {
            // 声明网络连接对象
            HttpURLConnection conn = null;
            // 声明一个特殊的输出流，在文件的任意位置进行写入操作
            RandomAccessFile raf = null;
            try {
                // 获取文件下载的URL
                URL url = new URL(mFileInfo.getUrl());
                // 建立连接
                conn = (HttpURLConnection) url.openConnection();
                // 设置网络连接超时时间
                conn.setConnectTimeout(3000);
                // 设置网络请求方法为GET
                conn.setRequestMethod("GET");
                // 声明文件长度
                int length = -1;
                // 如果连接成功，网络畅通
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    // 获取文件长度
                    length = conn.getContentLength();
                }
                // 如果文件长度小于0，说明网络连接不成功或文件不存在
                if (length < 0) {
                    // 退出run方法
                    return;
                }
                // 新建一个文件类
                File dir = new File(DOWNLOAD_PATH);
                // 如果文件夹不存在
                if (!dir.exists()) {
                    // 创建一个文件夹
                    dir.mkdir();
                }
                // 在新建的文件夹中创建一个文件
                File file = new File(dir, mFileInfo.getFileName());

                // 参数二：r-read 读   w-write 写    d-delete 删除
                raf = new RandomAccessFile(file, "rwd");
                // 设置文件长度
                raf.setLength(length);
                // 将长度放入文件信息对象中
                mFileInfo.setLength(length);
                // Handler发送信息
                mHandler.obtainMessage(mOperation, mFileInfo).sendToTarget();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭连接
                conn.disconnect();
                try {
                    // 关闭输出流
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
