package com.fobgochod.constant;

/**
 * 常用字段
 *
 * @author zhouxiao
 * @date 2020/11/18
 */
public interface BaseField {

    String ROOT_DIR = "0";

    String PID = "id";
    String ID = "_id";
    String CREATE_DATE = "createDate";

    String FILE_INFO_ID = "fileInfoId";
    String FILE_NAME = "fileName";
    String DIRECTORY_ID = "directoryId";
    String FILE_ID = "fileId";
    String CONTENT_TYPE = "contentType";

    /**
     * 父目录的字段名称
     */
    String DIR_PARENT_ID = "parentId";
    /**
     * 目录的名称字段
     */
    String DIR_NAME = "name";
    String DUPLICATE = " - 副本";
    String DUPLICATE_NAME = "%s - 副本";
}
