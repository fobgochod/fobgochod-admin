package com.fobgochod.domain;

import com.fobgochod.entity.file.FileInfo;

/**
 * 图片处理
 *
 * @author zhouxiao
 * @date 2020/5/13
 */
public class ImageHandle {

    private ImageInfo imageInfo;
    private FileInfo fileInfo;

    public ImageHandle() {
    }

    public ImageHandle(ImageInfo imageInfo, FileInfo fileInfo) {
        this.imageInfo = imageInfo;
        this.fileInfo = fileInfo;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
