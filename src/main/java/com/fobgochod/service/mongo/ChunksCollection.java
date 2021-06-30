package com.fobgochod.service.mongo;

import com.fobgochod.entity.Chunk;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.gridfs.GridFS;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File.chunks
 *
 * @author zhouxiao
 * @date 2021/3/12
 */
@Service
public class ChunksCollection {

    private final MongoCollection<Chunk> chunksCollection;

    public ChunksCollection(MongoDatabase database) {
        this.chunksCollection = database.getCollection("fs.chunks", Chunk.class);
    }

    public List<Chunk> findByFileId(ObjectId fileId) {
        MongoCursor<Chunk> cursor = chunksCollection.find(Filters.eq("files_id", fileId)).iterator();
        List<Chunk> chunks = new ArrayList<>();
        while (cursor.hasNext()) {
            chunks.add(cursor.next());
        }
        return Collections.unmodifiableList(chunks);
    }

    private Binary getData(int bufferOffset, byte[] buffer) {
        if (bufferOffset < GridFS.DEFAULT_CHUNKSIZE) {
            byte[] sizedBuffer = new byte[bufferOffset];
            System.arraycopy(buffer, 0, sizedBuffer, 0, bufferOffset);
            buffer = sizedBuffer;
        }
        return new Binary(buffer);
    }
}
