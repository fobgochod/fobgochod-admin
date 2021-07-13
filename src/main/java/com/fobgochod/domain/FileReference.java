package com.fobgochod.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author zhouxiao
 * @date 2021/7/13
 */
public class FileReference {

    private int count;
    private List<String> buckets = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<String> buckets) {
        this.buckets = buckets;
    }
}
