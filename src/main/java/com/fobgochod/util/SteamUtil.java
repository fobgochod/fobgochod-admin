package com.fobgochod.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SteamUtil {

    private static final Logger logger = LoggerFactory.getLogger(SteamUtil.class);

    public static ByteArrayOutputStream cloneInputStream(InputStream input) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                stream.write(buffer, 0, len);
            }
            stream.flush();
        } catch (Exception e) {
            logger.error("输入流转输出失败", e);
        }
        return stream;
    }
}
