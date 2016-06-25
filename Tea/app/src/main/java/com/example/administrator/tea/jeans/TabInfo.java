package com.example.administrator.tea.jeans;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class TabInfo {
    private String name;
    private int id;

    public TabInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
