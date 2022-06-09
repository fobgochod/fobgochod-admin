package com.fobgochod.service.crud.impl;

import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.service.crud.ShrinkCrudService;
import com.fobgochod.service.crud.base.BaseEntityService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShrinkCrudServiceImpl extends BaseEntityService<ShrinkImage> implements ShrinkCrudService {

    @Override
    public void deleteByShrinkId(String shrinkId) {
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        mongoCollection.deleteOne(Filters.eq("shrinkId", shrinkId));
    }

    @Override
    public ShrinkImage findByWidthAndHeight(String fileId, int width, int height) {
        List<Bson> filters = new ArrayList<>();
        Bson field1 = Filters.eq("fileId", fileId);
        Bson field2 = Filters.eq("width", width);
        Bson field3 = Filters.eq("height", height);
        filters.add(field1);
        filters.add(field2);
        filters.add(field3);
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        return mongoCollection.find(Filters.and(filters)).first();
    }


    @Override
    public List<ShrinkImage> findByFileId(String fileId) {
        Bson filter = Filters.eq("fileId", fileId);
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        MongoCursor<ShrinkImage> result = mongoCollection.find(filter, ShrinkImage.class).iterator();
        List<ShrinkImage> shrinkImages = new ArrayList<>();
        while (result.hasNext()) {
            shrinkImages.add(result.next());
        }
        return shrinkImages;
    }
}
