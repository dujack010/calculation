package com.allin.recommend.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by DuJunchen on 2017/4/12.
 */
@Component
public class HBaseUtil {
    private Configuration conf;
    private Connection con;

    @PreDestroy
    public void cleanUp(){
        try {
            if(this.con!=null){
                this.con.close();
            }
            System.out.println("连接关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConf(){
        if(conf==null){
            this.conf = HBaseConfiguration.create();
        }
        return this.conf;
    }

    public Connection getConnection(){
        if(con==null){
            try {
                System.out.println("创建连接");
                this.con = ConnectionFactory.createConnection(getConf());
            } catch (IOException e) {
                this.con = null;
            }
        }
        return this.con;
    }

    public void closeConnection(){
        if(con!=null){
            try {
                this.con.close();
                System.out.println("连接关闭");
                this.con=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
