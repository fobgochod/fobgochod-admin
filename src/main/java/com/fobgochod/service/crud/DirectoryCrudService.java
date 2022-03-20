package com.fobgochod.service.crud;

import com.fobgochod.service.crud.base.EntityService;
import com.fobgochod.entity.file.DirInfo;

import java.util.List;

public interface DirectoryCrudService extends EntityService<DirInfo> {

    boolean exists(String id);

    DirInfo findByName(String parentId, String name);

    List<DirInfo> findByParentId(String parentId);

    List<DirInfo> getDirInfos(String id);
}
