package com.cooffee.myapplication.thirdpartlogin.qq;

import android.content.Intent;
import android.os.Bundle;

import com.cooffee.myapplication.BaseActivity;
import com.cooffee.myapplication.R;
import com.cooffee.myapplication.thirdpartlogin.qq.listeners.BaseUIListener;
import com.cooffee.myapplication.thirdpartlogin.qq.utils.QQConstant;
import com.cooffee.myapplication.utils.Constant;
import com.tencent.tauth.Tencent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class QQLoginSuccessActivity extends BaseActivity {

    // 已实现序列化的Tencent对象
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化数据
        initData();

        // 初始化toolbar
        initToolbar("登录成功", Constant.TOOLBAR_BASE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 获取bundle数据
        Bundle bundle = getIntent().getExtras();
        // 如果bundle不存在
        if (bundle == null) {
            // 退出方法
            return;
        }
        // 获取Tencent对象
        mTencent = (Tencent) bundle.getSerializable(QQConstant.BUNDLE_TENTCENT);
    }

    @OnClick(R.id.btn_logout)
    void logOut() {
        // 获取app_id
//        String appId = getString(R.string.qq_app_id);
        // 创建Tencent实例
//        mTencent = MyTencent.createInstance(appId, getApplicationContext());
        // 注销
        mTencent.logout(this);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 调用父类方法
        super.onActivityResult(requestCode, resultCode, data);
        // 注销
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUIListener(this, mTencent));
    }
}
