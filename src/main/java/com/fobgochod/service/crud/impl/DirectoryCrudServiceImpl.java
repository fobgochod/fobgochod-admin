package com.fobgochod.service.crud.impl;

import com.fobgochod.service.crud.DirectoryCrudService;
import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.service.crud.base.BaseEntityService;
import com.fobgochod.util.IdUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryCrudServiceImpl extends BaseEntityService<DirInfo> implements DirectoryCrudService {

    @Override
    public boolean exists(String id) {
        if (IdUtil.isRootDir(id)) {
            return true;
        }
        return super.existsById(id);
    }

    @Override
    public List<DirInfo> findByParentId(String parentId) {
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        FindIterable<DirInfo> iterable = mongoCollection.find(Filters.eq(BaseField.DIR_PARENT_ID, parentId));
        List<DirInfo> lists = new ArrayList<>();
        for (DirInfo dirInfo : iterable) {
            lists.add(dirInfo);
        }
        return lists;
    }

    @Override
    public List<DirInfo> getDirInfos(String id) {
        List<DirInfo> dirInfos = new ArrayList<>();
        getDirInfo(id, dirInfos);
        return dirInfos;
    }

    private void getDirInfo(String parentId, List<DirInfo> dirInfos) {
        if (!IdUtil.isRootDir(parentId)) {
            DirInfo dirInfo = this.findById(parentId);
            dirInfos.add(dirInfo);
            getDirInfo(dirInfo.getParentId(), dirInfos);
        }
    }
}
