package com.xq.live.vo.in;

import java.util.Date;

/**
 * com.xq.live.vo.in
 *
 * @author zhangpeng32
 * Created on 2018/5/6 下午5:05
 * @Description:
 */
public class CashApplyInVo extends BaseInVo {
    private Long id;
    private Long userId;
    private String userName;

    private Date beginTime;

    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
