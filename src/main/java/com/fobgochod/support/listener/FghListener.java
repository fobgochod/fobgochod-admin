package com.fobgochod.support.listener;

import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.entity.admin.*;
import com.fobgochod.repository.*;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskManager;
import com.fobgochod.service.schedule.impl.BirthdayTask;
import com.fobgochod.service.schedule.impl.MedicineTask;
import com.fobgochod.service.schedule.impl.StatsTask;
import com.fobgochod.service.schedule.impl.TestTask;
import com.fobgochod.util.AESCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.time.LocalTime;

@Component
class FghListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(FghListener.class);
    private ApplicationContext applicationContext;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printStartInfo() {
        String app = this.applicationContext.getEnvironment().getProperty("spring.application.name");
        Integer port = this.applicationContext.getBean(ServerProperties.class).getPort();
        String url = "http://127.0.0.1:" + port;
        String actuatorUrl = url + "/actuator";
        String envUrl = url + "/env";
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, app, " started at            ", url));
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, app, " actuator at        ", actuatorUrl));
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, app, " environment at            ", envUrl));
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, app, " has started successfully!"));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        this.initUser();
        this.initTenant();
        this.initMedicine();
        this.initScheduled();
        this.printStartInfo();
    }

    private void initUser() {
        if (!userRepository.existsByCode("admin")) {
            User user = new User();
            user.setCode("admin");
            user.setName("管理员");
            user.setTelephone("16800000001");
            user.setEmail("admin@noneboy.com");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.Admin);
            userRepository.insert(user);
        }
        if (!userRepository.existsByCode("test")) {
            User user = new User();
            user.setCode("test");
            user.setName("测试员");
            user.setTelephone("16800000002");
            user.setEmail("test@noneboy.com");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.None);
            userRepository.insert(user);
        }
        if (!userRepository.existsByCode("zhouxiao")) {
            User user = new User();
            user.setCode("zhouxiao");
            user.setName("周萧");
            user.setTelephone("18255396069");
            user.setWechat("fobgochod");
            user.setEmail("zhouxiao@noneboy.com");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.Owner);
            userRepository.insert(user);
        }
        if (!userRepository.existsByCode("chentt")) {
            User user = new User();
            user.setCode("chentt");
            user.setName("陈甜甜");
            user.setTelephone("15371028040");
            user.setWechat("cherry_sweet0127");
            user.setEmail("chentt@noneboy.com");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.Owner);
            userRepository.insert(user);
        }
    }

    private void initTenant() {
        if (!tenantRepository.existsByCode("fobgochod")) {
            Tenant tenant = new Tenant();
            tenant.setCode("fobgochod");
            tenant.setName("山有扶苏");
            tenant.setEmail("fobgochod@noneboy.com");
            tenant.setTelephone("16800000001");
            tenant.setOwner("admin");
            tenantRepository.insert(tenant);
        }
        if (!tenantRepository.existsByCode("seven")) {
            Tenant tenant = new Tenant();
            tenant.setCode("seven");
            tenant.setName("隰有荷华");
            tenant.setEmail("seven@noneboy.com");
            tenant.setTelephone("16800000002");
            tenant.setOwner("zhouxiao");
            tenantRepository.insert(tenant);
        }
    }

    private void initMedicine() {
        if (!medicineRepository.existsByCode("C001")) {
            Medicine medicine = new Medicine();
            medicine.setUserId("chentt");
            medicine.setCode("C001");
            medicine.setName("水飞蓟宾葡甲胺片");
            medicine.setTotal(140f);
            medicine.setMorning(0f);
            medicine.setNoon(2f);
            medicine.setNight(2f);
            medicineRepository.insert(medicine);

            MedicineRecord record = new MedicineRecord();
            record.setMedicineId(medicine.getId());
            record.setType("入");
            record.setSlice(140f);
            record.setTime(LocalTime.now());
            medicineRecordRepository.insert(record);
        }
        if (!medicineRepository.existsByCode("C002")) {
            Medicine medicine = new Medicine();
            medicine.setUserId("chentt");
            medicine.setCode("C002");
            medicine.setName("碳酸锂缓释片");
            medicine.setTotal(100f);
            medicine.setMorning(0f);
            medicine.setNoon(1f);
            medicine.setNight(1f);
            medicineRepository.insert(medicine);
        }
        if (!medicineRepository.existsByCode("C003")) {
            Medicine medicine = new Medicine();
            medicine.setUserId("chentt");
            medicine.setCode("C003");
            medicine.setName("富马酸喹硫平缓释片");
            medicine.setTotal(120f);
            medicine.setMorning(0f);
            medicine.setNoon(1f);
            medicine.setNight(2f);
            medicineRepository.insert(medicine);
        }
    }

    private void initScheduled() {
        boolean ts001 = taskRepository.existsByCode(TaskIdEnum.TS001.name());
        if (!ts001) {
            Task task = new Task();
            task.setType("测试");
            task.setCode(TaskIdEnum.TS001.name());
            task.setName("测试Cron表达式");
            task.setCron("0/10 * * * * ?");
            task.setClassName(Introspector.decapitalize(TestTask.class.getSimpleName()));
            task.setDisable(true);
            task.setRemark("Cron测试");
            taskRepository.insert(task);
        }

        boolean ts002 = taskRepository.existsByCode(TaskIdEnum.TS002.name());
        if (!ts002) {
            Task task = new Task();
            task.setType("统计");
            task.setCode(TaskIdEnum.TS002.name());
            task.setName("系统使用状况统计");
            task.setCron("0 0 1 1/1 * ?");
            task.setClassName(Introspector.decapitalize(StatsTask.class.getSimpleName()));
            task.setDisable(false);
            task.setRemark("Bucket统计");
            taskRepository.insert(task);
        }

        boolean ts003 = taskRepository.existsByCode(TaskIdEnum.TS003.name());
        if (!ts003) {
            Task task = new Task();
            task.setType("文件");
            task.setCode(TaskIdEnum.TS003.name());
            task.setName("过期文件移动到回收站");
            task.setCron("0 0 1 1/1 * ?");
            task.setClassName(Introspector.decapitalize(StatsTask.class.getSimpleName()));
            task.setDisable(false);
            task.setRemark("进回收站");
            taskRepository.insert(task);
        }

        boolean ts004 = taskRepository.existsByCode(TaskIdEnum.TS004.name());
        if (!ts004) {
            Task task = new Task();
            task.setType("文件");
            task.setCode(TaskIdEnum.TS004.name());
            task.setName("回收站文件删除");
            task.setCron("0 0 1 1/1 * ?");
            task.setClassName(Introspector.decapitalize(StatsTask.class.getSimpleName()));
            task.setDisable(false);
            task.setRemark("永久删除");
            taskRepository.insert(task);
        }

        boolean ts005 = taskRepository.existsByCode(TaskIdEnum.TS005.name());
        if (!ts005) {
            Task task = new Task();
            task.setType("生活");
            task.setCode(TaskIdEnum.TS005.name());
            task.setName("吃药提醒");
            task.setCron("0 0 0/1 * * ?");
            task.setClassName(Introspector.decapitalize(MedicineTask.class.getSimpleName()));
            task.setDisable(false);
            task.setRemark("吃药提醒");
            taskRepository.insert(task);
        }

        boolean ts006 = taskRepository.existsByCode(TaskIdEnum.TS006.name());
        if (!ts006) {
            Task task = new Task();
            task.setType("生活");
            task.setCode(TaskIdEnum.TS006.name());
            task.setName("生日提醒");
            task.setCron("0 0 9 1/1 * ?");
            task.setClassName(Introspector.decapitalize(BirthdayTask.class.getSimpleName()));
            task.setDisable(false);
            task.setRemark("生日提醒");
            taskRepository.insert(task);
        }
        taskManager.refresh();
        logger.info(AnsiOutput.toString(AnsiColor.GREEN, "Task schedule has init successfully!"));
    }
}
