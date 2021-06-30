package com.fobgochod.domain;

import com.fobgochod.entity.file.FileInfo;

/**
 * 最近文件
 */
public class RecentFile extends FileInfo {

    private String directoryName;

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

}
