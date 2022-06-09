package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.Filter;
import com.mongodb.client.model.Filters;
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
import java.util.List;
import java.util.Map;

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
        for (Field field : SqlUtil.getFields(object)) {
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
            for (Field field : SqlUtil.getFields(object)) {
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

    public static Bson filter(Filter<?> filter) {
        List<Bson> filters = new ArrayList<>();
        try {
            filters.add(Filters.or(Filters.eq("deleted", false), Filters.eq("deleted", null)));

            List<Field> fields = SqlUtil.getFields(filter.getEq());
            for (Field field : fields) {
                field.setAccessible(true);
                // 处理equal
                Object filterEq = filter.getEq();
                if (filterEq != null) {
                    Object value = field.get(filter.getEq());
                    if (value != null) {
                        if (BaseField.PID.equals(field.getName())) {
                            filters.add(Filters.eq(BaseField.ID, value));
                        } else {
                            filters.add(Filters.eq(field.getName(), value));
                        }
                    }
                }
                // 处理like
                Object filterLike = filter.getLike();
                if (filterLike != null) {
                    Object value = field.get(filterLike);
                    if (value != null) {
                        filters.add(Filters.regex(field.getName(), String.format(LIKE, value)));
                    }
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException ignored) {
        }
        return Filters.and(filters);

    }

    public static Query query(Page<?> page) {
        if (page == null) {
            return new Query();
        }
        Query query = query(page.getFilter());
        query.with(sort(page.getOrders()));
        return query.skip(page.skip()).limit(page.limit());
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
