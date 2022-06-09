package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.Filter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

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
        Query query = SqlUtil.cond(page.getFilter());
        query.with(SqlUtil.sort(page.getOrders()));
        return query.skip(page.skip()).limit(page.limit());
    }

    public static Query cond(Filter<?> filter) {
        Query query = new Query();
        try {
            List<Field> fields = SqlUtil.getFields(filter.getEq());

            for (Field field : fields) {
                field.setAccessible(true);
                // 处理equal
                Object filterEq = filter.getEq();
                if (filterEq != null) {
                    Object value = field.get(filterEq);
                    if (value != null) {
                        query.addCriteria(Criteria.where(field.getName()).is(value));
                    }
                }
                // 处理like
                Object filterLike = filter.getLike();
                if (filterLike != null) {
                    Object value = field.get(filterLike);
                    if (value != null) {
                        query.addCriteria(Criteria.where(field.getName()).regex(String.format(LIKE, value)));
                    }
                }
                field.setAccessible(false);
            }

            // 处理Between
            Filter.Between between = filter.getBetween();
            if (between != null) {
                query.addCriteria(Criteria.where(between.getKey()).gte(between.getBegin()).lte(between.getEnd()));
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
    public static List<Field> getFields(Object object) {
        List<Field> fields = new ArrayList<>();
        if (object != null) {
            Class<?> clazz = object.getClass();
            //最顶层的查询条件实体不获取.
            while (clazz != null) {
                Field[] declaredFields = clazz.getDeclaredFields();
                fields.addAll(Arrays.stream(declaredFields).filter(o -> !o.isSynthetic()).collect(Collectors.toList()));
                //得到父类,然后赋给自己
                clazz = clazz.getSuperclass();
            }
        }
        return fields;
    }
}
