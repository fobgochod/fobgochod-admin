package com.fobgochod.domain;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 复制文件线程变量
 *
 * @author zhuzcz
 * @date 2021/3/4
 */
public final class FileOpTreeContextHolder {

    private static final ThreadLocal<List<FileOpTree>> contextHolder = new NamedThreadLocal<>("FileOpTreeContext");

    private FileOpTreeContextHolder() {
    }

    public static void resetContext() {
        contextHolder.remove();
    }

    public static List<FileOpTree> getContext() {
        List<FileOpTree> fileOpTrees = contextHolder.get();
        if (fileOpTrees == null) {
            fileOpTrees = createEmptyContext();
            contextHolder.set(fileOpTrees);
        }
        return fileOpTrees;
    }

    public static void setContext(@Nullable List<FileOpTree> fileOpTrees) {
        if (fileOpTrees == null) {
            resetContext();
        }
        contextHolder.set(fileOpTrees);
    }

    private static List<FileOpTree> createEmptyContext() {
        return new ArrayList<>();
    }
}
