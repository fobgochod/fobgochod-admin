package com.fobgochod.service.client.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.service.client.DirectoryCrudService;
import com.fobgochod.service.client.base.BaseEntityService;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.util.IdUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public boolean existsByName(String name) {
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        long documents = mongoCollection.countDocuments(Filters.eq(BaseField.DIR_NAME, name));
        return documents > 0;
    }

    @Override
    public boolean existsByName(String parentId, String name) {
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        Bson filter = Filters.and(
                Filters.eq(BaseField.DIR_PARENT_ID, parentId),
                Filters.eq(BaseField.DIR_NAME, name)
        );
        long documents = mongoCollection.countDocuments(filter);
        return documents > 0;
    }

    @Override
    public DirInfo findByName(String parentId, String name) {
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        Bson filter = Filters.and(
                Filters.eq(BaseField.DIR_PARENT_ID, parentId),
                Filters.eq(BaseField.DIR_NAME, name)
        );
        return mongoCollection.find(filter).first();
    }

    @Override
    public void renameDir(String id, String newName) {
        DirInfo dirInfo = this.findById(id);
        if (dirInfo != null) {
            if (this.findByName(dirInfo.getParentId(), newName) != null) {
                throw new BusinessException("存在相同的目录名称，重命名失败！");
            }
            dirInfo.setName(newName);
            super.update(dirInfo);
        }
    }

    @Override
    public List<DirInfo> findByName(String name) {
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$");
        Bson filter = Filters.regex(BaseField.DIR_NAME, pattern);
        FindIterable<DirInfo> iterable = mongoCollection.find(filter);
        List<DirInfo> lists = new ArrayList<>();
        for (DirInfo dirInfo : iterable) {
            lists.add(dirInfo);
        }
        return lists;
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
    public List<DirInfo> findByParentId(String parentId, Bson sort) {
        Bson query = Filters.eq(BaseField.DIR_PARENT_ID, parentId);
        MongoCollection<DirInfo> mongoCollection = this.getCollection();
        MongoCursor<DirInfo> iterator = mongoCollection
                .find(query)
                .sort(Sorts.ascending(BaseField.DIR_NAME))
                .iterator();
        List<DirInfo> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
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
