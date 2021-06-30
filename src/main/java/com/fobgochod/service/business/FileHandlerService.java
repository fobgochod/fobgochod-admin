package com.fobgochod.service.business;

import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.domain.v2.FileTree;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileHandlerService {

    List<FileTree> getFileTrees(BatchFid body);

    String getFileName(List<FileTree> fileTrees);

    void compressFile(List<FileTree> fileTrees, String zipFileName, HttpServletResponse response);

    void getArchiveEntry(ZipArchiveOutputStream zipOs, FileTree fileTree);
}
