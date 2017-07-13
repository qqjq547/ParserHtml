package com.hjq.parserhtml.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class LSM {
    private int arrarId;
    private String title;
    private String thumb;
    private int count;
    private int downNum;
    private List<String> urlArr;

    public LSM(int arrarId, String title, String thumb, int count, int downNum, List<String> urlArr) {
        this.arrarId = arrarId;
        this.title = title;
        this.thumb = thumb;
        this.count = count;
        this.downNum = downNum;
        this.urlArr = urlArr;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public List<String> getUrlArr() {
        return urlArr;
    }

    public void setUrlArr(List<String> urlArr) {
        this.urlArr = urlArr;
    }
}
