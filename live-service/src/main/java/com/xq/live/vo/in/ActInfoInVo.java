package com.xq.live.vo.in;

/**
 * @Description 活动入参
 * Created by zhangpeng32 on 2018/2/7.
 */
public class ActInfoInVo extends BaseInVo {
    private Long id;

    private String actName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }
}
