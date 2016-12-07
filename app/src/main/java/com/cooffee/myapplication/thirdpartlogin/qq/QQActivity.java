package com.cooffee.myapplication.thirdpartlogin.qq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.cooffee.myapplication.BaseActivity;
import com.cooffee.myapplication.R;
import com.cooffee.myapplication.commonadapter.CommonAdapter;
import com.cooffee.myapplication.commonadapter.utils.ViewHolder;
import com.cooffee.myapplication.thirdpartlogin.qq.listeners.BaseUIListener;
import com.cooffee.myapplication.thirdpartlogin.qq.service.LoginService;
import com.cooffee.myapplication.thirdpartlogin.qq.utils.QQConstant;
import com.cooffee.myapplication.utils.Constant;
import com.tencent.tauth.Tencent;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class QQActivity extends BaseActivity {

    @BindView(R.id.lv_qq)
    ListView mLvQQ;

    // 定义Tencent对象
    private Tencent mTencent;

    // 视图监听事件
    BaseUIListener mUiListener;

    // 列表数据
    private List<String> mOpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化toolbar
        initToolbar("QQ登录", Constant.TOOLBAR_UP);

        // 初始化列表
        initList();
    }

    /**
     * 初始化列表
     */
    private void initList() {
        if (mOpList != null) {
            // 初始化数据
            mOpList.add("QQ登录和注销");
            mOpList.add("分享消息到QQ（定向分享）");
            mOpList.add("获取用户信息");
            mOpList.add("更换QQ头像");
        } else {
            // 实例化操作列表
            mOpList = new ArrayList<>();
            // 初始化列表
            initList();
            // 退出方法
            return;
        }

        // listview配置适配器
        mLvQQ.setAdapter(new CommonAdapter<String>(this, mOpList, android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewHolder holder, String s) {
                // 设置每一项文本信息
                holder.setText(android.R.id.text1, s);
            }
        });

        Log.d(QQConstant.TAG, "the adapter is " + mLvQQ.getAdapter().getCount());
    }

    /**
     * ListView子项点击事件
     *
     * @param position
     */
    @OnItemClick(R.id.lv_qq)
    void clickItem(int position) {
        switch (position) {
            case QQConstant.LOGIN_LOGOUT: // 登录注销
                // QQ登录
                qqLogin();
                break;
            case QQConstant.SHARE_TO_QQ: // 分享至QQ

                break;
            case QQConstant.GET_USER_INFO: // 获取用户信息

                break;
            case QQConstant.CHANGE_AVATAR: // 更改头像

                break;
        }
    }

    /**
     * qq登录
     */
    private void qqLogin() {
        // 定义服务
        Intent intent = new Intent(this, LoginService.class);
        // 执行服务
        startService(intent);
    }

    /**
     * 执行意图后的回调方法
     *
     * @param requestCode 请求码
     * @param resultCode  结果代码
     * @param data        意图
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 调用父类方法
        super.onActivityResult(requestCode, resultCode, data);
        // qq登录成功后返回的数据
        Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
    }
}
