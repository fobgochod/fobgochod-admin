package com.fobgochod.constant;

/**
 * 常量命名参考 https://docs.mongodb.com/master/reference/operator/query
 *
 * @author zhouxiao
 * @date 2020/11/17
 */
public interface QueryOp {

    /**
     * 等于 =
     */
    String EQ = "$eq";
    /**
     * 不等于 !=、<>
     */
    String NE = "$ne";
    /**
     * 大于 >
     */
    String GT = "$gt";
    /**
     * 大于等于 >=
     */
    String GTE = "$gte";
    /**
     * 小于 <
     */
    String LT = "$lt";
    /**
     * 小于等于 <=
     */
    String LTE = "$lte";
    /**
     * in
     */
    String IN = "$in";
    /**
     * not in
     */
    String NIN = "$nin";

    String AND = "$and";
    String NOT = "$not";
    String NOR = "$nor";
    String OR = "$or";
}
