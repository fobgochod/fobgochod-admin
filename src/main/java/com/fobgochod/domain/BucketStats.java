package com.fobgochod.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Bucket初始化
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
public class BucketStats {

    public static List<String> SYSTEM = Arrays.asList("admin", "config", "local");

    private String name;
    private Long size;
    private Double sizeGb;
    private String sizeReadable;
    private Long collections;
    private Long indexes;
    private Long files;
    private List<BucketStats> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Double getSizeGb() {
        return sizeGb;
    }

    public void setSizeGb(Double sizeGb) {
        this.sizeGb = sizeGb;
    }

    public String getSizeReadable() {
        return sizeReadable;
    }

    public void setSizeReadable(String sizeReadable) {
        this.sizeReadable = sizeReadable;
    }

    public Long getCollections() {
        return collections;
    }

    public void setCollections(Long collections) {
        this.collections = collections;
    }

    public Long getIndexes() {
        return indexes;
    }

    public void setIndexes(Long indexes) {
        this.indexes = indexes;
    }

    public Long getFiles() {
        return files;
    }

    public void setFiles(Long files) {
        this.files = files;
    }

    public List<BucketStats> getChildren() {
        return children;
    }

    public void setChildren(List<BucketStats> children) {
        this.children = children;
    }
}
