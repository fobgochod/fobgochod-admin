package com.fobgochod.service.mongo;

import com.fobgochod.constant.BaseField;
import com.fobgochod.exception.SystemException;
import com.fobgochod.util.IdUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文档存储接口，用于屏蔽各种存储源（MongoDB、文件系统）的差异
 *
 * @author Xiao
 * @date 2022/3/20 0:43
 */
@Service
public class FileStorage {

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 把指定文件下载到流中
     *
     * @param fileId      文件id
     * @param destination 下载到的目标流
     */
    public void downloadToStream(ObjectId fileId, OutputStream destination) {
        gridFSBucket.downloadToStream(fileId, destination);
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
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * 把文件的指定部分下载到流中
     *
     * @param fileId      文件id 文件的id
     * @param destination 输出流
     * @param begin       开始位置
     * @param count       总长度
     */
    public void downloadPartToStream(ObjectId fileId, OutputStream destination, long begin, long count) {
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
                    destination.write(buffer, 0, (int) (count - toLen));
                } else {
                    destination.write(buffer, 0, read);
                }
                toLen += read;
            }
        } catch (IOException e) {
            throw new SystemException("读取文件到流失败");
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
        return gridFSBucket.uploadFromStream(fileName, source).toString();
    }

    /**
     * 把二进制流作为文件内容上传
     *
     * @param fileName
     * @param source
     * @return
     */
    public String uploadFromBytes(String fileName, byte[] source) {
        InputStream inputStream = new ByteArrayInputStream(source);
        return gridFSBucket.uploadFromStream(fileName, inputStream).toString();
    }

    /**
     * 获取当前文件大小
     *
     * @param fileId 文件Id
     */
    public long getFileLength(String fileId) {
        return this.getGridFile(fileId).getLength();
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    public void deleteFile(ObjectId fileId) {
        GridFSFile file = this.getGridFile0(fileId);
        if (file != null) {
            gridFSBucket.delete(fileId);
        }
    }

    private GridFSFile getGridFile(String fileId) {
        return getGridFile(IdUtil.getObjectId(fileId));
    }

    private GridFSFile getGridFile0(ObjectId fileId) {
        return gridFSBucket.find(Filters.eq(BaseField.ID, fileId)).first();
    }

    private GridFSFile getGridFile(ObjectId fileId) {
        GridFSFile gridFile = getGridFile0(fileId);
        if (gridFile == null) {
            throw new SystemException("文件相关的信息已经被删除：" + fileId);
        }
        return gridFile;
    }
}
