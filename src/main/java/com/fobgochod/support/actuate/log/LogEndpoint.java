package com.fobgochod.support.actuate.log;

import com.fobgochod.support.actuate.util.LogUtils;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

@WebEndpoint(id = "log")
public class LogEndpoint {

    @ReadOperation
    public Map<String, List<String>> logs() {
        return LogUtils.getFileLists();
    }

    @ReadOperation(produces = "text/plain; charset=UTF-8")
    public Resource log(@Selector String level) {
        return log(level, null);
    }

    @ReadOperation(produces = "text/plain; charset=UTF-8")
    public Resource log(@Selector String level, @Selector String filename) {
        Resource logFileResource = LogUtils.getLogFileResource(level, filename);
        if (logFileResource == null || !logFileResource.isReadable()) {
            return null;
        }
        return logFileResource;
    }
}
