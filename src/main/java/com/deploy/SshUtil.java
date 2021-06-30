package com.deploy;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接SSH
 * 执行命令Linux命令
 *
 * @author seven
 * @date 2021/1/15
 */
public class SshUtil {

    private static final Logger logger = LoggerFactory.getLogger(SshUtil.class);

    /**
     * 建立连接
     *
     * @param shell
     * @return
     * @throws JSchException
     */
    public static Session getSession(Shell shell) throws JSchException {
        JSch jSch = new JSch();
        if (Files.exists(Paths.get(shell.getIdentity()))) {
            jSch.addIdentity(shell.getIdentity(), shell.getPassphrase());
        }
        Session session = jSch.getSession(shell.getUsername(), shell.getHost(), shell.getPort());
        session.setPassword(shell.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    /**
     * 执行命令
     *
     * @param session
     * @param command
     * @return
     * @throws JSchException
     */
    public static List<String> exec(Session session, String command) throws JSchException {
        logger.debug(">> {}", command);
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream is = channel.getInputStream();
            channel.connect(Shell.CONNECT_TIMEOUT);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
                    logger.debug("   {}", inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        logger.error("JSch inputStream close error:", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("IOException:", e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    logger.error("JSch channel disconnect error:", e);
                }
            }
        }
        return resultLines;
    }
}
