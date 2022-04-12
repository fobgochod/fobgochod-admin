package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.Filter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询条件处理
 *
 * @author zhouxiao
 * @date 2021/1/8
 */
public class SqlUtil {

    private static final String LIKE = "^.*%s.*$";

    public static Query cond(Page<?> page) {
        Query query = SqlUtil.cond(page.getCond());
        query.with(SqlUtil.sort(page.getOrders()));
        return query.skip(page.skip()).limit(page.limit());
    }

    public static Query cond(Object cond) {
        Query query = new Query();
        try {
            // 处理equal
            for (Field field : SqlUtil.getFields(cond)) {
                field.setAccessible(true);
                Object value = field.get(cond);
                if (value != null) {
                    query.addCriteria(Criteria.where(field.getName()).is(value));
                }
                field.setAccessible(false);
            }

            if (cond instanceof Filter) {
                Filter filter = (Filter) cond;
                // 处理like
                Filter.Like like = filter.getLike();
                if (like != null && StringUtils.hasLength(like.getValue())) {
                    query.addCriteria(Criteria.where(like.getKey()).regex(String.format(LIKE, like.getValue())));
                }
                for (Filter.Like item : filter.getLikes()) {
                    if (StringUtils.hasLength(item.getValue())) {
                        query.addCriteria(Criteria.where(item.getKey()).regex(String.format(LIKE, item.getValue())));
                    }
                }

                // 处理Between
                Filter.Between between = filter.getBetween();
                if (between != null) {
                    query.addCriteria(Criteria.where(between.getKey()).gte(between.getBegin()).lte(between.getEnd()));
                }
                for (Filter.Between item : filter.getBetweens()) {
                    query.addCriteria(Criteria.where(item.getKey()).gte(item.getBegin()).lte(item.getEnd()));
                }
            }
        } catch (IllegalAccessException ignored) {
        }
        return query;
    }

    public static Sort sort(Map<String, Object> order) {
        ArrayList<Sort.Order> sort = new ArrayList<>();
        if (CollectionUtils.isEmpty(order)) {
            return Sort.unsorted();
        }
        for (Map.Entry<String, Object> entry : order.entrySet()) {
            if (BaseField.PID.equals(entry.getKey())) {
                if (Objects.equals(entry.getValue(), 1)) {
                    sort.add(Sort.Order.asc(BaseField.ID));
                } else {
                    sort.add(Sort.Order.desc(BaseField.ID));
                }
            } else {
                if (Objects.equals(entry.getValue(), 1)) {
                    sort.add(Sort.Order.asc(entry.getKey()));
                } else {
                    sort.add(Sort.Order.desc(entry.getKey()));
                }
            }
        }
        return Sort.by(sort);
    }

    /**
     * 获取对象所有字段
     *
     * @param object 对象
     * @return 字段集合
     */
    private static List<Field> getFields(Object object) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = object.getClass();
        //最顶层的查询条件实体不获取.
        while (clazz != Filter.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            fields.addAll(Arrays.stream(declaredFields).filter(o -> !o.isSynthetic()).collect(Collectors.toList()));
            //得到父类,然后赋给自己
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
