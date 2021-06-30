package com.fobgochod.service.client;

import com.fobgochod.service.client.base.EntityService;
import com.fobgochod.entity.file.DirInfo;
import org.bson.conversions.Bson;

import java.util.List;

public interface DirectoryCrudService extends EntityService<DirInfo> {

    boolean exists(String id);

    boolean existsByName(String name);

    boolean existsByName(String parentId, String name);

    DirInfo findByName(String parentId, String name);

    void renameDir(String id, String newName);

    List<DirInfo> findByName(String name);

    List<DirInfo> findByParentId(String parentId);

    List<DirInfo> findByParentId(String parentId, Bson sort);

    List<DirInfo> getDirInfos(String id);

}
