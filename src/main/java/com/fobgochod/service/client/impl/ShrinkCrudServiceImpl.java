package com.fobgochod.service.client.impl;

import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.service.client.ShrinkCrudService;
import com.fobgochod.service.client.base.BaseEntityService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShrinkCrudServiceImpl extends BaseEntityService<ShrinkImage> implements ShrinkCrudService {

    /**
     * 原图片信息ID
     */
    private static final String IMAGE_SHRINK_SOURCE_ID = "sourceId";

    @Override
    public void deleteByTargetId(String targetId) {
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        mongoCollection.deleteOne(Filters.eq("targetId", targetId));
    }

    @Override
    public ShrinkImage findByProperty(String sourceId, int width, int height) {
        List<Bson> conditions = new ArrayList<>();
        Bson field1 = Filters.eq("sourceId", sourceId);
        Bson field2 = Filters.eq("property.width", width);
        Bson field3 = Filters.eq("property.height", height);
        conditions.add(field1);
        conditions.add(field2);
        conditions.add(field3);
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        return mongoCollection.find(Filters.and(conditions)).first();
    }


    @Override
    public List<ShrinkImage> findBySourceId(String sourceId) {
        Bson filter = Filters.eq(IMAGE_SHRINK_SOURCE_ID, sourceId);
        MongoCollection<ShrinkImage> mongoCollection = this.getCollection();
        MongoCursor<ShrinkImage> result = mongoCollection.find(filter, ShrinkImage.class).iterator();
        List<ShrinkImage> shrinkImages = new ArrayList<>();
        while (result.hasNext()) {
            shrinkImages.add(result.next());
        }
        return shrinkImages;
    }
}
