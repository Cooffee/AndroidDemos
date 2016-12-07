package com.cooffee.myapplication.thirdpartlogin.qq.listeners;

import android.util.Log;

import com.cooffee.myapplication.thirdpartlogin.qq.utils.QQConstant;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * 项目名称：UsefulDemos
 * 模块名称：第三方登录
 * 包名：com.cooffee.myapplication.thirdpartlogin.qq.listeners
 * 类功能：QQ登录api接口监听类
 * 创建人：cooffee
 * 创建时间：2016 16/7/11 上午10:50
 * 联系邮箱: wjnovember@icloud.com
 */
public class BaseAPIListener implements IRequestListener {

    @Override
    public void onComplete(JSONObject response) {
        Log.d(QQConstant.TAG, "IRequestListener.onComplete:" + response.toString());
    }

    @Override
    public void onIOException(IOException e) {
        Log.d(QQConstant.TAG, "IRequestListener.onIOException" + e.getMessage());
    }

    @Override
    public void onMalformedURLException(MalformedURLException e) {
        Log.d(QQConstant.TAG, "IRequestListener.onIOException:" + e.toString());
    }

    @Override
    public void onJSONException(JSONException e) {
        Log.d(QQConstant.TAG, "IRequestListener.onJSONException:" + e.getMessage());
    }

    @Override
    public void onConnectTimeoutException(ConnectTimeoutException e) {

    }

    @Override
    public void onSocketTimeoutException(SocketTimeoutException e) {

    }

    @Override
    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

    }

    @Override
    public void onHttpStatusException(HttpUtils.HttpStatusException e) {

    }

    @Override
    public void onUnknowException(Exception e) {

    }
}
