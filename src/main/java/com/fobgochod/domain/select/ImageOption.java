package com.fobgochod.domain.select;

/**
 * 图片预览信息
 *
 * @author seven
 * @date 2021/3/13
 */
public class ImageOption {

    private String fileId;
    private String fileName;
    private String uri;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
