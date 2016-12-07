package com.cooffee.myapplication.commonadapter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 项目名称：UsefulDemos
 * 模块名称：通用适配器
 * 包名：com.cooffee.myapplication.commonadapter.utils
 * 类功能：ListView的子View的视图控件容器
 * 创建人：cooffee
 * 创建时间：2016 16/7/6 下午1:24
 * 联系邮箱: wjnovember@icloud.com
 */
public class ViewHolder {

    /**
     * 注：
     * 下面注释中的子View是相对ListView而言的，子View是ListView中的某一项的视图控件
     * 父View指的是ListView
     */

    // 存储view的键值对结构数组 key:控件名称，value: 控件
    private SparseArray<View> mViews;
    // 标记当前的view在listview中的位置
    private int mPosition;
    // listview的子view
    private View mConvertView;

    /**
     * 有参构造方法
     * @param context 上下文
     * @param parent view的父view，这里指listview
     * @param layoutId view的布局id
     * @param position view的位置
     */
    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        // 位置
        this.mPosition = position;
        // 视图键值对数组
        this.mViews = new SparseArray<>();

        // 返回填充后的view，并将其赋值给converview
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        // 将viewholder作为标签添加进convertview
        mConvertView.setTag(this);
    }

    /**
     * 返回ViewHolder
     * @param context 上下文
     * @param convertView 子view
     * @param parent 父view，这里指listView
     * @param layoutId 子view的id
     * @param position 子view在父view中的位置
     * @return ViewHolder 返回viewvholder
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        // 对子view是否为空进行判断
        if (convertView == null) { // 如果子view为空
            // 实例化ViewHolder并返回
            return new ViewHolder(context, parent, layoutId, position);
        } else { // 如果子view不为空
            // 获取之前以标签形式存储在ConvertView中的ViewHolder
            ViewHolder holder = (ViewHolder) convertView.getTag();
            // 子view的位置
            holder.mPosition = position;
            // 返回ViewHolder
            return holder;
        }
    }

    /**
     * 通过控件ID获取控件
     * @param viewId 控件ID
     * @param <T>  泛型
     * @return T 泛型
     */
    public <T extends View> T getView(int viewId) {
        // 从视图键值对数组中获取指定的view
        View view = mViews.get(viewId);

        // 对view 是否为空进行判断
        if (view == null) {
            // 获取子view 里面的控件
            view = mConvertView.findViewById(viewId);
            // 将获取的view控件放入控件数组中
            mViews.put(viewId, view);
        }

        // 返回控件
        return (T)view;
    }

    /**
     * 返回子view控件
     * @return View 视图控件
     */
    public View getConvertView() {
        // 返回子view
        return mConvertView;
    }

    /**
     * 设置id为viewId的控件的文本
     * @return ViewHoler 返回ViewHolder
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        // 通过id获取控件
        TextView tv = getView(viewId);
        // 设置文本
        tv.setText(text);
        // 返回Viewholder
        return this;
    }

    /**
     * 通过图片资源文件设置图片
     * @param viewId 控件的ID
     * @param resId 资源文件的ID
     * @return ViewHolder 返回ViewHolder
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        // 获取图片控件
        ImageView view = getView(viewId);
        // 通过资源id设置图片
        view.setImageResource(resId);
        // 返回ViewHolder
        return this;
    }

    /**
     * 通过Bitmap设置图片
     * @param viewId 控件ID
     * @param bitmap 图片
     * @return ViewHolder 返回ViewHolder
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        // 获取图片控件
        ImageView view = getView(viewId);
        // 通过bitmap设置图片
        view.setImageBitmap(bitmap);
        // 返回ViewHolder
        return this;
    }

    /**
     * 返回子view在父view中的位置
     * @return mPotision 位置
     */
    public int getPosition() {
        // 返回子view的位置
        return mPosition;
    }
}
