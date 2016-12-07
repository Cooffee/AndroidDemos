package com.cooffee.myapplication.thirdpartlogin.qq.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cooffee.myapplication.thirdpartlogin.qq.QQActivity;
import com.cooffee.myapplication.thirdpartlogin.qq.QQLoginSuccessActivity;
import com.cooffee.myapplication.thirdpartlogin.qq.service.LoginService;
import com.cooffee.myapplication.thirdpartlogin.qq.utils.QQConstant;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * 项目名称：UsefulDemos
 * 模块名称：第三方登录
 * 包名：com.cooffee.myapplication.thirdpartlogin.qq.listeners
 * 类功能：QQ登录ui事件监听类
 * 创建人：cooffee
 * 创建时间：2016 16/7/11 上午10:41
 * 联系邮箱: wjnovember@icloud.com
 */
public class BaseUIListener implements IUiListener {

    // 定义上下文
    private Context mContext;
    // 动作
    private String mAction;

    /**
     * 有参构造方法
     * @param context 上下文
     */
    public BaseUIListener(Context context, String action) {
        // 上下文赋值
        this.mContext = context;
        // 动作赋值
        this.mAction = action;
    }
    @Override
    public void onComplete(Object response) {
        Log.d(QQConstant.TAG, "onComplete");
        Log.d(QQConstant.TAG, "the response is " + ((JSONObject)response).toString());

        // 定义意图
        Intent intent = null;
        // 根据动作跳转不同的界面
        if (LoginService.ACTION_LOGIN.equals(mAction)) { // 如果是登录动作
            // 跳转登录成功界面
            intent = new Intent(mContext, QQLoginSuccessActivity.class);
        } else if (LoginService.ACTION_LOGOUT.equals(mAction)) { // 如果是注销动作
            // 跳转QQ操作界面
            intent = new Intent(mContext, QQActivity.class);
        }
        // 执行意图
        mContext.startActivity(intent);
    }

    private void doComplete(JSONObject values) {

    }

    @Override
    public void onError(UiError e) {
        Log.d(QQConstant.TAG, "code:" + e.errorCode + ", msg:" +
            e.errorMessage + ", detail:" + e.errorDetail);
    }

    @Override
    public void onCancel() {
        Log.d(QQConstant.TAG, "onCancel");
    }
}
