package com.fobgochod.config;

import com.fobgochod.domain.EnvProperties;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.gridfs.GridFS;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfiguration {

    @Autowired
    private EnvProperties env;
    @Autowired
    private MongoClient mongoClient;

    @Bean
    public MongoDatabase mongoDatabase() {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(env.getDatabase());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return mongoDatabase.withCodecRegistry(pojoCodecRegistry);
    }

    @Bean
    public GridFS gridFS() {
        return new GridFS(mongoClient.getDB(env.getDatabase()));
    }

    @Bean
    public GridFSBucket gridFSBucket(MongoDatabase mongoDatabase) {
        return GridFSBuckets.create(mongoDatabase);
    }
}
