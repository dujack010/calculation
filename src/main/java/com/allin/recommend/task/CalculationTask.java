package com.allin.recommend.task;

import com.allin.recommend.admin.BaseHbaseAdminAction;
import com.allin.recommend.pojo.SingleBean;
import com.allin.recommend.service.HBaseService;
import com.allin.recommend.utils.RedisUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.*;

/**
 * Created by DuJunchen on 2017/7/17.
 */
@Component
public class CalculationTask extends BaseHbaseAdminAction {
    private static final String liveKey = "recommend:customer:activated";

    @Autowired
    HBaseService service;

    @Autowired
    SingleBean singleBean;

    @Autowired
    RedisUtil redisUtil;

    @Scheduled(cron = "0 0/15 * * * ?")
    public void recommendResource() throws InterruptedException {
        System.out.println("开始执行");
        Jedis jedis = redisUtil.getPool().getResource();
        Pipeline pipelined = jedis.pipelined();
        //Set<String> users = jedis.smembers(liveKey);
        Long scard = jedis.scard(liveKey);
        System.out.println(scard);
        Set<String> smembers = jedis.smembers(liveKey);
        calculation(smembers);
        System.out.println("计算完成");
        for (String userId : smembers) {
            pipelined.srem(liveKey,userId);
        }
        pipelined.sync();
        jedis.close();
    }

    private void calculation(Set<String> idList) throws InterruptedException {
        System.out.println("进入方法");
        Scan scan = new Scan();
        singleBean.setTableName(itemProfile);
        singleBean.setScan(scan);
        //scan获取所有resourceId
        ResultScanner results = service.scan(singleBean);
        ArrayList<Result> itemProfileResult = new ArrayList<>();
        for (Result result : results) {
            itemProfileResult.add(result);
        }
        for (String customerId : idList) {
            long start = System.currentTimeMillis();
            Get get = new Get(Bytes.toBytes(customerId));
            singleBean.setTableName(userProfile);
            singleBean.setGet(get);
            Result userProfile = service.get(singleBean);
            NavigableMap<byte[], byte[]> userProfileFamilyMap = userProfile.getFamilyMap(Bytes.toBytes(profileFamily));
            if(userProfileFamilyMap==null){
                continue;
            }
            Collection<byte[]> userProfileValues = userProfileFamilyMap.values();
            Set<Map.Entry<byte[], byte[]>> userSet = userProfileFamilyMap.entrySet();
            Put put = new Put(Bytes.toBytes(customerId));
            int counter = 0;
            for (Result result : itemProfileResult) {
                String resourceId = Bytes.toString(result.getRow());
                double topSum = 0;
                double bottom1 = 0;
                double bottom2 = 0;
                Set<Map.Entry<byte[], byte[]>> itemProfileRowEntry = result.getFamilyMap(Bytes.toBytes(profileFamily)).entrySet();
                for (Map.Entry<byte[], byte[]> entry : itemProfileRowEntry) {
                    int ia = getIntSafe(entry.getValue());
                    bottom2 += Math.pow(ia,2);
                    double ua = getDoubleSafe(userProfileFamilyMap.get(entry.getKey()));
                    topSum = topSum + (ua * ia);
                }
                for (byte[] userProfileValue : userProfileValues) {
                    double ua = Bytes.toDouble(userProfileValue);
                    bottom1 += Math.pow(ua,2);
                }
                /*for (Map.Entry<byte[], byte[]> entry : userSet) {
                    //String tagId = Bytes.toString(entry.getKey());
                    double ua = Bytes.toDouble(entry.getValue());
                    //byte[] value = result.getValue(Bytes.toBytes(profileFamily), Bytes.toBytes(tagId));
                    //int ia = getIntSafe(value);
                    //topSum = topSum + (ua * ia);
                    bottom1 += Math.pow(ua,2);
                    //bottom2 += Math.pow(ia,2);
                }*/
                double itemScore = topSum / (Math.sqrt(bottom1) * Math.sqrt(bottom2));
                if(!Double.isNaN(itemScore)){
                    String familyName = getFamilyName(resourceId);
                    if(familyName.length()>1){
                        put.addColumn(Bytes.toBytes(familyName),Bytes.toBytes(resourceId),Bytes.toBytes(itemScore));
                        counter++;
                    }
                }
            }
            if(counter>0){
                singleBean.setTableName(recommendList);
                singleBean.setPut(put);
                service.put(singleBean);
                System.out.println("用户"+customerId+"保存结果");
            }
            long end = System.currentTimeMillis();
            System.out.println("用户"+customerId+"处理用时"+(end-start));
        }
    }

    private String getFamilyName(String resourceId) {
        String[] split = resourceId.split("_");
        int type = Integer.parseInt(split[0]);
        switch (type){
            case 1: return "video";
            case 2: return "doc";
            case 7: return "case";
            default: return "";
        }
    }

    private int getIntSafe(byte[] value) {
        if(value==null){
            return 0;
        }else{
            return Bytes.toInt(value);
        }
    }

    private double getDoubleSafe(byte[] value) {
        if(value==null){
            return 0;
        }else{
            return Bytes.toDouble(value);
        }
    }
}
