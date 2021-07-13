package com.fobgochod.service.client;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.client.base.EntityService;

import java.util.List;

public interface ShareCrudService extends EntityService<ShareRecord> {

    void deleteByFileId(String fileInfoId);

    ShareRecord shareFile(BatchFid batchFid);

    boolean isShareExpired(ShareRecord shareRecord);

    List<ShareRecord> getShareFileByFileId(String fileId);
}
