package com.fobgochod.entity;

import com.mongodb.gridfs.GridFSDBFile;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 文件信息
 *
 * @author seven
 * @date 2020/11/24
 * @see com.mongodb.client.gridfs.model.GridFSFile
 */
public class File {

    private ObjectId id;
    private String filename;
    private long length;
    private long chunkSize;
    private Date uploadDate;
    private String md5;
    private Document metadata;
    private Document extraElements;

    public File() {
    }

    public File(GridFSDBFile dbFile) {
        this.id = (ObjectId) dbFile.getId();
        this.filename = dbFile.getFilename();
        this.length = dbFile.getLength();
        this.chunkSize = dbFile.getChunkSize();
        this.uploadDate = dbFile.getUploadDate();
        this.md5 = dbFile.getMD5();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Document getMetadata() {
        return metadata;
    }

    public void setMetadata(Document metadata) {
        this.metadata = metadata;
    }

    public Document getExtraElements() {
        return extraElements;
    }

    public void setExtraElements(Document extraElements) {
        this.extraElements = extraElements;
    }
}
