package com.allin.recommend.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;

/**
 * Created by DuJunchen on 2017/6/1.
 */
@Component
public class RedisUtil {
    private String pwd = "u8aScgd";
    //private String host = "192.168.1.126";
    private String host = "10.51.113.36";
    //private String pwd = "admin";
    //private int port = 6379;
    private int port = 11818;

    //单点
    private Jedis jedis;
    //连接池
    private JedisPool pool;

    public RedisUtil() {
        JedisPoolConfig config = new JedisPoolConfig();
        pool = new JedisPool(config,host,port,2000,pwd,0);
    }

    public JedisPool getPool() {
        return pool;
    }

    @PreDestroy
    public void cleanUp(){
        this.pool.close();
        System.out.println("redis连接池关闭");
    }
}