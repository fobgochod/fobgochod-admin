package com.fobgochod.service.mongo;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.EnvProperties;
import com.fobgochod.domain.FileReference;
import com.fobgochod.entity.File;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.util.IdUtil;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 文档存储接口，用于屏蔽各种存储源（MongoDB、文件系统）的差异
 *
 * @author chenxsa
 */
@Service
public class FileStorage {

    @Autowired
    private EnvProperties env;
    @Autowired
    private GridFS gridFS;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;


    /**
     * 把指定文件下载到流中
     *
     * @param fileId 文件id
     * @param out    下载到的目标流
     */
    public void downloadToStream(ObjectId fileId, OutputStream out) {
        gridFSBucket.downloadToStream(fileId, out);
    }

    /**
     * 把指定文件下载到流中
     *
     * @param fileId 文件id 文件的id
     * @return 返回文件流
     */
    public byte[] downloadToBytes(String fileId) {
        try (GridFSDownloadStream stream = gridFSBucket.openDownloadStream(new ObjectId(fileId))) {
            GridFSFile file = stream.getGridFSFile();
            int size = file.getChunkSize();
            int len = (int) file.getLength();
            int readSize = Math.min(len, size);
            byte[] returnBts = new byte[len];
            int offset = 0;
            while (len > 0) {
                int tmp;
                if (len > readSize) {
                    tmp = stream.read(returnBts, offset, readSize);
                    offset += tmp;
                } else {
                    tmp = stream.read(returnBts, offset, len);
                }
                len -= tmp;
            }
            return returnBts;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 把文件的指定部分下载到流中
     *
     * @param fileId 文件id 文件的id
     * @param to     输出流
     * @param begin  开始位置
     * @param count  总长度
     */
    public void downloadPartToStream(ObjectId fileId, OutputStream to, long begin, long count) {
        try (GridFSDownloadStream stream = gridFSBucket.openDownloadStream(fileId)) {
            long toLen = 0;
            //跳过指定的流字节数
            if (begin > 0) {
                stream.skip(begin);
            }
            int bufferSize = 81920;
            byte[] buffer = new byte[bufferSize];
            int read;
            while ((toLen < count) && (read = stream.read(buffer, 0, buffer.length)) > 0) {
                if (toLen + read > count) {
                    to.write(buffer, 0, (int) (count - toLen));
                } else {
                    to.write(buffer, 0, read);
                }
                toLen += read;
            }
        } catch (IOException e) {
            throw new BusinessException("读取文件到流失败", e);
        }
    }


    /**
     * 把指定流作为文件内容上传
     *
     * @param fileName 文件名
     * @param source   数据
     * @return
     */
    public String uploadFromStream(String fileName, InputStream source) {
        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(createFileMetadata());
        return gridFSBucket.uploadFromStream(fileName, source, options).toString();
    }

    /**
     * 把二进制流作为文件内容上传
     *
     * @param fileName
     * @param source
     * @return
     */
    public String uploadFromBytes(String fileName, byte[] source) {
        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(createFileMetadata());
        InputStream inputStream = new ByteArrayInputStream(source);
        return gridFSBucket.uploadFromStream(fileName, inputStream, options).toString();
    }

    private Document createFileMetadata() {
        Map<String, Object> fileMetadata = new HashMap<>();
        FileReference fileReference = new FileReference();
        fileReference.setCount(0);
        fileMetadata.put(BaseField.REFERENCE, fileReference);
        fileMetadata.put(BaseField.COMPLETED, true);
        return new Document(fileMetadata);
    }


    /**
     * 获取当前文件大小
     *
     * @param fileId 文件Id
     * @return
     */
    public long getFileLength(String fileId) {
        return this.getGridFile(fileId).getLength();
    }

    private void endUpload(ObjectId fileId) {
        GridFSDBFile gridFSDBFile = this.getGridFile(fileId);
        gridFSDBFile.getMetaData().put(BaseField.COMPLETED, true);
        gridFSDBFile.save();
    }

    /**
     * 创建空文件
     *
     * @param fileName 文件名称
     */
    public String createFile(String fileName) {
        Document bsonDocument = createFileMetadata();
        bsonDocument.put(BaseField.COMPLETED, false);
        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(bsonDocument);
        byte[] bts = new byte[0];
        return gridFSBucket.uploadFromStream(fileName, new ByteArrayInputStream(bts), options).toString();
    }

    /**
     * 获取文件元数据信息
     *
     * @param fileId 文件id
     * @return 返回元数据信息
     */
    public int getFileMetadataCount(ObjectId fileId) {
        GridFSDBFile gridFile = this.getGridFile0(fileId);
        if (gridFile != null && gridFile.getMetaData() != null) {
            DBObject reference = (DBObject) gridFile.getMetaData().get(BaseField.REFERENCE);
            if (reference != null) {
                return Integer.parseInt(reference.get(BaseField.COUNT).toString());
            }
        }
        return 0;
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    public void deleteFile(String fileId) {
        deleteFile(new ObjectId(fileId));
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    public void deleteFile(ObjectId fileId) {
        gridFSBucket.delete(fileId);
    }

    /**
     * 获取文件的输入流
     *
     * @param fileId 文件Id
     * @return 输入流
     */
    public InputStream getInputStream(ObjectId fileId) {
        GridFSDBFile gridFile = this.getGridFile(fileId);
        if (gridFile.getMetaData() != null) {
            return gridFile.getInputStream();
        }
        return null;
    }

    /**
     * 获取文件信息
     *
     * @param fileId 文件id
     * @return 文件信息
     */
    public File getFile(String fileId) {
        return new File(this.getGridFile(fileId));
    }

    public File getFile(ObjectId fileId) {
        return new File(this.getGridFile(fileId));
    }

    private GridFSDBFile getGridFile(String fileId) {
        return getGridFile(IdUtil.getObjectId(fileId));
    }

    private GridFSDBFile getGridFile0(ObjectId fileId) {
        QueryBuilder query = QueryBuilder.start(BaseField.ID).is(fileId);
        return gridFS.findOne(query.get());
    }

    private GridFSDBFile getGridFile(ObjectId fileId) {
        GridFSDBFile gridFile = getGridFile0(fileId);
        if (gridFile == null) {
            throw new BusinessException("文件相关的信息已经被删除：" + fileId);
        }
        return gridFile;
    }
}
