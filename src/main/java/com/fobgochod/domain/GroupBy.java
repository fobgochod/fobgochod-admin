package com.fobgochod.domain;

/**
 * GroupBy.java
 *
 * @author Xiao
 * @date 2022/2/22 1:33
 */
public class GroupBy {

    private Group id;
    private Integer count;

    public Group getId() {
        return id;
    }

    public void setId(Group id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static class Group {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
