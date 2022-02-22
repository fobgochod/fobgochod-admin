package com.fobgochod.support.deploy;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Linux scp 命令用于 Linux 之间复制文件和目录。
 * scp 是 secure copy 的缩写, scp 是 linux 系统下基于 ssh 登陆进行安全的远程文件拷贝命令。
 * scp 是加密的，rcp 是不加密的，scp 是 rcp 的加强版。
 *
 * @author seven
 * @date 2021/1/15
 */
public class ScpUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScpUtil.class);

    /**
     * 本地文件拷贝到服务器
     *
     * @param session
     * @param source
     * @param destination
     * @return
     */
    public static long scpTo(Session session, String source, String destination) {
        logger.debug(">> cp {} {}", source, destination);
        FileInputStream fis = null;
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            boolean ptimestamp = false;
            String command = "scp";
            if (ptimestamp) {
                command += " -p";
            }
            command += " -t " + destination;
            channel.setCommand(command);
            channel.connect(Shell.CONNECT_TIMEOUT);
            if (checkAck(in) != 0) {
                return -1;
            }
            File _lfile = new File(source);


            if (ptimestamp) {
                command = "T " + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                if (checkAck(in) != 0) {
                    return -1;
                }
            }
            //send "C0644 filesize filename", where filename should not include '/'
            long fileSize = _lfile.length();
            command = "C0644 " + fileSize + " ";
            if (source.lastIndexOf('/') > 0) {
                command += source.substring(source.lastIndexOf('/') + 1);
            } else {
                command += source;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                return -1;
            }
            //send content of file
            fis = new FileInputStream(source);
            byte[] buf = new byte[1024];
            long sum = 0;
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, len);
                sum += len;
            }
            //send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                return -1;
            }
            return sum;
        } catch (JSchException e) {
            logger.error("scp to catched jsch exception, ", e);
        } catch (IOException e) {
            logger.error("scp to catched io exception, ", e);
        } catch (Exception e) {
            logger.error("scp to error, ", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error("File input stream close error, ", e);
                }
            }
        }
        return -1;
    }

    /**
     * 服务器文件拷贝到本地
     *
     * @param session
     * @param source
     * @param destination
     * @return
     */
    public static long scpFrom(Session session, String source, String destination) {
        logger.debug(">> cp {} {}", source, destination);
        FileOutputStream fis = null;
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("scp -f " + source);
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] buf = new byte[1024];
            //send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            while (true) {
                if (checkAck(in) != 'C') {
                    break;
                }
            }
            //read '644 '
            in.read(buf, 0, 4);
            long fileSize = 0;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    break;
                }
                if (buf[0] == ' ') {
                    break;
                }
                fileSize = fileSize * 10L + (long) (buf[0] - '0');
            }
            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            // read a content of lfile
            if (Files.isDirectory(Paths.get(destination))) {
                fis = new FileOutputStream(destination + File.separator + file);
            } else {
                fis = new FileOutputStream(destination);
            }
            long sum = 0;
            while (true) {
                int len = in.read(buf, 0, buf.length);
                if (len <= 0) {
                    break;
                }
                sum += len;
                if (len >= fileSize) {
                    fis.write(buf, 0, (int) fileSize);
                    break;
                }
                fis.write(buf, 0, len);
                fileSize -= len;
            }
            return sum;
        } catch (JSchException e) {
            logger.error("scp to catched jsch exception, ", e);
        } catch (IOException e) {
            logger.error("scp to catched io exception, ", e);
        } catch (Exception e) {
            logger.error("scp to error, ", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error("File output stream close error, ", e);
                }
            }
        }
        return -1;
    }

    /**
     * checkAck
     * <p>
     * b may be
     * 0 for success,
     * 1 for error,
     * 2 for fatal error,
     * -1
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        if (b == 0 || b == -1) {
            return b;
        }

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) {
                // error
                System.out.print(sb.toString());
            }
            if (b == 2) {
                // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }
}
