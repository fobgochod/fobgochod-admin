package com.fobgochod.service.mongo;

import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.File;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Files
 *
 * @author zhouxiao
 * @date 2021/3/12
 */
@Service
public class FilesCollection {

    private final MongoCollection<File> filesCollection;

    public FilesCollection(MongoDatabase database) {
        this.filesCollection = database.getCollection("fs.files", File.class);
    }

    public List<File> find() {
        MongoCursor<File> cursor = filesCollection.find().iterator();
        List<File> files = new ArrayList<>();
        while (cursor.hasNext()) {
            files.add(cursor.next());
        }
        return Collections.unmodifiableList(files);
    }

    public File findById(ObjectId fileId) {
        return filesCollection.find(Filters.eq(BaseField.ID, fileId)).first();
    }

    public void referenceCount(ObjectId fileId, long delta) {
        Bson filter = Filters.eq(BaseField.ID, fileId);
        Bson update = Updates.inc(BaseField.METADATA_REFERENCE_COUNT, delta);
        filesCollection.updateOne(filter, update);
    }

    public long getFilesSize() {
        List<File> files = this.find();
        return files.stream().map(File::getLength).reduce(Long::sum).orElse(0L);
    }

    public void drop() {
        filesCollection.drop();
    }
}
