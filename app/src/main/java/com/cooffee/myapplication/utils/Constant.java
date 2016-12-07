package com.cooffee.myapplication.utils;

/**
 * 项目名称：UsefulDemos
 * 模块名称：
 * 包名：com.cooffee.myapplication.utils
 * 类功能：
 * 创建人：cooffee
 * 创建时间：2016 16/7/6 下午2:58
 * 联系邮箱: wjnovember@icloud.com
 */
public class Constant {

    // Log的标记
    public static final String TAG = "Demos";

    // 断点续传
    public static final int RESUME_FROM_BREAK_DOWN = 0x00;
    // 第三方登录
    public static final int THIRD_PART_LOGIN = 0x01;
    // 第三方支付
    public static final int THIRD_PART_PAY = 0x02;
    // mp3 mp4播放
    public static final int MP3_MP4_PLAY = 0x03;
    // markdown文本显示
    public static final int MARK_DOWN = 0x04;
    // 蓝牙连接
    public static final int BLUE_TOOTH = 0x05;
    // 加载word文档
    public static final int LOAD_WORD = 0x06;

    // 意图标签 文件信息
    public static final String FILE_INFO = "FILE_INFO";
    // 意图传值关键字
    public static final String KEY_FINISHED = "finished";

    // SharedPreferences关键字 是否取消
    public static final String KEY_CANCEL = "is_cancel";

    // 类型 整型
    public static final int TYPE_INTEGER = 0x01;
    // 类型 字符串
    public static final int TYPE_STRING = 0x02;
    // 类型 浮点型
    public static final int TYPE_FLOAT = 0x03;
    // 长整型
    public static final int TYPE_LONG = 0x04;
    // 布尔型
    public static final int TYPE_BOOLEAN = 0x05;

    // -toolbar形式
    // 基本形式，无侧滑菜单，不返回上一层activity
    public static final int TOOLBAR_BASE = 0x01;
    // 返回上一层activity
    public static final int TOOLBAR_UP = 0x02;

}
