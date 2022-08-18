package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.StatsResult;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 系统统计
 *
 * @author zhouxiao
 * @date 2021/1/26
 * @see TaskIdEnum#TS002
 */
@Component
public class StatsTask extends TaskService {

    private static final Logger logger = LoggerFactory.getLogger(StatsTask.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public void execute() {
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));

        statsRepository.deleteByYearMonth(year, month);
        long start = System.currentTimeMillis();
        StatsResult statsResult = fileInfoCrudService.fileDiskStats();
        List<StatsResult> statsResults = fileInfoCrudService.fileDiskStatsByTenant();

        Stats stats = new Stats();
        stats.setYear(year);
        stats.setMonth(month);
        stats.setUserCount(userRepository.findAll().size());
        stats.setTenantCount(tenantRepository.findAll().size());
        stats.setFileCount(statsResult.getCount());
        stats.setTotalSize(statsResult.getSize());
        stats.setTenants(statsResults);
        statsRepository.insert(stats);
        long end = System.currentTimeMillis();
        logger.info("统计系统使用状况成功，耗时：{}ms.", (end - start));
    }
}
