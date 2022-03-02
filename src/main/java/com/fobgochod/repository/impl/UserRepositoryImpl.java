package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
@Repository
public class UserRepositoryImpl extends BaseEntityRepository<User> implements UserRepository {

    @Override
    public User findByCode(String code) {
        return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public User findByTelephone(String telephone) {
        return mongoTemplate.findOne(Query.query(Criteria.where("telephone").is(telephone)), getEntityClass());
    }

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public User findByCodeAndPassword(String code, String password) {
        return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code).and("password").is(password)), getEntityClass());
    }
}
