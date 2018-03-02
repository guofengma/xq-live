package com.xq.live.dubbo.model;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-01 19:28
 * @copyright:hbxq
 **/
public class Test implements Serializable {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
