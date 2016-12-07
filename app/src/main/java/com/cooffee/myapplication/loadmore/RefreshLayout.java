package com.cooffee.myapplication.loadmore;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cooffee.myapplication.R;

/**
 * 项目名称：UsefulDemos
 * 模块名称：上拉加载更多模块
 * 包名：com.cooffee.myapplication.loadmore
 * 类功能：自定义控件，继承下拉刷新控件，当ListView滑动到最底部且还有继续向上滑的趋势的时候，加载更多
 * 创建人：cooffee
 * 创建时间：2016 16/7/6 下午4:17
 * 联系邮箱: wjnovember@icloud.com
 */
public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    // ListView有效滑动长度
    private int mTouchSlop;
    // ListView控件声明
    private ListView mListView;
    // 加载监听事件声明
    private OnLoadListener mOnLoadListener;
    // 布局，用于提示ListView正在加载更多
    private View mFooterView;
    // 按下时Y坐标
    private int mYDown;
    // 抬起来时的Y坐标，通过与mYDonw比较，判断是否处于上拉状态
    private int mYLast;
    // 是否处于加载状态
    private boolean isLoading;

    /**
     * 有参构造方法
     *
     * @param context 上下文
     */
    public RefreshLayout(Context context) {
        // 调用包含两个参数的构造方法
        this(context, null);
    }

    /**
     * 有参构造方法
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public RefreshLayout(Context context, AttributeSet attrs) {
        // 调用父类构造方法
        super(context, attrs);
        // 获取ListView有效滑动的长度，在这个滑动长度以内，ListView不会滑动
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 获取ListView底部View，用于提示正在加载更多数据
        mFooterView = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
    }

    /**
     * 控件生成时调用，用来确定控件的大小
     *
     * @param changed 大小是否改变
     * @param left    左侧坐标
     * @param top     顶部坐标
     * @param right   右侧坐标
     * @param bottom  底部坐标
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 调用父类构造方法
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView
        if (mListView == null) {
            initListView();
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        // 获取刷新布局下子项的数量
        int childs = getChildCount();
        // 刷新布局包含子项
        if (childs > 0) {
            // 获取第一个子项，一般只有一个子项，为ListView
            View childView = getChildAt(0);
            // 如果子项是ListView
            if (childView instanceof ListView) {
                // 获取ListView
                mListView = (ListView) childView;
                // 设置ListView滚动事件监听
                mListView.setOnScrollListener(this);
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @param event
     * @return
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     * <p/>
     * dispatchTouchEvent的执行顺序为：
     * 首先触发ACTIVITY的dispatchTouchEvent
     * 然后触发ACTIVITY的onUserInteraction
     * 然后触发LAYOUT的dispatchTouchEvent
     * 然后触发LAYOUT的onInterceptTouchEvent
     * <p/>
     * dispatchTouchEvent：
     * 此方法一般用于初步处理事件，因为动作是由此分发，所以通常会调用super.dispatchTouchEvent。
     * 这样就会继续调用onInterceptTouchEvent，再由onInterceptTouchEvent决定事件流向。
     * <p/>
     * onInterceptTouchEvent：
     * 若返回值为True事件会传递到自己的onTouchEvent()；
     * 若返回值为False传递到下一个view的dispatchTouchEvent()；
     * <p/>
     * onTouchEvent()：
     * 若返回值为True，事件由自己处理消耗，后续动作序列让其处理；
     * 若返回值为False，自己不消耗事件了，向上返回让其他的父view的onTouchEvent接受处理；
     * <p/>
     * 虽然还是不太懂，暂且将其认为是触摸监听事件调用的前提，在调用前将触摸事件分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 获取触控事件动作
        final int action = event.getAction();

        // 根据动作的不同，执行不同的效果
        switch (action) {
            case MotionEvent.ACTION_DOWN: // 按下
                mYDown = (int) event.getRawY();
                mYLast = mYDown;
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                mYLast = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP: // 抬起
                // 如果可以加载
                if (canLoad()) {
                    // 加载数据
                    loadData();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 判断ListView是否可以加载
     * 可以加载条件：
     * 1. 当前未处于加载状态
     * 2. ListView在最底部
     * 3. 当前是上拉操作
     *
     * @return
     */
    private boolean canLoad() {
        return !isLoading && isBottom() && isPullUp();
    }

    /**
     * 判断ListView是否滑动到最底部
     *
     * @return
     */
    public boolean isBottom() {
        // 如果ListView没有实例化
        if (mListView == null) {
            // 实例化ListView
            initListView();
        }
        // 获取ListView的适配器
        ListAdapter listAdapter = mListView.getAdapter();
        // 如果ListView存在实例且配置好适配器
        if (mListView != null && listAdapter != null) {
            // 获取ListView最后一个View所在的下标
            int lastIndex = listAdapter.getCount() - 1;
            // 判断可见的最后一个子View的位置和ListView的实际最后一个子View是否一致，来判断ListView是否滑到底部
            return mListView.getLastVisiblePosition() == lastIndex;
        }
        // ListView不存在或未配置适配器或不存在数据
        return false;
    }

    /**
     * 判断ListView是否处于上拉操作状态
     *
     * @return
     */
    public boolean isPullUp() {
        // 滑动的长度
        int ySlide = mYDown - mYLast;
        // 如果滑动距离有效，则处于上拉状态；否则反之
        return ySlide >= mTouchSlop;
    }

    /**
     * 加载数据
     */
    private void loadData() {
        // 如果加载监听事件已经注册
        if (mOnLoadListener != null) {
            // 设置状态为加载状态
            setLoading(true);
            // 调用加载
            mOnLoadListener.onLoad();
        }
    }

    /**
     * 设置加载状态
     *
     * @param loading 是否在加载
     */
    public void setLoading(boolean loading) {
        // 更新加载状态
        isLoading = loading;
        // 根据是否在加载状态执行相应的操作
        if (isLoading) { // 如果处于加载状态
            // 添加底部View
            mListView.addFooterView(mFooterView);
        } else {
            // 移除底部View
            mListView.removeFooterView(mFooterView);
            // 重置按下时Y的坐标
            mYDown = 0;
            // 重置抬起时Y的坐标
            mYLast = 0;
        }
    }

    /**
     * 注册加载时监听事件
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        // 注册加载时监听事件
        mOnLoadListener = loadListener;
    }

    /**
     * 滚动状态改变时候调用
     *
     * @param view        子视图控件
     * @param scrollState 滚动状态
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * 滚动时调用
     *
     * @param view             子视图控件
     * @param firstVisibleItem 子视图中第一个可见的子项的下标
     * @param visibleItemCount 子视图中最后一个可见的子项的下标
     * @param totalItemCount   子视图中所有项总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 如果可以加载
        if (canLoad()) {
            // 加载数据
            loadData();
        }
    }


    /**
     * 加载更多事件监听接口
     */
    public static interface OnLoadListener {
        // 加载时调用的方法
        public void onLoad();
    }
}
