package com.fobgochod.api.file;

import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.crud.ShrinkCrudService;
import com.fobgochod.service.file.DownloadService;
import com.fobgochod.service.file.FileOpService;
import com.fobgochod.service.file.UploadService;
import com.fobgochod.util.SnowFlake;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;

@RestController
public class ImageFileController {

    @Autowired
    private FileOpService fileOpService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private ShrinkCrudService shrinkCrudService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 按比压缩图片分享地址
     *
     * @param body#id     文件id
     * @param body#width  缩小宽度(px)
     * @param body#height 缩小高度(px)
     * @throws Exception 异常信息
     */
    @PostMapping("/file/images/shrink")
    public Callable<?> getImage(@RequestBody ShrinkImage body) throws Exception {
        FileInfo fileInfo = fileOpService.findFileInfo(body.getFileId());

        ShrinkImage imageShrink = shrinkCrudService.findByWidthAndHeight(body.getFileId(), body.getWidth(), body.getHeight());
        if (imageShrink != null) {
            FileInfo shrinkInfo = fileInfoCrudService.findById(imageShrink.getShrinkId());
            return () -> shrinkInfo;
        } else {
            return new ShrinkImageTask(body.getWidth(), body.getHeight(), fileInfo);
        }
    }

    private class ShrinkImageTask implements Callable<FileInfo> {
        private final Integer width;
        private final Integer height;
        private final FileInfo fileInfo;

        public ShrinkImageTask(Integer width, Integer height, FileInfo fileInfo) {
            this.width = width;
            this.height = height;
            this.fileInfo = fileInfo;
        }

        @Override
        public FileInfo call() {
            try {
                byte[] bytes = downloadService.downloadToBytes(fileInfo.getId());
                // 压缩图片并保存
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (width > 0 && height > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).size(width, height).keepAspectRatio(false).toOutputStream(outputStream);
                } else if (width > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).width(width).toOutputStream(outputStream);
                } else if (height > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).height(height).toOutputStream(outputStream);
                }
                FileInfo shrinkInfo = fileInfo.clone();
                shrinkInfo.setId(SnowFlake.getInstance().get());
                shrinkInfo.setWidth(width);
                shrinkInfo.setHeight(height);
                uploadService.uploadFile(shrinkInfo, new ByteArrayInputStream(outputStream.toByteArray()));
                // 保存压缩和原图关系
                ShrinkImage imageShrink = new ShrinkImage();
                imageShrink.setFileId(fileInfo.getId());
                imageShrink.setWidth(width);
                imageShrink.setHeight(height);
                imageShrink.setShrinkId(shrinkInfo.getId());
                shrinkCrudService.insert(imageShrink);
                return shrinkInfo;
            } catch (Exception e) {
                throw new SystemException("图片上传失败：" + e.getMessage());
            }
        }
    }
}
