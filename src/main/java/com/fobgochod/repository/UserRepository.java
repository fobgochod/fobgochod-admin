package com.fobgochod.repository;

import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface UserRepository extends EntityRepository<User> {

    User findByCode(@Param("code") String code);

    User findByTelephone(@Param("telephone") String telephone);

    boolean existsByCode(@Param("code") String code);

    User findByCodeAndPassword(@Param("code") String code, @Param("password") String password);
}
