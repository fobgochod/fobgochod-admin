package com.fobgochod.api.file;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.ImageHandle;
import com.fobgochod.domain.ImageInfo;
import com.fobgochod.domain.StdData;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.service.business.FileService;
import com.fobgochod.service.business.ShareService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.ShareCrudService;
import com.fobgochod.service.client.ShrinkCrudService;
import com.fobgochod.service.mongo.FileStorage;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.SteamUtil;
import com.fobgochod.util.UserUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@RestController
public class FileImageController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShareService shareService;
    @Autowired
    private ShareCrudService shareCrudService;
    @Autowired
    private ShrinkCrudService shrinkCrudService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 上传并压缩图片
     *
     * @param shrink   是否缩小 默认否
     * @param width    宽
     * @param height   高
     * @param fileJson 文件信息
     * @param req      输入流
     * @return
     * @throws Exception
     */
    @PostMapping("/buckets/images/upload")
    public ResponseEntity<?> uploadImage(@RequestParam(defaultValue = "0") Boolean shrink,
                                         @RequestParam(defaultValue = "0") Integer width,
                                         @RequestParam(defaultValue = "0") Integer height,
                                         @RequestHeader(value = FghConstants.HTTP_HEADER_API_ARG_KEY) String fileJson,
                                         HttpServletRequest req) throws Exception {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setWidth(width);
        imageInfo.setHeight(height);
        imageInfo.setShrink(shrink);
        ImageHandle imageHandle = new ImageHandle(imageInfo, FileInfo.get(fileJson));
        Future<Map<String, String>> result = threadPoolTaskExecutor.submit(new UploadImageTask(imageHandle, req.getInputStream(), UserUtil.getAuthentication()));
        return ResponseEntity.ok(StdData.ofSuccess(result.get()));
    }

    /**
     * 按比例获取图片分享地址
     *
     * @param fileId 文件id
     * @param shrink 是否缩小 默认否
     * @param share  是否分享 默认否
     * @param width  缩小宽度(px)
     * @param height 缩小高度(px)
     * @throws Exception 异常信息
     */
    @GetMapping("/buckets/images/{fileId}")
    public ResponseEntity<?> getImage(@PathVariable String fileId,
                                      @RequestParam(defaultValue = "1") Boolean shrink,
                                      @RequestParam(defaultValue = "1") Boolean share,
                                      @RequestParam(defaultValue = "0") Integer width,
                                      @RequestParam(defaultValue = "0") Integer height) throws Exception {
        FileInfo fileInfo = fileInfoCrudService.findById(fileId);
        if (fileInfo == null) {
            throw new BusinessException(String.format("图片[%s]不存在", fileId));
        }
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setWidth(width);
        imageInfo.setHeight(height);
        imageInfo.setShrink(shrink);
        ImageHandle imageHandle = new ImageHandle(imageInfo, fileInfo);

        Map<String, Object> map = new LinkedHashMap<>();
        ShrinkImage imageShrink = shrinkCrudService.findByProperty(fileId, width, height);
        if (imageShrink != null) {
            FileInfo shrinkInfo = fileInfoCrudService.findById(imageShrink.getTargetId());
            List<ShareRecord> sharedFiles = shareCrudService.getShareFileByFileId(imageShrink.getTargetId());
            if (shrinkInfo != null && !sharedFiles.isEmpty()) {
                map.put("fileId", shrinkInfo.getId());
                map.put("directoryId", shrinkInfo.getDirectoryId());
                map.put("fileName", shrinkInfo.getName());
                map.put("extension", shrinkInfo.getExtension());
                map.put("contentType", shrinkInfo.getContentType());
                map.put("size", shrinkInfo.getSize());
                map.put("shareUrl", sharedFiles.get(0).getUrl());
                map.put("shareDate", sharedFiles.get(0).getCreateDate());
                map.put("property", new Document().append("width", imageInfo.getWidth()).append("height", imageInfo.getHeight()));
            }
        } else {
            Future<Map<String, Object>> result = threadPoolTaskExecutor.submit(new ShrinkImageTask(imageHandle, UserUtil.getAuthentication()));
            map = result.get();
        }
        if (!share) {
            map.remove("shareUrl");
            map.remove("shareDate");
        }
        return ResponseEntity.ok(StdData.ofSuccess(map));
    }

    /**
     * 根据原图id获取压缩图片分享地址
     *
     * @param fileId 文件id
     * @param shrink 是否缩小 默认否
     */
    @GetMapping("/buckets/images/{fileId}/{shrink}")
    public ResponseEntity<?> getImage(@PathVariable String fileId,
                                      @PathVariable Boolean shrink) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileId);
        if (fileInfo == null) {
            throw new BusinessException(String.format("图片[%s]不存在", fileId));
        }
        List<Map<String, Object>> result = new ArrayList<>();
        if (shrink) {
            List<ShrinkImage> imageShrinks = shrinkCrudService.findBySourceId(fileId);
            imageShrinks.forEach(p -> {
                FileInfo shrinkInfo = fileInfoCrudService.findById(p.getTargetId());
                List<ShareRecord> sharedFiles = shareCrudService.getShareFileByFileId(p.getTargetId());
                sharedFiles.forEach(o -> {
                    Map<String, Object> temp = new LinkedHashMap<>(10);
                    temp.put("directoryId", shrinkInfo.getDirectoryId());
                    temp.put("fileName", shrinkInfo.getName());
                    temp.put("extension", shrinkInfo.getExtension());
                    temp.put("contentType", shrinkInfo.getContentType());
                    temp.put("size", shrinkInfo.getSize());
                    temp.put("shareUrl", o.getUrl());
                    temp.put("shareDate", o.getCreateDate());
                    temp.put("property", p.getProperty());
                    result.add(temp);
                });
            });
        } else {
            List<ShareRecord> sharedFiles = shareCrudService.getShareFileByFileId(fileInfo.getId());
            sharedFiles.forEach(o -> {
                Map<String, Object> temp = new LinkedHashMap<>(10);
                temp.put("directoryId", fileInfo.getDirectoryId());
                temp.put("fileName", fileInfo.getName());
                temp.put("extension", fileInfo.getExtension());
                temp.put("contentType", fileInfo.getContentType());
                temp.put("size", fileInfo.getSize());
                temp.put("shareUrl", o.getUrl());
                temp.put("shareDate", o.getCreateDate());
                temp.put("property", Collections.emptyMap());
                result.add(temp);
            });
        }
        return ResponseEntity.ok(StdData.ofSuccess(result));
    }

    private class UploadImageTask implements Callable<Map<String, String>> {
        private final ImageHandle imageHandle;
        private final InputStream inputStream;
        private final Authentication authentication;

        public UploadImageTask(ImageHandle imageHandle, InputStream inputStream, Authentication authentication) {
            this.imageHandle = imageHandle;
            this.inputStream = inputStream;
            this.authentication = authentication;
        }

        @Override
        public Map<String, String> call() {
            UserUtil.setAuthentication(authentication);
            try {
                FileInfo fileInfo = imageHandle.getFileInfo();
                ImageInfo imageInfo = imageHandle.getImageInfo();
                String fileId;
                String originUrl;
                String shrinkId;
                String shrinkUrl;
                boolean flag = !imageInfo.isShrink() || (imageInfo.getWidth() <= 0 && imageInfo.getHeight() <= 0);
                if (flag) {
                    // 上传图片
                    fileId = fileService.uploadFileStream(fileInfo, inputStream);
                    originUrl = shareService.shareFileForever(fileInfo);
                    shrinkId = fileId;
                    shrinkUrl = originUrl;
                } else {
                    // 打开两个新的输入流
                    ByteArrayOutputStream output = SteamUtil.cloneInputStream(inputStream);
                    InputStream inputStream1 = new ByteArrayInputStream(output.toByteArray());
                    InputStream inputStream2 = new ByteArrayInputStream(output.toByteArray());

                    // 上传图
                    FileInfo originFileInfo = fileInfo.clone();
                    fileId = fileService.uploadFileStream(originFileInfo, inputStream1);
                    // 分享原图
                    originUrl = shareService.shareFileForever(originFileInfo);

                    // 上传压缩后的图片
                    FileInfo shrinkFileInfo = fileInfo.clone();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int width = imageInfo.getWidth();
                    int height = imageInfo.getHeight();
                    if (width > 0 && height > 0) {
                        Thumbnails.of(inputStream2).size(width, height).keepAspectRatio(false).toOutputStream(outputStream);
                    } else if (width > 0) {
                        Thumbnails.of(inputStream2).width(width).toOutputStream(outputStream);
                    } else if (height > 0) {
                        Thumbnails.of(inputStream2).height(height).toOutputStream(outputStream);
                    }
                    shrinkId = fileService.uploadFileStream(shrinkFileInfo, new ByteArrayInputStream(outputStream.toByteArray()));
                    // 分享压缩图片
                    shrinkUrl = shareService.shareFileForever(shrinkFileInfo);

                    // 保存压缩和原图关系
                    ShrinkImage imageShrink = new ShrinkImage();
                    imageShrink.setSourceId(originFileInfo.getId().toString());
                    imageShrink.setProperty(new Document().append("width", imageInfo.getWidth()).append("height", imageInfo.getHeight()));
                    imageShrink.setTargetId(shrinkId);
                    shrinkCrudService.insert(imageShrink);
                }
                Map<String, String> map = new LinkedHashMap<>();
                map.put("fileId", fileId);
                map.put("originUrl", originUrl);
                map.put("shrinkId", shrinkId);
                map.put("shrinkUrl", shrinkUrl);
                return map;
            } catch (Exception e) {
                throw new BusinessException("图片上传失败", e);
            }
        }
    }

    private class ShrinkImageTask implements Callable<Map<String, Object>> {
        private final ImageHandle imageHandle;
        private final Authentication authentication;

        public ShrinkImageTask(ImageHandle imageHandle, Authentication authentication) {
            this.imageHandle = imageHandle;
            this.authentication = authentication;
        }

        @Override
        public Map<String, Object> call() {
            UserUtil.setAuthentication(authentication);
            try {
                Map<String, Object> map = new LinkedHashMap<>();

                FileInfo fileInfo = imageHandle.getFileInfo();
                ImageInfo imageInfo = imageHandle.getImageInfo();
                boolean flag = !imageInfo.isShrink() || (imageInfo.getWidth() <= 0 && imageInfo.getHeight() <= 0);
                if (flag) {
                    // 不压缩，直接获取原图
                    List<ShareRecord> sharedFiles = shareCrudService.getShareFileByFileId(fileInfo.getId().toString());
                    if (!sharedFiles.isEmpty()) {
                        map.put("fileId", fileInfo.getId());
                        map.put("directoryId", fileInfo.getDirectoryId());
                        map.put("fileName", fileInfo.getName());
                        map.put("extension", fileInfo.getExtension());
                        map.put("contentType", fileInfo.getContentType());
                        map.put("size", fileInfo.getSize());
                        map.put("shareUrl", sharedFiles.get(0).getUrl());
                        map.put("shareDate", sharedFiles.get(0).getCreateDate());
                        map.put("property", Collections.emptyMap());
                    }
                } else {
                    byte[] bytes = fileStorage.downloadToBytes(fileInfo.getFileId().toString());
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
                    String shrinkId = fileService.uploadFileStream(shrinkInfo, new ByteArrayInputStream(outputStream.toByteArray()));
                    // 分享图片
                    String shrinkUrl = shareService.shareFileForever(shrinkInfo);
                    // 保存压缩和原图关系
                    ShrinkImage imageShrink = new ShrinkImage();
                    imageShrink.setSourceId(fileInfo.getId());
                    imageShrink.setProperty(new Document().append("width", imageInfo.getWidth()).append("height", imageInfo.getHeight()));
                    imageShrink.setTargetId(shrinkId);
                    shrinkCrudService.insert(imageShrink);

                    map.put("fileId", shrinkInfo.getId());
                    map.put("directoryId", shrinkInfo.getDirectoryId());
                    map.put("fileName", shrinkInfo.getName());
                    map.put("extension", shrinkInfo.getExtension());
                    map.put("contentType", shrinkInfo.getContentType());
                    map.put("size", shrinkInfo.getSize());
                    map.put("shareUrl", shrinkUrl);
                    map.put("shareDate", sdf.format(new Date()));
                    map.put("property", new Document().append("width", imageInfo.getWidth()).append("height", imageInfo.getHeight()));
                }
                return map;
            } catch (Exception e) {
                throw new BusinessException("图片上传失败", e);
            }
        }
    }
}
