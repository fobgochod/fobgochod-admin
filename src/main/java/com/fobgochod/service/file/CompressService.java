package com.fobgochod.service.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.FileTree;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface CompressService {

    List<FileTree> getFileTrees(BatchFid body);

    String getFileName(List<FileTree> fileTrees);

    void compressFile(List<FileTree> fileTrees, String zipFileName, HttpServletResponse response);

    void getArchiveEntry(ZipArchiveOutputStream zipOs, FileTree fileTree);
}
