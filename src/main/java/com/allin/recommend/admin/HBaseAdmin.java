package com.allin.recommend.admin;

import com.allin.recommend.pojo.BatchBean;
import com.allin.recommend.pojo.SingleBean;
import com.allin.recommend.utils.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by DuJunchen on 2017/4/13.
 */
public class HBaseAdmin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HBaseUtil util;

    /**
     * 批量写入
     * @param bean
     */
    public void batchPut(BatchBean bean){
        long start = System.currentTimeMillis();
        List<Put> putList = bean.getPutList();
        Table table = null;
        try {
            table = util.getConnection().getTable(TableName.valueOf(bean.getTableName()));
            table.put(putList);
            long end = System.currentTimeMillis();
            logger.info("HBASE批量用时:"+(end-start));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 单条数据写入
     */
    public void put(SingleBean bean){
        long start = System.currentTimeMillis();
        Put put = bean.getPut();
        HTable table = null;
        try {
            table = (HTable) util.getConnection().getTable(TableName.valueOf(bean.getTableName()));
            table.put(put);
            long end = System.currentTimeMillis();
            logger.info("HBASE put用时:"+(end-start));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 单条获取数据
     * @param bean
     */
    public Result get(SingleBean bean){
        long start = System.currentTimeMillis();
        Get get = bean.getGet();
        Table table = null;
        try {
            table = util.getConnection().getTable(TableName.valueOf(bean.getTableName()));
            Result result = table.get(get);
            long end = System.currentTimeMillis();
            logger.info("HBASE GET用时:"+(end-start));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Result();
    }

    /**
     * scan操作
     * @param bean
     * @return
     */
    public ResultScanner scan(SingleBean bean){
        Scan scan = bean.getScan();
        Table table = null;
        try {
            table = util.getConnection().getTable(TableName.valueOf(bean.getTableName()));
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void delete(SingleBean bean){
        Delete del = bean.getDel();
        Table table = null;
        try {
            table = util.getConnection().getTable(TableName.valueOf(bean.getTableName()));
            table.delete(del);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnection() {
        util.closeConnection();
    }
}
