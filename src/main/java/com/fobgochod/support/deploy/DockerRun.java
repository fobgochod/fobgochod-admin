package com.fobgochod.support.deploy;

import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动 deploy
 *
 * @author seven
 * @date 2021/1/15
 */
public class DockerRun {

    private static final Logger logger = LoggerFactory.getLogger(DockerRun.class);

    public static void main(String[] args) throws Exception {
        DockerRun.deploy();
    }

    public static void deploy() {
        DockerRun.deploy("doc/run/startup.sh");
    }

    public static void deploy(String source) {
        Session session = null;
        try {
            Shell shell = new Shell();
            session = SshUtil.getSession(shell);
            session.connect(Shell.CONNECT_TIMEOUT);
            if (session.isConnected()) {
                logger.info("Host({}) connected.", shell.getHost());
            }
            SshUtil.exec(session, "mkdir /tmp/deploy");
            ScpUtil.scpTo(session, source, "/tmp/deploy/startup.sh");
            SshUtil.exec(session, "sh /tmp/deploy/startup.sh");
            SshUtil.exec(session, "rm -rf /tmp/deploy");
        } catch (Exception e) {
            logger.error("deploy fail {}", e.getMessage());
        } finally {
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e) {
                    logger.error("JSch channel disconnect error:", e);
                }
            }
        }
    }
}
