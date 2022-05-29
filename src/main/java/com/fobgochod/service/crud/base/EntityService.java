package com.fobgochod.service.crud.base;

import com.fobgochod.entity.BaseEntity;
import com.fobgochod.repository.base.CrudRegistry;
import org.bson.conversions.Bson;

public interface EntityService<T extends BaseEntity> extends CrudRegistry<T> {

    /**
     * 虚拟删除
     *
     * @param id 文件ID
     */
    void removeById(String id);

    void restoreById(String id);

    /**
     * 保存实体，不添加基础字段数据
     *
     * @param data 实体
     * @return 返回实体id
     */
    T save(T data);

    /**
     * 修改实体
     *
     * @param filter 筛选条件
     * @param update
     */
    void update(Bson filter, Bson update);
}
