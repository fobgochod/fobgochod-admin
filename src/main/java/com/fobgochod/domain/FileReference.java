package com.fobgochod.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzwd
 * @date : 2018-07-06 17:10
 * @Description:
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
