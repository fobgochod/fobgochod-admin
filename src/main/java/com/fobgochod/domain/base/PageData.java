package com.fobgochod.domain.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果
 *
 * @author zhouxiao
 * @date 2021/1/20
 */
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private List<T> list;

    public PageData() {
        this.list = new ArrayList<>();
    }

    public static <T> PageData<T> zero() {
        return new PageData<>();
    }

    public static <T> PageData<T> data(long total, List<T> list) {
        PageData<T> pageData = new PageData<>();
        pageData.setTotal(total);
        pageData.setList(list);
        return pageData;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
