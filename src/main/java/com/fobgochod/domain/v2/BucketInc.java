package com.fobgochod.domain.v2;

/**
 * Bucket初始化
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
public class BucketInc {

    private String name;
    private Long prev;
    private Double prevGb;
    private String prevReadable;
    private Long next;
    private Double nextGb;
    private String nextReadable;
    private String diffSign;
    private Long diff;
    private String diffReadable;

    public BucketInc() {
        this.prev = 0L;
        this.next = 0L;
        this.diff = 0L;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrev() {
        return prev;
    }

    public void setPrev(Long prev) {
        this.prev = prev;
    }

    public Double getPrevGb() {
        return prevGb;
    }

    public void setPrevGb(Double prevGb) {
        this.prevGb = prevGb;
    }

    public String getPrevReadable() {
        return prevReadable;
    }

    public void setPrevReadable(String prevReadable) {
        this.prevReadable = prevReadable;
    }

    public Long getNext() {
        return next;
    }

    public void setNext(Long next) {
        this.next = next;
    }

    public Double getNextGb() {
        return nextGb;
    }

    public void setNextGb(Double nextGb) {
        this.nextGb = nextGb;
    }

    public String getNextReadable() {
        return nextReadable;
    }

    public void setNextReadable(String nextReadable) {
        this.nextReadable = nextReadable;
    }

    public String getDiffSign() {
        return diffSign;
    }

    public void setDiffSign(String diffSign) {
        this.diffSign = diffSign;
    }

    public Long getDiff() {
        return diff;
    }

    public void setDiff(Long diff) {
        this.diff = diff;
    }

    public String getDiffReadable() {
        return diffReadable;
    }

    public void setDiffReadable(String diffReadable) {
        this.diffReadable = diffReadable;
    }

}
