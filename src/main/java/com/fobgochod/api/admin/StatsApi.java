package com.fobgochod.api.admin;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.v2.BucketStats;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stats 统计
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/stats")
public class StatsApi {

    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        statsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(statsRepository.findByPage(body));
    }

    @GetMapping("/increment/{prev}/{next}")
    public ResponseEntity<?> increment(@PathVariable String prev,
                                       @PathVariable String next) {
        return ResponseEntity.ok(statsRepository.getBucketInc(prev, next));
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return ResponseEntity.ok(statsRepository.findNewest());
    }

    @GetMapping("/total")
    public ResponseEntity<?> total(@RequestParam(value = "limit", defaultValue = "10") Long limit) {
        List<FileInfo> fileInfos = fileInfoCrudService.findAll();
        Map<String, List<FileInfo>> fileMap = fileInfos.stream().collect(Collectors.groupingBy(FileInfo::getTenantId));
        List<BucketStats> buckets = new ArrayList<>();

        for (Map.Entry<String, List<FileInfo>> entry : fileMap.entrySet()) {
            BucketStats stats = new BucketStats();
            stats.setName(StringUtils.hasText(entry.getKey()) ? entry.getKey() : FghConstants.DEFAULT_TENANT);
            stats.setSize(entry.getValue().stream().mapToLong(FileInfo::getSize).sum());
            stats.setFiles((long) entry.getValue().size());
            buckets.add(stats);
        }

        List<BucketStats> sizeStats = buckets.stream().sorted(Comparator.comparing(BucketStats::getSize).reversed()).limit(limit).collect(Collectors.toList());

        List<String> tenantSizes = new ArrayList<>();
        List<Map<String, Object>> sizes = new ArrayList<>();

        sizeStats.forEach(o -> {
            tenantSizes.add(o.getName());
            Map<String, Object> temp = new HashMap<>();
            temp.put("value", DataUtil.toFixed(o.getSize(), DataUtil.BIG_MB));
            temp.put("name", o.getName());
            sizes.add(temp);
        });

        List<BucketStats> countStats = buckets.stream().sorted(Comparator.comparing(BucketStats::getFiles).reversed()).limit(limit).collect(Collectors.toList());

        List<String> tenantFiles = new ArrayList<>();
        List<Long> files = new ArrayList<>();

        countStats.forEach(o -> {
            tenantFiles.add(o.getName());
            files.add(o.getFiles());
        });

        Map<String, Object> result = new HashMap<>();
        result.put("tenantSizes", tenantSizes);
        result.put("sizes", sizes);
        result.put("tenantFiles", tenantFiles);
        result.put("files", files);
        return ResponseEntity.ok(result);
    }
}
