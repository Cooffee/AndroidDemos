package com.cooffee.myapplication.thirdpartlogin.qq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cooffee.myapplication.R;
import com.cooffee.myapplication.thirdpartlogin.qq.listeners.BaseUIListener;
import com.tencent.tauth.Tencent;

/**
 * 项目名称：UsefulDemos
 * 模块名称：
 * 包名：com.cooffee.myapplication.thirdpartlogin.qq.service
 * 类功能：
 * 创建人：cooffee
 * 创建时间：2016 16/7/12 下午2:26
 * 联系邮箱: wjnovember@icloud.com
 */
public class LoginService extends Service {

    // 登录动作
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    // 注销动作
    public static final String ACTION_LOGOUT = "ACTION_LOGOUT";

    // Tencent对象
    private static Tencent mTencent;

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
        // 获取动作
        String action = intent.getAction();
        // 获取app_id
        String appId = getString(R.string.qq_app_id);
        // 如果Tencent没有实例化
        if (mTencent == null) {
            // 实例化Tencent
            mTencent = Tencent.createInstance(appId, getApplicationContext());
        }
        if (ACTION_LOGIN.equals(action)) { // 如果是登录动作
            // 如果没有在登录状态
            if (!mTencent.isSessionValid()) {
                // 登录操作
                mTencent.login(activity, "application", new BaseUIListener(getBaseContext(), ACTION_LOGIN));
            }
        } else if (ACTION_LOGOUT.equals(action)) { // 如果是注销动作

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
