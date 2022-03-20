package com.fobgochod.service.file;

import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.entity.file.FileInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 文件操作
 *
 * @author seven
 * @date 2021/3/8
 */
public interface DownloadService {

    /**
     * 把指定文件下载到流中
     *
     * @param fileInfo 文件信息
     * @param out      下载到的目标输出流
     */
    void downloadToStream(FileInfo fileInfo, OutputStream out);

    /**
     * 把文件的指定部分下载到流中
     *
     * @param fileInfo 文件信息
     * @param out      输出流
     * @param start    开始位置
     * @param length   读取长度
     */
    void downloadPartToStream(FileInfo fileInfo, OutputStream out, long start, long length);

    /**
     * 把指定文件下载到流中
     *
     * @param fileId 文件的id
     * @return 返回文件字节流
     */
    byte[] downloadToBytes(String fileId);

    /**
     * 下载或者预览文件
     *
     * @param fileInfoId
     * @param type
     * @param request
     * @param response
     */
    void downloadFile(String fileInfoId, InlineAttachment type, HttpServletRequest request, HttpServletResponse response);

    /**
     * 下载或者预览文件
     *
     * @param fileInfoId 文件的id
     * @param type       下载/预览
     * @param response
     */
    void downloadFile(String fileInfoId, InlineAttachment type, HttpServletResponse response);

    /**
     * 解析请求头Range属性，实现断点续传
     * 解决IOS系统mp4等视频文件不能播放问题
     *
     * @param fileInfo 文件
     * @param type     下载/预览
     * @param request
     * @param response
     * @see https://blog.csdn.net/zhengbin6072/article/details/78235004/
     * @see https://www.jb51.net/article/178273.htm
     */
    void downloadFileRange(FileInfo fileInfo, InlineAttachment type, HttpServletRequest request, HttpServletResponse response);
}
