package com.fobgochod.repository.base;

import com.fobgochod.entity.BaseEntity;

public interface EntityRepository<T extends BaseEntity> extends CrudRegistry<T> {

}
