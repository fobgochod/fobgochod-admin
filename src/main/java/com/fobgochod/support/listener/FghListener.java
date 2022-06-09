package com.fobgochod.support.listener;

import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.entity.admin.Role;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.RoleRepository;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.util.SecureUtils;
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

@Component
class FghListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(FghListener.class);
    private ApplicationContext applicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TenantRepository tenantRepository;

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
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, app, " actuator at           ", actuatorUrl));
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, app, " environment at        ", envUrl));
        logger.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, app, " has started successfully!"));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        this.initUser();
        this.initRole();
        this.initTenant();
        this.printStartInfo();
    }

    private void initUser() {
        if (!userRepository.existsByCode("admin")) {
            User user = new User();
            user.setCode("admin");
            user.setName("管理员");
            user.setTelephone("16800000001");
            user.setEmail("admin@nobody.com");
            user.setPassword(SecureUtils.sha256("vivi229229"));
            user.setRole(RoleEnum.Admin.name());
            userRepository.insert(user);
        }
    }

    private void initRole() {
        if (!roleRepository.existsByCode(RoleEnum.Admin.name())) {
            Role role = new Role();
            role.setCode(RoleEnum.Admin.name());
            role.setName("管理员");
            role.setOrder(1L);
            roleRepository.insert(role);
        }
    }

    private void initTenant() {
        if (!tenantRepository.existsByCode("fobgochod")) {
            Tenant tenant = new Tenant();
            tenant.setCode("fobgochod");
            tenant.setName("山有扶苏");
            tenant.setEmail("fobgochod@nobody.com");
            tenant.setTelephone("16800000001");
            tenant.setOwner("admin");
            tenantRepository.insert(tenant);
        }
    }
}
