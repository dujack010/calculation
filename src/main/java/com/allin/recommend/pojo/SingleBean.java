package com.allin.recommend.pojo;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by DuJunchen on 2017/4/13.
 */
@Component
@Scope("prototype")
public class SingleBean extends BaseBean {
    private Put put;
    private Get get;
    private Scan scan;
    private Delete del;

    public SingleBean(){
    }

    public SingleBean(String tableName) {
        super(tableName);
    }


    public Delete getDel() {
        return del;
    }

    public SingleBean setDel(Delete del) {
        this.del = del;
        return this;
    }

    public Scan getScan() {
        return scan;
    }

    public SingleBean setScan(Scan scan) {
        this.scan = scan;
        return this;
    }

    public Get getGet() {
        return get;
    }

    public SingleBean setGet(Get get) {
        this.get = get;
        return this;
    }

    public SingleBean setPut(Put put) {
        this.put = put;
        return this;
    }


    public Put getPut() {
        return put;
    }
}
