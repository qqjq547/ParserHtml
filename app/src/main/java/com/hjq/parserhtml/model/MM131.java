package com.hjq.parserhtml.model;

/**
 * Created by Administrator on 2017/7/6.
 */

public class MM131 {
    private int arrarId;
    private String title;
    private String time;
    private int count;
    private int downNum;

    public MM131(int arrarId, String title, int count, int downNum) {
        this.arrarId = arrarId;
        this.title = title;
        this.count = count;
        this.downNum = downNum;
    }

    public int getArrarId() {
        return arrarId;
    }

    public void setArrarId(int arrarId) {
        this.arrarId = arrarId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
