package com.cooffee.myapplication.breakdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cooffee.myapplication.BaseActivity;
import com.cooffee.myapplication.R;
import com.cooffee.myapplication.breakdown.db.ThreadDAO;
import com.cooffee.myapplication.breakdown.db.ThreadDAOImpl;
import com.cooffee.myapplication.breakdown.pojo.FileInfo;
import com.cooffee.myapplication.breakdown.pojo.ThreadInfo;
import com.cooffee.myapplication.breakdown.service.DownloadService;
import com.cooffee.myapplication.breakdown.service.DownloadTask;
import com.cooffee.myapplication.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BreakDownActivity extends BaseActivity {

    // toolbar控件关联
    @BindView(R.id.app_bar)
    Toolbar mToolbar;

    // 下载按钮关联控件
    @BindView(R.id.btn_download)
    Button mBtnDownload;

    // 取消按钮关联控件
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;

    // 文件名称文本关联控件
    @BindView(R.id.tv_fileName)
    TextView mTvFileName;

    // 进度条加载控件关联
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    // 下载
    private static String DOWNLOAD = null;
    // 暂停
    private static String PAUSE = null;

    // 是否在下载状态
    boolean isDownloading;

    // 文件信息对象
    FileInfo mFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_down);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化toolbar
        initToolbar("断点续传", Constant.TOOLBAR_UP);

        // 初始化资源
        initResource();

        // 初始化文件信息
        initFileInfo();

        // 初始化视图
        initView();

        // 初始化广播
        initBroadcaster();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(mReceiver);
    }

    /**
     * 初始化资源
     * 从资源文件中获取文本、图片等
     */
    private void initResource() {
        // 下载
        DOWNLOAD = getString(R.string.download);
        // 暂停
        PAUSE = getString(R.string.pause);
    }

    /**
     * 初始化视图
     */
    private void initView() {
//        // -初始化toolbar
//        // 设置标题
//        mToolbar.setTitle("断点续传");
//        // 配置toolbar到activity中
//        setSupportActionBar(mToolbar);
//        // 激活toolbar的home键
//        getSupportActionBar().setHomeButtonEnabled(true);
//        // 显示返回上一层按钮
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // -初始化进度条
        // 初始化意图
        Intent intent = new Intent(this, DownloadService.class);
        // 意图动作设置 查看进度
        intent.setAction(DownloadService.ACTION_CHECK_PROCESS);
        // 意图传参 文件信息
        intent.putExtra(Constant.FILE_INFO, mFileInfo);
        // 开启服务
        startService(intent);

        // 显示下载的文件名
        mTvFileName.setText(mFileInfo.getFileName());
    }

    /**
     * 初始化文件信息
     */
    private void initFileInfo() {
        // 实例化文件信息对象
        mFileInfo = new FileInfo(0, "http://wj1024.com/AdobeFlashPlayer_22_a_install.dmg",
                "AdobeFlashPlayer_22_a_install.dmg", 0, 0);
    }

    /**
     * 初始化广播
     */
    private void initBroadcaster() {
        // 定义过滤器
        IntentFilter filter = new IntentFilter();
        // 添加动作过滤
        filter.addAction(DownloadService.ACTION_UPDATE);
        // 注册广播
        registerReceiver(mReceiver, filter);
    }

    /**
     * 点击下载按钮时候调用
     */
    @OnClick(R.id.btn_download)
    void clickDownload(Button btn) {
        // 将取消按钮设置为可点击
        mBtnCancel.setClickable(true);
        // 根据下载状态的不同进行相应的操作
        if (isDownloading == false) { // 如果当前没有在下载状态
            // 将状态设置为下载
            isDownloading = true;
            // 下载按钮文本设置为暂停
            btn.setText(PAUSE);
            // 创建意图，并添加服务
            Intent intentStart = new Intent(this, DownloadService.class);
            // 设置意图动作
            intentStart.setAction(DownloadService.ACTION_START);
            // 添加数据
            intentStart.putExtra(Constant.FILE_INFO, mFileInfo);
            // 开启服务
            startService(intentStart);
        } else { // 如果当前在下载状态
            // 将状态设置为暂停
            isDownloading = false;
            // 下载按钮文本更改为下载
            btn.setText(DOWNLOAD);
            // 创建意图，并添加服务
            Intent intentStop = new Intent(this, DownloadService.class);
            // 设置意图动作 取消下载
            intentStop.setAction(DownloadService.ACTION_STOP);
            // 添加数据
            intentStop.putExtra(Constant.FILE_INFO, mFileInfo);
            // 开启服务
            startService(intentStop);
        }
    }

    /**
     * 点击取消按钮调用
     */
    @OnClick(R.id.btn_cancel)
    void clickCancel(Button btn) {
        // 设置状态为未下载
        isDownloading = false;
        // 使取消按钮不可点击
        btn.setClickable(false);
        // 将下载按钮文本显示为”下载“
        mBtnDownload.setText(DOWNLOAD);
        // 创建意图，添加取消下载的服务
        Intent intentCancel = new Intent(this, DownloadService.class);
        // 设置意图动作 取消下载
        intentCancel.setAction(DownloadService.ACTION_CANCEL);
        // 添加数据
        intentCancel.putExtra(Constant.FILE_INFO, mFileInfo);
        // 开启服务
        startService(intentCancel);
    }

    /**
     * 定义并实例化广播接收器
     * 用于更新UI，接收下载进度
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        // 获取当前的时间，更新UI的时间间隔
        long time = System.currentTimeMillis();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                // 每隔500毫秒更新一次UI
//                if (System.currentTimeMillis() - time > 500) {
                // 从当前时间重新计时
//                    time = System.currentTimeMillis();
                // 获取下载进度
                int finished = intent.getIntExtra(Constant.KEY_FINISHED, 0);
                Log.d(Constant.TAG, "finished is " + finished);
                // 进度条设置进度
                mProgressBar.setProgress(finished);

                // 如果finish为100说明下载完毕
                if (finished == 100) {
                    // 如果下载完毕，将按钮设置为"下载"
                    mBtnDownload.setText(DOWNLOAD);
                    // 将下载状态重置
                    isDownloading = false;
                    // 将取消按钮设置为不可点击
                    mBtnCancel.setClickable(false);
                }


//                }
            }
        }
    };
}
