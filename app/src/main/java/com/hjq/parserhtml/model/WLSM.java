package com.hjq.parserhtml.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class WLSM {
    private int arrarId;
    private String title;
    private String time;
    private int page;
    private List<String> urlArr;

    public WLSM(int arrarId, String title, String time, int page, List<String> urlArr) {
        this.arrarId = arrarId;
        this.title = title;
        this.time = time;
        this.page = page;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getUrlArr() {
        return urlArr;
    }

    public void setUrlArr(List<String> urlArr) {
        this.urlArr = urlArr;
    }
}
