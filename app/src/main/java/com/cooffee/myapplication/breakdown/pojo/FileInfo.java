package com.cooffee.myapplication.breakdown.pojo;

import java.io.Serializable;

/**
 * 项目名称：UsefulDemos
 * 模块名称：断点续传
 * 包名：com.cooffee.myapplication.breakdown.bean
 * 类功能：下载的文件的信息
 * 创建人：cooffee
 * 创建时间：2016 16/7/7 上午10:47
 * 联系邮箱: wjnovember@icloud.com
 */
public class FileInfo implements Serializable {

    // 文件id
    private int id;
    // 文件下载的链接
    private String url;
    // 文件名
    private String fileName;
    // 文件大小（长度）
    private int length;
    // 文件下载完多少
    private int finished;

    public FileInfo() {

    }

    public FileInfo(int id, String url, String fileName, int length, int finished) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        String info = super.toString() + ":[id: " + id + ", url: " + url + "; fileName: " + fileName +
                "; length: " + length + "; finished: " + finished + "]";
        return info;
    }
}
