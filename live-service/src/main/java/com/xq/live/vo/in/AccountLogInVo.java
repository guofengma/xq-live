package com.xq.live.vo.in;

/**
 * com.xq.live.vo.in
 * 交易流水日志
 * @author zhangpeng32
 * Created on 2018/5/7 上午10:13
 * @Description:
 */
public class AccountLogInVo extends BaseInVo{
    private Long id;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
