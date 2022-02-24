package com.fobgochod.domain;

/**
 * Domain和Entity互转
 *
 * @author zhouxiao
 * @date 2020/4/22
 */
public interface Converter<E> {
    /**
     * Domain转Entity
     *
     * @return Entity
     */
    E doForward();

    /**
     * Entity转Domain
     *
     * @param e Entity
     */
    void doBackward(E e);
}
