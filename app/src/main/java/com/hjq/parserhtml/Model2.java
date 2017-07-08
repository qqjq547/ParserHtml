package com.hjq.parserhtml;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class Model2 {
    private int arrarId;
    private int count;
    private int downNum;
    private List<String> urlArr;

    public Model2(int arrarId, int count, int downNum, List<String> urlArr) {
        this.arrarId = arrarId;
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
