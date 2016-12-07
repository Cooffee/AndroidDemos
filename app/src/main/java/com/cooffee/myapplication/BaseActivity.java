package com.cooffee.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.cooffee.myapplication.utils.Constant;

import butterknife.ButterKnife;

/**
 * 项目名称：UsefulDemos
 * 模块名称：全局
 * 包名：com.cooffee.myapplication
 * 类功能：基础Activity，实现ButterKnife框架的绑定
 * 创建人：cooffee
 * 创建时间：2016 16/7/11 下午7:58
 * 联系邮箱: wjnovember@icloud.com
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        Toolbar

    }

    /**
     * 初始化toolbar
     * @param title 显示标题
     * @param type toolbar的形式
     */
    protected void initToolbar(String title, int type) {
        String t = getString(R.string.app_name);
        if (title != null && !TextUtils.isEmpty(title)) {
            t = title;
        }
        // 关联Toolbar控件
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        // 设置标题
        toolbar.setTitle(t);
        // 将toolbar配置到activity中
        setSupportActionBar(toolbar);
        // 激活action按钮
        getSupportActionBar().setHomeButtonEnabled(true);
        // 根据type选择不同形式的toolbar
        switch (type) {
            case Constant.TOOLBAR_BASE: // 基本toolbar
                break;
            case Constant.TOOLBAR_UP: // 返回上一层activity
                // 显示返回上一层按钮
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
        }
    }
}
