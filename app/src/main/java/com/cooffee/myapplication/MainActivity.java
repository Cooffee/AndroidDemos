package com.cooffee.myapplication;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.cooffee.myapplication.breakdown.BreakDownActivity;
import com.cooffee.myapplication.commonadapter.CommonAdapter;
import com.cooffee.myapplication.commonadapter.utils.ViewHolder;
import com.cooffee.myapplication.loadmore.RefreshLayout;
import com.cooffee.myapplication.thirdpartlogin.ThirdPartLoginActivity;
import com.cooffee.myapplication.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    // toolbar控件关联
    @BindView(R.id.app_bar)
    Toolbar mToolbar;

    // 关联listview
    @BindView(R.id.lv_demos)
    ListView mLvDemos;

    // 关联下拉刷新控件
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    // 通用适配器声明
    private CommonAdapter<String> mAdapter;

    // 案例字符串数组
    private String[] demos = {
            "断点续传", "第三方登录", "第三方支付", "mp3及mp4播放",
            "markdown", "蓝牙连接", "加载word"
    };

    private List<String> mDemoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化toolbar
        initToolbar("案例积累", Constant.TOOLBAR_BASE);

        // 初始化拉动刷新控件
        initRefreshLayout();

        // 初始化数据
        initData();

        // 配置适配器
        setAdapter();

        //注册监听事件
        initListener();
    }

    /**
     * 初始化拉动刷新控件
     */
    private void initRefreshLayout() {
        // 初始化颜色
        int[] colors = {R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimaryLight};
        // 将颜色放入刷新控件里面
        mRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 添加数据
        if (mDemoList != null) {
            mDemoList.add("断点续传");
            mDemoList.add("第三方登录");
            mDemoList.add("第三方支付");
            mDemoList.add("mp3及mp4播放");
            mDemoList.add("markdown");
            mDemoList.add("蓝牙连接");
            mDemoList.add("加载word");
        } else { // 如果案例列表不存在
            // 实例化案例列表
            mDemoList = new ArrayList<>();
            // 递归
            initData();
        }
    }

    /**
     * 配置适配器
     */
    private void setAdapter() {
        // 实例化适配器
        mAdapter = new CommonAdapter<String>(MainActivity.this, mDemoList, android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewHolder holder, String s) {
                // 将案例数据显示到视图控件中
                holder.setText(android.R.id.text1, s);
            }
        };
        // 给listview设置adapter
        mLvDemos.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.lv_demos)
    void clickItem(int position) {
        switch (position) {
            case Constant.RESUME_FROM_BREAK_DOWN: // 断点续传
                // 跳转到断点续传界面
                Intent intentBreakdown = new Intent(this, BreakDownActivity.class);
                startActivity(intentBreakdown);
                break;
            case Constant.THIRD_PART_LOGIN: // 第三方登录
                // 跳转到第三方登录界面
                Intent intentLogin = new Intent(this, ThirdPartLoginActivity.class);
                startActivity(intentLogin);
                break;
            case Constant.THIRD_PART_PAY: // 第三方支付

                break;
            case Constant.MP3_MP4_PLAY: // mp3 mp4播放

                break;
            case Constant.MARK_DOWN: // markdown显示

                break;
            case Constant.BLUE_TOOTH: // 蓝牙连接

                break;
            case Constant.LOAD_WORD: // 加载word文档

                break;
        }
    }

    /**
     * 注册监听事件
     */
    private void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadListener(this);
    }

    /**
     * 刷新时调用
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO：更新ListView数据
                // 适配器数据更新
                mAdapter.notifyDataSetChanged();
                // 提示刷新完毕
                Toast.makeText(MainActivity.this, "刷新完毕", Toast.LENGTH_SHORT).show();
                // 设置不在刷新状态
                mRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    /**
     * 加载更多时调用
     */
    @Override
    public void onLoad() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 数据列表添加一条数据
//                mDemoList.add("加载。。。");
                // 更新适配器
//                mAdapter.notifyDataSetChanged();
                // 提示加载完成
                Toast.makeText(MainActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                // 设置不在刷新状态
                mRefreshLayout.setLoading(false);
            }
        }, 1500);
    }
}
