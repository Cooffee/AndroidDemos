package com.cooffee.myapplication.breakdown.pojo;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.pojo
 * 类功能：下载文件的线程的信息
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 上午10:55
 * 联系邮箱: wjnovember@icloud.com
 */
public class ThreadInfo {
    // 线程ID
    private int id;
    // 下载链接
    private String url;
    // 文件的开始下载位置
    private int start;
    // 文件的结束下载位置
    private int end;
    // 文件已下载的长度
    private int finished;

    public ThreadInfo() {

    }

    public ThreadInfo(int id, String url, int start, int end, int finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        String info = super.toString() + ":[id: " + id + "; url: " + url + "; start:" + start + ";" +
                " end: " + end + "; finished: " + finished + "]";
        return info;
    }
}
