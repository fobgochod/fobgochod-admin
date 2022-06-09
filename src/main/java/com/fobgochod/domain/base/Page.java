package com.fobgochod.domain.base;

import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.Filter;
import com.fobgochod.util.QueryUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.Map;

/**
 * 分页信息
 *
 * @author zhouxiao
 * @date 2021/1/20
 */
public class Page<T> {

    public static final Page<?> OK = new Page<>();

    /**
     * 查询条件
     */
    private Filter<T> filter;
    /**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 排序
     */
    private Map<String, Object> orders;
    /**
     * 总数
     */
    private long total;

    public Page() {
        this.filter = new Filter<>();
        this.pageNum = 1;
        this.pageSize = 10;
        this.orders = Collections.singletonMap(BaseField.CREATE_DATE, -1);
    }

    public static Page<?> ok() {
        return OK;
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Object> orders) {
        this.orders = orders;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Query query() {
        return QueryUtil.query(this);
    }

    public Bson filter() {
        return QueryUtil.filter(this.filter);
    }

    public Bson sort() {
        return new Document(this.orders);
    }

    public int skip() {
        return (this.pageNum - 1) * this.pageSize;
    }

    public int limit() {
        return this.pageSize;
    }
}
