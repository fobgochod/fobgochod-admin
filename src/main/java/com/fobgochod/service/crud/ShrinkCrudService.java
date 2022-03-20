package com.fobgochod.service.crud;

import com.fobgochod.service.crud.base.EntityService;
import com.fobgochod.entity.file.ShrinkImage;

import java.util.List;

/**
 * 图片压缩
 *
 * @author seven
 * @date 2021/3/12
 */
public interface ShrinkCrudService extends EntityService<ShrinkImage> {

    /**
     * 删除压缩记录
     *
     * @param targetId
     */
    void deleteByTargetId(String targetId);

    /**
     * 按压缩
     *
     * @param sourceId
     * @param width
     * @param height
     * @return
     */
    ShrinkImage findByProperty(String sourceId, int width, int height);

    /**
     * 获取原图所有压缩图片
     *
     * @param sourceId 文件id
     * @return ImageShrink
     */
    List<ShrinkImage> findBySourceId(String sourceId);
}
