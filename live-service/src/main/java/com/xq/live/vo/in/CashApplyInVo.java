package com.xq.live.vo.in;

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
}
