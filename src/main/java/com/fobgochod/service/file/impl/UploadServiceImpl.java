package com.fobgochod.service.file.impl;

import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.file.UploadService;
import com.fobgochod.service.mongo.FileStorage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, InputStream source) {
        try {
            fileInfoCrudService.fillFileInfo(fileInfo);

            String fileId = fileStorage.uploadFromStream(fileInfo.getName(), source);
            long size = fileStorage.getFileLength(fileId);

            fileInfo.setFileId(new ObjectId(fileId));
            fileInfo.setSize(size);
            fileInfo.setCompleted(Boolean.TRUE);
            fileInfoCrudService.insert(fileInfo);

            return fileInfo;
        } catch (Exception e) {
            this.deleteFile(fileInfo.getId());
            throw new SystemException("File upload failed !");
        }
    }

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, MultipartFile file) {
        try {
            fileInfoCrudService.fillFileInfo(fileInfo, file);
            uploadFile(fileInfo, file.getInputStream());
            return fileInfo;
        } catch (Exception e) {
            this.deleteFile(fileInfo.getId());
            throw new SystemException(String.format("上传失败 %s", e.getMessage()));
        }
    }

    private void deleteFile(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        deleteFile(fileInfo);
    }

    @Override
    public void deleteFile(FileInfo fileInfo) {
        if (fileInfo == null || fileInfo.getFileId() == null) {
            return;
        }
        boolean delete = fileInfoCrudService.needDelete(fileInfo.getId(), fileInfo.getFileId());
        if (delete) {
            fileStorage.deleteFile(fileInfo.getFileId());
        }
    }
}
