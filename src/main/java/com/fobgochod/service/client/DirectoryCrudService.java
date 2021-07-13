package com.fobgochod.service.client;

import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.service.client.base.EntityService;

import java.util.List;

public interface DirectoryCrudService extends EntityService<DirInfo> {

    boolean exists(String id);

    DirInfo findByName(String parentId, String name);

    List<DirInfo> findByParentId(String parentId);

    List<DirInfo> getDirInfos(String id);
}
