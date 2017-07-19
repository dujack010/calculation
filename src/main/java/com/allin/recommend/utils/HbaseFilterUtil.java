package com.allin.recommend.utils;

import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuJunchen on 2017/7/6.
 */
public class HbaseFilterUtil {
    /**
     * 筛选出匹配的所有的行
     */
    public static RowFilter getRowFilter(String row){
        return new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(row)));
    }

    public static SingleColumnValueFilter getSingleColumnValueFilter(byte[] family,byte[] column,byte[] value){
        return new SingleColumnValueFilter(family,column, CompareFilter.CompareOp.EQUAL,value);
    }

    public static KeyOnlyFilter getKeyOnlyFilter(){
        return new KeyOnlyFilter();
    }

    public static FilterList getFilterList(Filter... args){
        List<Filter> list = new ArrayList<>();
        for (Filter arg : args) {
            list.add(arg);
        }
        return new FilterList(list);
    }
}
