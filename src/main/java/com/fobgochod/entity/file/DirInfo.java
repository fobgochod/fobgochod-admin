package com.fobgochod.entity.file;

import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

/**
 * 文件目录
 *
 * @author zhouxiao
 * @date 2020/11/25
 */
@Document("DirInfo")
public class DirInfo extends BaseEntity {

    /**
     * 上级目录
     */
    private String parentId;
    /**
     * 目录名称，不区分大小写
     */
    private String name;

    /**
     * 判断2个目录是否相等
     *
     * @param source 目录1
     * @param target 目录2
     * @return 相等返回true
     */
    public static boolean equal(String source, String target) {
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
            return true;
        }
        if (BaseField.ROOT_DIR.equals(source) && "0".equals(target)) {
            return true;
        }
        if (BaseField.ROOT_DIR.equals(target) && "0".equals(source)) {
            return true;
        }
        return source.equals(target);
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DirInfo clone() {
        try {
            return (DirInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
