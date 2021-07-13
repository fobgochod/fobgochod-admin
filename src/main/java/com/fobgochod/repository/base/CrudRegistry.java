package com.fobgochod.repository.base;

import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.entity.BaseEntity;

import java.util.Collection;
import java.util.List;

/**
 * Entity CRUD
 *
 * @author seven
 * @date 2021/6/29
 */
public interface CrudRegistry<T extends BaseEntity> {

    /**
     * 添加Entity
     *
     * @param data entity
     * @return 主键
     */
    String insert(T data);

    /**
     * 根据id删除Entity
     *
     * @param id 主键
     */
    void deleteById(String id);

    /**
     * 根据ids批量删除Entity
     *
     * @param ids 主键集合
     */
    long deleteByIdIn(Collection<String> ids);

    /**
     * 修改Entity
     *
     * @param data entity
     */
    void update(T data);

    /**
     * 根据id查询Entity
     *
     * @param id 主键
     * @return Entity
     */
    T findById(String id);

    /**
     * 根据id判断是否存在
     *
     * @param id 主键
     * @return true false
     */
    boolean existsById(String id);

    /**
     * 查询所有Entity
     *
     * @return Entities
     */
    List<T> findAll();

    /**
     * 查询所有Entity
     *
     * @param filter 查询条件
     * @return Entities
     */
    List<T> findAll(T filter);

    /**
     * 分页查询
     *
     * @param page 查询条件
     * @return Entities
     */
    PageData<T> findByPage(Page page);

    /**
     * 删除集合
     */
    void dropCollection();
}
