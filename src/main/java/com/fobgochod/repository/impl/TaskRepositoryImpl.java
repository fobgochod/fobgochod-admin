package com.fobgochod.repository.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
@Repository
public class TaskRepositoryImpl extends BaseEntityRepository<Task> implements TaskRepository {

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public Task findValidTaskByCode(String code) {
        Task task = mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), getEntityClass());
        if (task == null || task.getDisable()) {
            return null;
        }
        return task;
    }

    @Override
    public Task findByClassName(String className) {
        return mongoTemplate.findOne(Query.query(Criteria.where("className").is(className)), getEntityClass());
    }

    @Override
    public boolean lock(String id) {
        Task task = mongoTemplate.findAndModify(Query.query(Criteria.where(BaseField.ID).is(id).and("hash").ne(1)),
                Update.update("hash", 1), getEntityClass());
        return Objects.nonNull(task);
    }

    @Override
    public void unlock(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where(BaseField.ID).is(id)), Update.update("hash", 0), getEntityClass());
    }
}
