package com.fobgochod;

import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.BucketRepository;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.util.AESCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class FghListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        if (!userRepository.existsByCode("admin")) {
            User user = new User();
            user.setPassword(AESCipher.getSHA256("fobgochod"));
            user.setCode("admin");
            user.setName("管理员");
            user.setEmail("admin@noneboy.com");
            user.setTelephone("16800000001");
            user.setRole(RoleEnum.Admin);
            userRepository.insert(user);
        }
        if (!userRepository.existsByCode("zhouxx")) {
            User user = new User();
            user.setCode("zhouxx");
            user.setName("周萧");
            user.setEmail("zhouxx@noneboy.com");
            user.setTelephone("16800000002");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.Admin);
            userRepository.insert(user);
        }
        if (!userRepository.existsByCode("zhoux")) {
            User user = new User();
            user.setCode("zhoux");
            user.setName("萧周");
            user.setEmail("zhoux@noneboy.com");
            user.setTelephone("16800000002");
            user.setPassword(AESCipher.getSHA256("test"));
            user.setRole(RoleEnum.Owner);
            userRepository.insert(user);
        }

        if (!tenantRepository.existsByCode("fobgochod")) {
            Tenant tenant = new Tenant();
            tenant.setCode("fobgochod");
            tenant.setName("山有扶苏");
            tenant.setEmail("admin@noneboy.com");
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
            tenant.setOwner("zhouxx");
            tenantRepository.insert(tenant);
        }

        if (!bucketRepository.existsByCode("ddd")) {
            Bucket tenant = new Bucket();
            tenant.setCode("ddd");
            tenant.setName("领域驱动设计");
            tenant.setOwner("zhouxx");
            tenant.setTask(TaskIdEnum.TS001.name());
            bucketRepository.insert(tenant);
        }
    }
}
