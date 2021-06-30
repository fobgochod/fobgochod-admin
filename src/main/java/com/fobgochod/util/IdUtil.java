package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ID转换
 *
 * @author seven
 * @date 2021/2/27
 */
public class IdUtil {

    public static final List<String> ROOT_DIR = new ArrayList<String>() {{
        add("0");
        add("null");
        add(BaseField.EMPTY_UUID_STR);
    }};

    public static ObjectId getObjectId(String objectId) {
        return new ObjectId(objectId);
    }

    public static String getObjectId(ObjectId objectId) {
        return objectId.toString();
    }

    public static UUID getUuid(String uuid) {
        return UUID.fromString(uuid);
    }

    public static String getUuid(UUID uuid) {
        return uuid.toString();
    }

    public static UUID getDirId(String dirId) {
        if (StringUtils.isEmpty(dirId) || "0".equals(dirId)) {
            return BaseField.EMPTY_UUID;
        }
        return UUID.fromString(dirId);
    }

    public static UUID getDirId(UUID dirId) {
        if (dirId == null) {
            dirId = BaseField.EMPTY_UUID;
        }
        return dirId;
    }

    public static String getDirectoryId(String dirId) {
        if (StringUtils.isEmpty(dirId) || "0".equals(dirId)) {
            return BaseField.EMPTY_UUID_STR;
        }
        return dirId;
    }

    public static String getDirectoryId(UUID dirId) {
        if (dirId == null) {
            dirId = BaseField.EMPTY_UUID;
        }
        return dirId.toString();
    }

    /**
     * 是否为根目录
     *
     * @param dirId 目录ID
     * @return boolean
     */
    public static boolean isRootDir(String dirId) {
        return dirId == null
                || "".equals(dirId)
                || "0".equals(dirId)
                || BaseField.EMPTY_UUID_STR.equals(dirId);
    }

    /**
     * 是否为根目录
     *
     * @param dirId 目录ID
     * @return boolean
     */
    public static boolean isRootDir(UUID dirId) {
        return dirId == null || BaseField.EMPTY_UUID.equals(dirId);
    }
}
