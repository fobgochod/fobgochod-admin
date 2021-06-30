package com.fobgochod.constant;

/**
 * 常量命名参考 https://docs.mongodb.com/manual/reference/operator/update
 *
 * @author zhouxiao
 * @date 2020/11/17
 */
public interface UpdateOp {

    String CURRENT_DATE = "$currentDate";
    String INC = "$inc";
    String MIN = "$min";
    String MAX = "$max";
    String MUL = "$mul";
    String RENAME = "$rename";
    String SET = "$set";
    String SET_ON_INSERT = "$setOnInsert";
    String UNSET = "$unset";
}
