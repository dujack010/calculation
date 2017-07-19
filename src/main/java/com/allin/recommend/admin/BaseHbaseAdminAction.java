package com.allin.recommend.admin;

import com.allin.recommend.pojo.SingleBean;
import com.allin.recommend.service.HBaseService;
import com.allin.recommend.utils.HbaseFilterUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

/**
 * Created by DuJunchen on 2017/7/13.
 */
public class BaseHbaseAdminAction {
    protected static final String itemProfile="item_profile";
    protected static final String profileFamily="tag";
    protected static final String userProfile="user_profile";
    protected static final String recommendList = "recommend_list";

    @Autowired
    protected HBaseService service;

    /**
     * 获取某标签相关的所有资源
     * @param tagId
     * @return
     */
    protected List<String> getResourceByTag(String tagId) {
        Scan scan = new Scan();
        KeyOnlyFilter kof = HbaseFilterUtil.getKeyOnlyFilter();
        SingleColumnValueFilter scvf = HbaseFilterUtil.getSingleColumnValueFilter(Bytes.toBytes(profileFamily), Bytes.toBytes(tagId), Bytes.toBytes(1));
        scvf.setFilterIfMissing(Boolean.TRUE);
        scan.setFilter(HbaseFilterUtil.getFilterList(kof, scvf));
        ResultScanner results = service.scan(new SingleBean(itemProfile).setScan(scan));
        List<String> resourceIdsWithTag = new ArrayList<>();
        for (Result result : results) {
            resourceIdsWithTag.add(Bytes.toString(result.getRow()));
        }
        return resourceIdsWithTag;
    }

    /**
     * 计算用户对标签的兴趣值
     * 实时
     * @param resourceByTag 该标签
     * @param scoreMap 用户对资源的打分集合
     * @param avg
     * @return
     */
    protected double getTagScore(List<String> resourceByTag, NavigableMap<byte[], byte[]> scoreMap, double avg) {
        Double sum = 0.0;
        for (String resourceId : resourceByTag) {
            double score = Bytes.toDouble(scoreMap.get(Bytes.toBytes(resourceId)));
            System.out.println("用户对资源"+resourceId+"的打分是"+score);
            double balance = (score - avg);
            System.out.println("减掉平均分后:"+balance);
            sum += balance;
        }
        if(resourceByTag.size()==0){
            return 0.0;
        }
        return sum/resourceByTag.size();
    }

    protected byte[] getRecommendFamily(int type){
        switch (type){
            case 1: return Bytes.toBytes("video");
            case 2: return Bytes.toBytes("doc");
            case 4: return Bytes.toBytes("topic");
            case 7: return Bytes.toBytes("case");
            default: return null;
        }
    }
}
