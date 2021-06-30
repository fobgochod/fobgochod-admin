package com.fobgochod.domain.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件层次
 *
 * @author zhouxiao
 * @date 2021/2/24
 */
public class FileTree {

    private String fileId;
    private String fileName;
    private String filePath;
    private boolean directory;
    private List<FileTree> fileTrees;

    public FileTree() {
        this.fileTrees = new ArrayList<>();
    }

    public FileTree(String fileId, String fileName) {
        this();
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public FileTree(String fileId, String fileName, boolean directory) {
        this(fileId, fileName);
        this.directory = directory;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public List<FileTree> getFiles() {
        return fileTrees;
    }

    public void setFiles(List<FileTree> fileTrees) {
        this.fileTrees = fileTrees;
    }
}
