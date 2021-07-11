package com.fobgochod.domain;

import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录信息
 *
 * @author seven
 * @date 2021/7/11
 */
public class Directory {

    private List<FileInfo> files;
    private List<DirInfo> dirs;

    public Directory() {
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
    }

    public static Directory build(List<DirInfo> dirs, List<FileInfo> files) {
        Directory directory = new Directory();
        if (dirs != null) {
            directory.setDirs(dirs);
        }
        if (files != null) {
            directory.setFiles(files);
        }
        return directory;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public List<DirInfo> getDirs() {
        return dirs;
    }

    public void setDirs(List<DirInfo> dirs) {
        this.dirs = dirs;
    }
}
