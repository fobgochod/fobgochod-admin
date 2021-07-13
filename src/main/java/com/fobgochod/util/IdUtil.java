package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * ID转换
 *
 * @author seven
 * @date 2021/2/27
 */
public class IdUtil {

    public static final List<String> ROOT_DIR = new ArrayList<String>() {{
        add(null);
        add("null");
        add(BaseField.ROOT_DIR);
    }};

    public static ObjectId getObjectId(String objectId) {
        return new ObjectId(objectId);
    }

    public static String getDirId(String dirId) {
        if (ROOT_DIR.contains(dirId)) {
            return BaseField.ROOT_DIR;
        }
        return dirId;
    }

    /**
     * 是否为根目录
     *
     * @param dirId 目录ID
     * @return boolean
     */
    public static boolean isRootDir(String dirId) {
        return ROOT_DIR.contains(dirId);
    }
}
