package com.fobgochod.entity.admin;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 角色
 *
 * @author Xiao
 * @date 2022/5/23 21:31
 */
@Document("role")
public class Role extends BaseEntity {

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
