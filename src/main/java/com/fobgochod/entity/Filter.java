package com.fobgochod.entity;

import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件
 *
 * @author Xiao
 * @date 2022/3/30 23:12
 */
public class Filter {

    @Transient
    private Like like;
    @Transient
    private Between between;
    @Transient
    private List<Like> likes;
    @Transient
    private List<Between> betweens;

    public Filter() {
        this.likes = new ArrayList<>();
        this.betweens = new ArrayList<>();
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }

    public Between getBetween() {
        return between;
    }

    public void setBetween(Between between) {
        this.between = between;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Between> getBetweens() {
        return betweens;
    }

    public void setBetweens(List<Between> betweens) {
        this.betweens = betweens;
    }

    public static class Like {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
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
