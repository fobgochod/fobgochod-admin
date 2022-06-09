package com.fobgochod.entity;

import java.time.LocalDateTime;

/**
 * 查询条件
 *
 * @author Xiao
 * @date 2022/3/30 23:12
 */
public class Filter<T> {

    private T eq;
    private T like;
    private Between between;

    public Filter() {
    }

    public T getEq() {
        return eq;
    }

    public void setEq(T eq) {
        this.eq = eq;
    }

    public T getLike() {
        return like;
    }

    public void setLike(T like) {
        this.like = like;
    }

    public Between getBetween() {
        return between;
    }

    public void setBetween(Between between) {
        this.between = between;
    }

    public static class Between {
        private String key;
        private LocalDateTime begin;
        private LocalDateTime end;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public LocalDateTime getBegin() {
            return begin;
        }

        public void setBegin(LocalDateTime begin) {
            this.begin = begin;
        }

        public LocalDateTime getEnd() {
            return end;
        }

        public void setEnd(LocalDateTime end) {
            this.end = end;
        }
    }
}
