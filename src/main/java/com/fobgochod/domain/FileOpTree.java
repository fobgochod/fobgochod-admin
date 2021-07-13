package com.fobgochod.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作
 *
 * @author zhuzcz
 * @date 2021/3/3
 */
public class FileOpTree {

    private String sourceId;
    private String targetId;
    private boolean success;
    private String message;
    private boolean directory;
    private List<FileOpTree> fileOps;

    public FileOpTree() {
        this.fileOps = new ArrayList<>();
    }

    public FileOpTree(String sourceId, String targetId, boolean success, boolean directory, String message) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.success = success;
        this.directory = directory;
        this.message = message;
    }

    public static FileOpTree fileOpOk(String sourceId, String targetId) {
        FileOpTree fileOpTree = new FileOpTree();
        fileOpTree.setSourceId(sourceId);
        fileOpTree.setTargetId(targetId);
        fileOpTree.setSuccess(true);
        fileOpTree.setMessage("文件复制成功");
        return fileOpTree;
    }

    public static FileOpTree fileOpFail(String sourceId, String message) {
        FileOpTree fileOpTree = new FileOpTree();
        fileOpTree.setSourceId(sourceId);
        fileOpTree.setMessage(message);
        return fileOpTree;
    }

    public static FileOpTree dirOpOk(String sourceId, String targetId) {
        FileOpTree fileOpTree = new FileOpTree();
        fileOpTree.setSourceId(sourceId);
        fileOpTree.setTargetId(targetId);
        fileOpTree.setSuccess(true);
        fileOpTree.setMessage("目录复制成功");
        fileOpTree.setDirectory(true);
        return fileOpTree;
    }

    public static FileOpTree dirOpFail(String sourceId, String message) {
        FileOpTree fileOpTree = new FileOpTree();
        fileOpTree.setSourceId(sourceId);
        fileOpTree.setMessage(message);
        fileOpTree.setDirectory(true);
        return fileOpTree;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public List<FileOpTree> getFileOps() {
        return fileOps;
    }

    public void setFileOps(List<FileOpTree> fileOps) {
        this.fileOps = fileOps;
    }
}
