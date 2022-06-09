package com.fobgochod.service.crud;

import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.service.crud.base.EntityService;

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
     * @param shrinkId
     */
    void deleteByShrinkId(String shrinkId);

    /**
     * 按压缩
     *
     * @param fileId
     * @param width
     * @param height
     * @return
     */
    ShrinkImage findByWidthAndHeight(String fileId, int width, int height);

    /**
     * 获取原图所有压缩图片
     *
     * @param fileId 文件id
     * @return ImageShrink
     */
    List<ShrinkImage> findByFileId(String fileId);
}
