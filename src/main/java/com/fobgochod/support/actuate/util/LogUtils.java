package com.fobgochod.support.actuate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 日志目录文件处理工具
 *
 * @author zhouxiao
 * @date 2021/7/1
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    private static final Pattern PATTERN = Pattern.compile("[^0-9]");
    private static final String LOG_PATH = ".logs";

    public static Map<String, List<String>> getFileLists() {
        Map<String, List<String>> fileLists = new HashMap<>();
        File logRootDir = new File(LOG_PATH);
        File[] dirs = logRootDir.listFiles();
        if (dirs == null) {
            return fileLists;
        }
        for (File dir : dirs) {
            String[] fileNames = dir.list();
            if (fileNames == null) {
                fileLists.put(dir.getPath(), Collections.emptyList());
            } else {
                fileLists.put(dir.getPath(), Arrays.asList(fileNames));
            }
        }
        return fileLists;
    }

    public static Resource getLogFileResource(String level) {
        return getLogFileResource(level, null);
    }

    /**
     * .logs<br/>
     * ├── debug<br/>
     * ├── error<br/>
     * ├── info<br/>
     * └── warn<br/>
     * <p>
     * 根据目录和文件名查找日志文件<br/>
     * 文件不存在就找指定目录最新的文件
     *
     * @param level    日志level
     * @param fileName 日志文件名
     * @return 文件资源
     */
    public static Resource getLogFileResource(String level, String fileName) {
        String pathName = String.format("%s/%s/%s", LOG_PATH, level, fileName);
        File targetFile = new File(pathName);
        if (!targetFile.exists()) {
            File[] listFiles = new File(String.format("%s/%s", LOG_PATH, level)).listFiles();
            if (listFiles == null) {
                return null;
            }
            long maxSort = -1;
            for (File file : listFiles) {
                long sort = Long.parseLong(PATTERN.matcher(file.getName()).replaceAll("").trim());
                if (sort > maxSort) {
                    maxSort = sort;
                    targetFile = file;
                }
            }
        }
        logger.info("succeed find the newest log file {}", targetFile.getPath());
        return new FileSystemResource(targetFile.getPath());
    }
}
