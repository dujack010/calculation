package com.allin.recommend.service;

import com.allin.recommend.admin.HBaseAdmin;
import com.allin.recommend.pojo.BatchBean;
import com.allin.recommend.pojo.SingleBean;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.stereotype.Service;

/**
 * Created by DuJunchen on 2017/4/14.
 */
@Service
public class HBaseService extends HBaseAdmin {

    public void put(SingleBean bean) {
        super.put(bean);
    }

    public Result get(SingleBean bean) {
        return super.get(bean);
    }

    public ResultScanner scan(SingleBean bean){
        return super.scan(bean);
    }

    public void delete(SingleBean bean){
        super.delete(bean);
    }

    public void batchPut(BatchBean bean){
        super.batchPut(bean);
    }
}
