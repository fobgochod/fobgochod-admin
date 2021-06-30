package com.deploy;

import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取镜像
 *
 * @author seven
 * @date 2021/1/15
 */
public class DockerImage {

    private static final Logger logger = LoggerFactory.getLogger(DockerImage.class);

    public static void main(String[] args) throws Exception {
        String project = "fobgochod-admin";
        String version = "1.0.0";
        DockerImage.build(project, version);
    }

    public static void build(String project, String version) {
        Session session = null;
        try {
            Shell shell = new Shell();
            session = SshUtil.getSession(shell);
            session.connect(Shell.CONNECT_TIMEOUT);
            if (session.isConnected()) {
                logger.info("Host({}) connected.", shell.getHost());
            }
            SshUtil.exec(session, "mkdir /tmp/image");
            SshUtil.exec(session, String.format("docker save -o /tmp/image/%s-%s.tar %s:%s", project, version, project, version));
            ScpUtil.scpFrom(session, String.format("/tmp/image/%s-%s.tar", project, version), String.format("doc/%s-%s.tar", project, version));
            SshUtil.exec(session, "rm -rf /tmp/image");
        } catch (Exception e) {
            logger.error("build fail {}", e.getMessage());
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
