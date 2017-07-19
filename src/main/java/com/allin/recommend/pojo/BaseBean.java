package com.allin.recommend.pojo;

/**
 * Created by DuJunchen on 2017/6/19.
 */
public class BaseBean {
    protected String tableName;

    public BaseBean() {
    }

    public BaseBean(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
