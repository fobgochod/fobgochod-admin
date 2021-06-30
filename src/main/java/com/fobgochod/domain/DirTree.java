package com.fobgochod.domain;

import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件目录结构
 *
 * @author zhouxiao
 * @date 2021/2/26
 */
public class DirTree extends DirInfo {

    private List<FileInfo> files;
    private List<DirTree> dirs;

    public DirTree() {
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
    }

    public DirTree(DirInfo dirInfo) {
        this();
        BeanUtils.copyProperties(dirInfo, this);
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public List<DirTree> getDirs() {
        return dirs;
    }

    public void setDirs(List<DirTree> dirs) {
        this.dirs = dirs;
    }
}
