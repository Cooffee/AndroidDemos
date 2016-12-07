package com.cooffee.myapplication.commonadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cooffee.myapplication.commonadapter.utils.ViewHolder;

import java.util.List;

/**
 * 项目名称：UsefulDemos
 * 模块名称：通用适配器
 * 包名：com.cooffee.myapplication.commonadapter
 * 类功能：适配器，抽象类，用于将数据显示到ListView中
 * 创建人：cooffee
 * 创建时间：2016 16/7/6 下午1:20
 * 联系邮箱: wjnovember@icloud.com
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    // 上下文声明
    protected Context mContext;
    // 数据声明
    protected List<T> mDatas;
    // 布局填充器声明
    protected LayoutInflater mInflater;
    // 布局ID声明
    private int mLayoutId;

    /**
     * 有构造方法
     * @param context 上下文
     * @param datas 数据
     * @param layoutId 布局ID
     */
    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        // 上下文赋值
        this.mContext = context;
        // 数据赋值
        this.mDatas = datas;
        // 布局ID赋值
        this.mLayoutId = layoutId;
        // 通过上下文获取布局填充器
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 获取数据的数量
     * @return
     */
    @Override
    public int getCount() {
        // 返回数据的数量
        return mDatas.size();
    }

    /**
     * 获取数据列表的某一条数据
     * @param position 指定某一个位置的数据
     * @return 单条数据(泛型)
     */
    @Override
    public T getItem(int position) {
        // 返回某一条数据
        return mDatas.get(position);
    }

    /**
     * 获取某一条数据的id
     * @param position 数据的位置
     * @return
     */
    @Override
    public long getItemId(int position) {
        // 返回数据的位置
        return position;
    }

    /**
     * ListView的某一项进入屏幕窗口时调用
     * @param position 进入窗口的子项在ListView中的位置
     * @param convertView 子项的视图布局
     * @param parent 父项，即ListView
     * @return 返回子项的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 声明并实例化视图容器
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
        // 将数据转化为视图
        convert(holder, getItem(position));
        // 返回子项的视图
        return holder.getConvertView();
    }

    /**
     * 抽象方法：
     * 将数据转化为视图
     * @param holder 视图容器
     * @param t 泛型
     */
    public abstract void convert(ViewHolder holder, T t);
}
