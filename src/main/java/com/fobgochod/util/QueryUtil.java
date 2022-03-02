package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.mongodb.client.model.Filters;
import org.apache.logging.log4j.util.Strings;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询条件处理
 *
 * @author zhouxiao
 * @date 2021/1/8
 */
public class QueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final String LIKE = "^.*%s.*$";

    /**
     * 组装查询条件
     *
     * @param object
     * @return
     */
    public static Query query(Object object) {
        Query query = new Query();
        if (object == null) {
            return query;
        }
        for (Field field : getFields(object)) {
            field.setAccessible(true);
            org.springframework.data.mongodb.core.mapping.Field annotation = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            if (annotation != null) {
                // @Field注解重新定义字段name
                Criteria criteria = getCriteria(annotation.value(), field, object);
                if (criteria != null) {
                    query.addCriteria(criteria);
                }
                continue;
            }
            // 其他没有@Field注解字段
            Criteria criteria = getCriteria(field, object);
            if (criteria != null) {
                query.addCriteria(criteria);
            }
            field.setAccessible(false);
        }
        return query;
    }

    public static Bson filter(Object object) {
        List<Bson> filters = new ArrayList<>();
        if (object != null) {
            for (Field field : getFields(object)) {
                field.setAccessible(true);
                Bson bson = getBson(field, object);
                if (bson != null) {
                    filters.add(bson);
                }
                field.setAccessible(false);
            }
        }
        if (filters.isEmpty()) {
            return new BsonDocument();
        }
        return Filters.and(filters);
    }

    public static Bson filter(Map<String, Object> filter) {
        List<Bson> filters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            if (BaseField.PID.equals(entry.getKey())) {
                filters.add(Filters.eq(BaseField.ID, entry.getValue().toString()));
            } else if (BaseField.DIR_PARENT_ID.equals(entry.getKey())) {
                filters.add(Filters.eq(entry.getKey(), entry.getValue().toString()));
            } else if (entry.getValue() instanceof String) {
                filters.add(Filters.regex(entry.getKey(), String.format(LIKE, entry.getValue())));
            } else {
                filters.add(Filters.eq(entry.getKey(), entry.getValue()));
            }
        }
        return Filters.and(filters);
    }

    public static Query query(Page page) {
        if (page == null) {
            return new Query();
        }
        Query query = query(page.getFilters());
        query.with(sort(page.getOrders()));
        return query.skip(page.skip()).limit(page.limit());
    }

    public static Query query(Map<String, Object> filter) {
        Query query = new Query();
        if (CollectionUtils.isEmpty(filter)) {
            return query;
        }
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            if (BaseField.PID.equals(entry.getKey())) {
                query.addCriteria(Criteria.where(BaseField.ID).is(entry.getValue().toString()));
            } else if (entry.getValue() instanceof String) {
                query.addCriteria(Criteria.where(entry.getKey()).regex(String.format(LIKE, entry.getValue())));
            } else {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
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
                if (entry.getValue().equals(1)) {
                    sort.add(Sort.Order.asc(BaseField.ID));
                } else {
                    sort.add(Sort.Order.desc(BaseField.ID));
                }
            } else {
                if (entry.getValue().equals(1)) {
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
     * @param object
     * @return
     */
    private static List<Field> getFields(Object object) {
        List<Field> fields = new ArrayList<>();
        Class<?> aClass = object.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (aClass != null) {
            Field[] declaredFields = aClass.getDeclaredFields();
            fields.addAll(Arrays.stream(declaredFields).filter(o -> !o.isSynthetic()).collect(Collectors.toList()));
            //得到父类,然后赋给自己
            aClass = aClass.getSuperclass();
        }
        return fields;
    }

    private static Criteria getCriteria(Field field, Object object) {
        return getCriteria(field.getName(), field, object);
    }

    private static Criteria getCriteria(String fieldName, Field field, Object object) {
        try {
            Object fieldValue = field.get(object);
            if (!StringUtils.isEmpty(fieldValue)) {
                if (String.class.getName().equals(field.getGenericType().getTypeName())) {
                    return Criteria.where(fieldName).regex(String.format(LIKE, fieldValue));
                } else {
                    return Criteria.where(fieldName).is(fieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("获取对象字段值错误：{}", e.getMessage());
        }
        return null;
    }

    private static Bson getBson(Field field, Object object) {
        try {
            Object fieldValue = field.get(object);
            if (!StringUtils.isEmpty(fieldValue)) {
                if (BaseField.PID.equals(field.getName())) {
                    return Filters.eq(BaseField.ID, fieldValue);
                } else if (String.class.getName().equals(field.getGenericType().getTypeName())) {
                    return Filters.regex(field.getName(), String.format(LIKE, fieldValue));
                } else {
                    return Filters.eq(field.getName(), fieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("获取对象字段值错误：{}", e.getMessage());
        }
        return null;
    }
}
