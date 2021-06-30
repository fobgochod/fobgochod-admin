package com.fobgochod.service.business;

import com.fobgochod.entity.file.FileInfo;

/**
 * 分享
 *
 * @author zhouxiao
 * @date 2021/3/12
 */
public interface ShareService {

    /**
     * 永久分享
     *
     * @param fileInfo 文件
     * @return String
     */
    String shareFileForever(FileInfo fileInfo);

    /**
     * 限时分享
     *
     * @param fileInfo 文件
     * @param d        天数
     * @return String
     */
    String shareFileTimeLimit(FileInfo fileInfo, int d);
}
