package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.base.EnvProperties;
import com.fobgochod.domain.BucketStats;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import com.fobgochod.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Bucket 总量统计
 *
 * @author zhouxiao
 * @date 2021/1/26
 */
@Component
public class StatsTask extends TaskService {

    private static final Logger logger = LoggerFactory.getLogger(StatsTask.class);

    @Autowired
    private EnvProperties env;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private StatsRepository statsRepository;

    @Override
    public void execute() {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.ST001.name());
        if (task != null) {
            statsRepository.deleteByYearMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
            long start = System.currentTimeMillis();
            BucketStats bucketStats = statsRepository.getBucketStats(env.getDatabase());
            Stats stats = new Stats();
            stats.setYear(LocalDate.now().getYear());
            stats.setMonth(LocalDate.now().getMonthValue());
            stats.setUserCount(userRepository.findAll().size());
            stats.setTenantCount(tenantRepository.findAll().size());
            stats.setFileCount(bucketStats.getFiles());
            stats.setTotalSize(bucketStats.getSize());
            stats.setTotalSizeReadable(DataUtil.byteSwitch(stats.getTotalSize()));
            statsRepository.insert(stats);
            long end = System.currentTimeMillis();
            logger.info("统计系统使用状况成功，耗时：{}ms.", (end - start));
        }
    }
}
