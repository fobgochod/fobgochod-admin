package com.fobgochod.api.file;

import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.crud.ShrinkCrudService;
import com.fobgochod.domain.ImageHandle;
import com.fobgochod.domain.ImageInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.file.DownloadService;
import com.fobgochod.service.file.FileOpService;
import com.fobgochod.service.file.UploadService;
import com.fobgochod.util.SnowFlake;
import net.coobird.thumbnailator.Thumbnails;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

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
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 按比压缩图片分享地址
     *
     * @param fileId 文件id
     * @param width  缩小宽度(px)
     * @param height 缩小高度(px)
     * @throws Exception 异常信息
     */
    @GetMapping("/buckets/images/{fileId}")
    public ResponseEntity<?> getImage(@PathVariable String fileId,
                                      @RequestParam(defaultValue = "0") Integer width,
                                      @RequestParam(defaultValue = "0") Integer height) throws Exception {
        FileInfo fileInfo = fileOpService.findFileInfo(fileId);
        ImageHandle imageHandle = new ImageHandle(new ImageInfo(width, height), fileInfo);

        FileInfo shrinkInfo;
        ShrinkImage imageShrink = shrinkCrudService.findByProperty(fileId, width, height);
        if (imageShrink != null) {
            shrinkInfo = fileInfoCrudService.findById(imageShrink.getTargetId());
        } else {
            Future<FileInfo> result = threadPoolTaskExecutor.submit(new ShrinkImageTask(imageHandle));
            shrinkInfo = result.get();
        }
        return ResponseEntity.ok(shrinkInfo);
    }

    private class ShrinkImageTask implements Callable<FileInfo> {
        private final ImageHandle imageHandle;

        public ShrinkImageTask(ImageHandle imageHandle) {
            this.imageHandle = imageHandle;
        }

        @Override
        public FileInfo call() {
            try {
                FileInfo fileInfo = imageHandle.getFileInfo();
                ImageInfo imageInfo = imageHandle.getImageInfo();
                if (!imageInfo.shrink()) {
                    // 不压缩，直接获取原图
                    return fileInfoCrudService.findById(fileInfo.getId());
                }
                byte[] bytes = downloadService.downloadToBytes(fileInfo.getId());
                // 压缩图片并保存
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int width = imageInfo.getWidth();
                int height = imageInfo.getHeight();
                if (width > 0 && height > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).size(width, height).keepAspectRatio(false).toOutputStream(outputStream);
                } else if (width > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).width(width).toOutputStream(outputStream);
                } else if (height > 0) {
                    Thumbnails.of(new ByteArrayInputStream(bytes)).height(height).toOutputStream(outputStream);
                }
                FileInfo shrinkInfo = fileInfo.clone();
                shrinkInfo.setId(SnowFlake.getInstance().get());
                uploadService.uploadFile(shrinkInfo, new ByteArrayInputStream(outputStream.toByteArray()));
                // 保存压缩和原图关系
                ShrinkImage imageShrink = new ShrinkImage();
                imageShrink.setSourceId(fileInfo.getId());
                imageShrink.setProperty(new Document().append("width", imageInfo.getWidth()).append("height", imageInfo.getHeight()));
                imageShrink.setTargetId(shrinkInfo.getId());
                shrinkCrudService.insert(imageShrink);
                return shrinkInfo;
            } catch (Exception e) {
                throw new SystemException("图片上传失败：" + e.getMessage());
            }
        }
    }
}
