package com.fobgochod.service.business;

/**
 * @author chenzwd
 */
public interface FileTagService {

    /**
     * 添加标签
     *
     * @param fileInfoId 文件id
     * @param tag        标签
     */
    void addTag(String fileInfoId, String tag);

    /**
     * 删除所有标签
     *
     * @param fileInfoId 文件id
     */
    void deleteTag(String fileInfoId);

    /**
     * 删除某个标签
     *
     * @param fileInfoId 文件id
     * @param tag        标签
     */
    void deleteTag(String fileInfoId, String tag);

    /**
     * 替换标签
     *
     * @param fileInfoId 文件id
     * @param tag        标签
     */
    void replaceTag(String fileInfoId, String tag);

    /**
     * 获取标签
     *
     * @param fileInfoId 文件id
     * @return 返回标签
     */
    String getTag(String fileInfoId);
}

