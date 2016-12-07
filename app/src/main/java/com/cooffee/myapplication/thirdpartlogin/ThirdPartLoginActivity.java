package com.cooffee.myapplication.thirdpartlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.cooffee.myapplication.BaseActivity;
import com.cooffee.myapplication.R;
import com.cooffee.myapplication.thirdpartlogin.qq.QQActivity;
import com.cooffee.myapplication.thirdpartlogin.qq.listeners.BaseUIListener;
import com.cooffee.myapplication.utils.Constant;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdPartLoginActivity extends BaseActivity {

    // QQ登录按钮控件关联
    @BindView(R.id.img_qq)
    ImageView mImgQQ;

    // 微信登录按钮控件关联
    @BindView(R.id.img_wechat)
    ImageView mImgWechat;

    // 新浪登录按钮控件关联
    @BindView(R.id.img_sina)
    ImageView mImgSina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_part_login);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化toolbar
        initToolbar("第三方登录", Constant.TOOLBAR_UP);
    }

    @OnClick(R.id.img_qq)
    void clickQQLogin() {
        Intent intent = new Intent(this, QQActivity.class);
        startActivity(intent);
    }
}