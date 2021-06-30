package com.fobgochod.service.client.impl;

import com.fobgochod.service.client.RecycleCrudService;
import com.fobgochod.service.client.base.BaseEntityService;
import com.fobgochod.entity.file.RecycleBin;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecycleCrudServiceImpl extends BaseEntityService<RecycleBin> implements RecycleCrudService {

    @Override
    public List<RecycleBin> findLtDeleteDate(LocalDateTime deleteDate) {
        MongoCollection<RecycleBin> mongoCollection = this.getCollection();
        MongoCursor<RecycleBin> iterator = mongoCollection.find(Filters.lt("deleteDate", deleteDate)).iterator();
        List<RecycleBin> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public List<RecycleBin> findAllSorted(Bson sort) {
        MongoCollection<RecycleBin> mongoCollection = this.getCollection();
        MongoCursor<RecycleBin> result = mongoCollection.find().sort(sort).iterator();
        List<RecycleBin> lists = new ArrayList<>();
        while (result.hasNext()) {
            lists.add(result.next());
        }
        return lists;
    }
}
