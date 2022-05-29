package com.fobgochod.api.admin;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stats 统计
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsRepository statsRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        statsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<Stats> body) {
        return ResponseEntity.ok(statsRepository.findByPage(body));
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return ResponseEntity.ok(statsRepository.findNewest());
    }

    @GetMapping("/total")
    public ResponseEntity<?> total() {
        Stats stats = statsRepository.findNewest();

        List<String> tenantSizes = new ArrayList<>();
        List<Map<String, Object>> sizes = new ArrayList<>();
        stats.getTenants().forEach(o -> {
            tenantSizes.add(o.getTenantId());
            Map<String, Object> temp = new HashMap<>();
            temp.put("value", DataUtil.toFixed(o.getSize(), DataUtil.BIG_MB));
            temp.put("name", o.getTenantId());
            sizes.add(temp);
        });


        List<String> tenantFiles = new ArrayList<>();
        List<Long> files = new ArrayList<>();
        stats.getTenants().forEach(o -> {
            tenantFiles.add(o.getTenantId());
            files.add(o.getCount());
        });

        Map<String, Object> result = new HashMap<>();
        result.put("tenantSizes", tenantSizes);
        result.put("sizes", sizes);
        result.put("tenantFiles", tenantFiles);
        result.put("files", files);
        return ResponseEntity.ok(result);
    }
}
