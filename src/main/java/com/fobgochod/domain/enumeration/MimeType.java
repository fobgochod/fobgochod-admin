package com.fobgochod.domain.enumeration;

/**
 * 视频格式
 *
 * @author zhouxiao
 * @date 2021/3/9
 * @see org.springframework.boot.web.server.MimeMappings
 */
public class MimeType {
    /**
     * MP4
     */
    public static final String VIDEO_MP4 = "video/mp4";
    /**
     * WebM
     */
    public static final String VIDEO_WEBM = "video/webm";
    /**
     * Ogg
     */
    public static final String VIDEO_OGG = "video/ogg";

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_BMP = "image/bmp";
    public static final String IMAGE_GIF = "image/gif";

    public static boolean isVideo(String type) {
        return type != null && (type.contains(VIDEO_MP4)
                || type.contains(VIDEO_WEBM)
                || type.contains(VIDEO_OGG));
    }

    public static boolean isImage(String type) {
        return type != null && (type.contains(IMAGE_JPEG)
                || type.contains(IMAGE_PNG)
                || type.contains(IMAGE_BMP)
                || type.contains(IMAGE_GIF));
    }
}
