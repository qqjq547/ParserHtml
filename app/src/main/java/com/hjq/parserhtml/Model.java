package com.hjq.parserhtml;

/**
 * Created by Administrator on 2017/7/6.
 */

public class Model {
    private int arrarId;
    private int count;
    private int downNum;

    public Model(int arrarId, int count, int downNum) {
        this.arrarId = arrarId;
        this.count = count;
        this.downNum = downNum;
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

}
