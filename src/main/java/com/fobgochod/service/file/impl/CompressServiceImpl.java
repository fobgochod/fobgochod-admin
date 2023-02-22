package com.fobgochod.service.file.impl;

import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.file.CompressService;
import com.fobgochod.service.file.DownloadService;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.util.FileUtil;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件处理、加功、压缩等
 *
 * @author zhouxiao
 * @date 2021/2/24
 */
@Service
public class CompressServiceImpl implements CompressService {

    private static final Logger logger = LoggerFactory.getLogger(CompressServiceImpl.class);

    @Autowired
    private DownloadService downloadService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 为了在压缩文件中包含空文件夹
     *
     * @param dir
     * @return
     */
    private static String formatDirPath(String dir) {
        if (!dir.endsWith(java.io.File.separator)) {
            dir += java.io.File.separator;
        }
        return dir;
    }

    @Override
    public List<FileTree> getFileTrees(BatchFid body) {
        body.afterPropertiesSet();
        List<FileTree> fileTrees = new ArrayList<>();
        for (String fileId : body.getFileIds()) {
            FileInfo fileInfo = fileInfoCrudService.findById(fileId);
            FileTree fileTree = new FileTree(fileInfo.getId(), fileInfo.getName());
            fileTree.setFilePath(fileInfo.getName());
            fileTrees.add(fileTree);
        }
        for (String directoryId : body.getDirIds()) {
            FileTree dirTree = fileInfoCrudService.getFileTree(directoryId, null);
            fileTrees.add(dirTree);
        }
        return fileTrees;
    }

    @Override
    public String getFileName(List<FileTree> fileTrees) {
        String fileName = String.format("批量下载[%s]", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
        final int size = fileTrees.size();
        if (size == 1) {
            fileName = fileTrees.get(0).getFileName();
        } else {
            Optional<FileTree> dir = fileTrees.stream().filter(FileTree::isDirectory).findFirst();
            if (dir.isPresent()) {
                fileName = dir.get().getFileName();
            } else {
                Optional<FileTree> file = fileTrees.stream().findFirst();
                if (file.isPresent()) {
                    fileName = FileUtil.getFileNameNoExt(file.get().getFileName());
                }
            }
        }
        return fileName + ".zip";
    }

    @Override
    public void compressFile(List<FileTree> fileTrees, String zipFileName, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
            // 通过 OutputStream 创建 zip 压缩流。如果是压缩到本地，也可以直接使用 ZipArchiveOutputStream(final File file)
            ZipArchiveOutputStream zipOs = new ZipArchiveOutputStream(response.getOutputStream());
            // setUseZip64(final Zip64Mode mode)：是否使用 Zip64 扩展。
            // Zip64Mode 枚举有 3 个值：Always：对所有条目使用 Zip64 扩展、Never：不对任何条目使用Zip64扩展、AsNeeded：对需要的所有条目使用Zip64扩展
            zipOs.setUseZip64(Zip64Mode.AsNeeded);
            for (FileTree fileTree : fileTrees) {
                getArchiveEntry(zipOs, fileTree);
            }
            //7）最后关闭 zip 压缩输出流.
            zipOs.close();
        } catch (IOException e) {
            logger.error("压缩文件失败 {}", e.getMessage());
        }
    }

    @Override
    public void getArchiveEntry(ZipArchiveOutputStream zipOs, FileTree fileTree) {
        try {
            if (fileTree.isDirectory()) {
                for (FileTree tree : fileTree.getFiles()) {
                    getArchiveEntry(zipOs, tree);
                }
                fileTree.setFilePath(formatDirPath(fileTree.getFilePath()));
            }
            ArchiveEntry entry = new ZipArchiveEntry(fileTree.getFilePath());
            zipOs.putArchiveEntry(entry);
            if (!fileTree.isDirectory()) {
                // 写入此条目的所有必要数据。如果条目未压缩或压缩后的大小超过4 GB 则抛出异常
                byte[] fileBytes = downloadService.downloadToBytes(fileTree.getFileId());
                zipOs.write(fileBytes);
            }
            zipOs.closeArchiveEntry();
        } catch (Exception e) {
            logger.error("创建压缩文件条目失败 {}", e.getMessage());
        }
    }
}
