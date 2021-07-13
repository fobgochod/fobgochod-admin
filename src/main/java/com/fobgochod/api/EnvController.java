package com.fobgochod.api;

import com.fobgochod.domain.base.EnvProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@RestController
public class EnvController {

    @Autowired
    private EnvProperties envProperties;

    @GetMapping(value = "/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(new LinkedHashMap<String, String>(4) {{
            put("version", envProperties.getVersion());
            put("build-time", envProperties.getBuildTime());
            put("server-time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            this.put("profiles-active", envProperties.getActive());
        }});
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        envProperties.refresh();
        return ResponseEntity.ok(envProperties);
    }
}
