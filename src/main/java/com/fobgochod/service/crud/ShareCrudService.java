package com.fobgochod.service.crud;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.crud.base.EntityService;

import java.util.List;

public interface ShareCrudService extends EntityService<ShareRecord> {

    String shareFile(FileInfo fileInfo);

    void deleteByFileId(String fileInfoId);

    ShareRecord shareFile(BatchFid batchFid);

    ShareRecord getShareUnExpired(String shareId);

    List<ShareRecord> getShareFileByFileId(String fileId);
}
