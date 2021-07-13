package com.fobgochod.util;

import com.fobgochod.constant.BaseField;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.DirTree;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;

import java.util.List;
import java.util.Optional;

public class FileUtil {

    /**
     * 根据文件名获取文件后缀
     * mongo.exe->exe
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileExt(String fileName) {
        String[] fileNameArray = fileName.split("\\.");
        if (fileNameArray.length > 1) {
            return fileNameArray[fileNameArray.length - 1];
        }
        return null;
    }

    /**
     * 去除文件后缀名
     * mongo.exe->mongo
     *
     * @param fileName 文件名
     * @return 文件名
     */
    public static String getFileNameNoExt(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf(FghConstants.DOT);
            if ((dot > -1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    /**
     * 获取重复文件名
     * 目录L1->目录L1 - 副本
     * mongo.exe->mongo - 副本.exe
     *
     * @param fileName 文件名
     * @return 重复文件名
     */
    public static String getDupFileName(String fileName) {
        String dupFileName = String.format(BaseField.DUPLICATE_NAME, getFileNameNoExt(fileName));
        String extension = getFileExt(fileName);
        if (extension != null) {
            return dupFileName + FghConstants.DOT + extension;
        }
        return dupFileName;
    }

    public static String getDupFileName0(String fileName) {
        String fileNameNoExt = getFileNameNoExt(fileName);
        String extension = getFileExt(fileName);
        if (extension != null) {
            return getDupName(fileNameNoExt) + FghConstants.DOT + extension;
        }
        return getDupName(fileNameNoExt);
    }

    public static String getDupName(String fileName) {
        boolean hasDup = fileName.contains(BaseField.DUPLICATE);
        if (hasDup) {
            String nameNoDup = fileName.substring(0, fileName.indexOf(BaseField.DUPLICATE));
            String dupName = fileName.substring(fileName.indexOf(BaseField.DUPLICATE));
            int numBegin = dupName.indexOf("(");
            if (numBegin < 0) {
                return fileName + " (2)";
            }
            int numEnd = dupName.indexOf(")");
            int dupNum = Integer.parseInt(dupName.substring(numBegin + 1, numEnd));
            return nameNoDup + dupName.replace("(" + dupNum + ")", "(" + (dupNum + 1) + ")");
        } else {
            return fileName + BaseField.DUPLICATE;
        }
    }

    /**
     * 根据目录树 计算目录大小
     *
     * @param dirTree 目录树
     * @return 目录大小
     */
    public static long getDirSize(DirTree dirTree) {
        long size = 0;
        if (dirTree != null) {
            size += dirTree.getFiles().stream().mapToLong(FileInfo::getSize).sum();
            for (DirTree childDir : dirTree.getDirs()) {
                size += getDirSize(childDir);
            }
        }
        return size;
    }

    public static String getPaths(String dir, List<DirInfo> dirInfos) {
        if (BaseField.ROOT_DIR.equals(dir)) {
            return "/";
        } else {
            Optional<DirInfo> optional = dirInfos.stream().filter(o -> o.getId().equals(dir)).findFirst();
            if (optional.isPresent()) {
                DirInfo dirInfo = optional.get();
                if ((BaseField.ROOT_DIR.equals(dirInfo.getParentId()))) {
                    return "/" + dirInfo.getName();
                } else {
                    return getPaths(dirInfo.getParentId(), dirInfos) + "/" + dirInfo.getName();
                }
            } else {
                return "";
            }
        }
    }
}
