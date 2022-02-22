package com.fobgochod.support.actuate.logging;

import com.fobgochod.support.actuate.util.LogUtils;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.boot.logging.LogFile;
import org.springframework.core.io.Resource;

import java.io.File;

@WebEndpoint(id = "logfile")
public class LogFileEndpoint extends LogFileWebEndpoint {

    public LogFileEndpoint(LogFile logFile, File externalFile) {
        super(logFile, externalFile);
    }

    @Override
    @ReadOperation(produces = "text/plain; charset=UTF-8")
    public Resource logFile() {
        Resource logFileResource = LogUtils.getLogFileResource("error");
        if (logFileResource == null || !logFileResource.isReadable()) {
            return null;
        }
        return logFileResource;
    }

    @ReadOperation(produces = "text/plain; charset=UTF-8")
    public Resource logFile(@Selector String level) {
        Resource logFileResource = LogUtils.getLogFileResource(level);
        if (logFileResource == null || !logFileResource.isReadable()) {
            return null;
        }
        return logFileResource;
    }
}
