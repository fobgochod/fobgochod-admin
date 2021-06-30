package com.fobgochod.service.business.impl;

import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.business.FileTagService;
import com.fobgochod.service.client.FileInfoCrudService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 文件标签
 *
 * @author seven
 * @date 2021/3/11
 */
@Service
public class FileTagServiceImpl implements FileTagService {

    private static final String SEMICOLON = ";";

    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public void addTag(String fileInfoId, String tag) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        String oldTags = fileInfo.getTag();
        if (!StringUtils.isEmpty(oldTags)) {
            tag = oldTags + SEMICOLON + tag;
        }
        fileInfo.setTag(tag);
        fileInfoCrudService.update(fileInfo);
    }


    @Override
    public void deleteTag(String fileInfoId, String tag) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        String[] tags = fileInfo.getTag().split(SEMICOLON);
        String newTags = Arrays.stream(tags).filter(o -> !tag.equals(o)).collect(Collectors.joining(SEMICOLON));
        fileInfo.setTag(newTags);
        fileInfoCrudService.update(fileInfo);
    }

    @Override
    public void deleteTag(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        fileInfo.setTag(null);
        fileInfoCrudService.update(fileInfo);

    }

    @Override
    public void replaceTag(String fileInfoId, String tag) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        fileInfo.setTag(tag);
        fileInfoCrudService.update(fileInfo);
    }

    @Override
    public String getTag(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        return fileInfo.getTag() == null ? Strings.EMPTY : fileInfo.getTag();
    }
}
