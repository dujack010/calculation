package com.allin.recommend.pojo;

import org.apache.hadoop.hbase.client.Put;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DuJunchen on 2017/6/19.
 * 用于批量操作的bean
 */
@Component
@Scope("prototype")
public class BatchBean extends BaseBean {
    private List<Put> putList;

    public BatchBean() {
    }

    public BatchBean(String tableName) {
        super(tableName);
    }

    public void setPutList(List<Put> putList) {
        this.putList = putList;
    }

    public List<Put> getPutList() {
        return putList;
    }
}
