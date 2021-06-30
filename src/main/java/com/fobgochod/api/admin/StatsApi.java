package com.fobgochod.api.admin;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.BucketStats;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public StdData stats() {
        return StdData.ofSuccess(statsRepository.findAll());
    }

    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(statsRepository.findByPage(body));
    }

    @GetMapping("/increment/{prev}/{next}")
    public StdData increment(@PathVariable String prev,
                             @PathVariable String next) {
        return StdData.ofSuccess(statsRepository.getBucketInc(prev, next));
    }

    @DeleteMapping("/{id}")
    public StdData delete(@PathVariable String id) {
        statsRepository.deleteById(id);
        return StdData.ok();
    }

    @GetMapping("/count")
    public StdData count() {
        return StdData.ofSuccess(statsRepository.findNewest());
    }

    @GetMapping("/total")
    public StdData total(@RequestParam(value = "limit", defaultValue = "10") Long limit) {
        Stats stats = statsRepository.findNewest();
        List<BucketStats> buckets = stats.getBuckets();

        List<BucketStats> buckets1 = buckets.stream().sorted(Comparator.comparing(BucketStats::getSize).reversed()).limit(limit).collect(Collectors.toList());

        List<String> bucketSizes = new ArrayList<>();
        List<Map<String, Object>> sizes = new ArrayList<>();

        buckets1.forEach(o -> {
            bucketSizes.add(o.getName());
            Map<String, Object> temp = new HashMap<>();
            temp.put("value", DataUtil.byte2Gb(o.getSize()));
            temp.put("name", o.getName());
            sizes.add(temp);
        });

        List<BucketStats> buckets2 = buckets.stream().sorted(Comparator.comparing(BucketStats::getFiles).reversed()).limit(limit).collect(Collectors.toList());

        List<String> bucketFiles = new ArrayList<>();
        List<Long> files = new ArrayList<>();

        buckets2.forEach(o -> {
            bucketFiles.add(o.getName());
            files.add(o.getFiles());
        });

        Map<String, Object> result = new HashMap<>();
        result.put("bucketSizes", bucketSizes);
        result.put("sizes", sizes);
        result.put("bucketFiles", bucketFiles);
        result.put("files", files);
        return StdData.ofSuccess(result);
    }
}
