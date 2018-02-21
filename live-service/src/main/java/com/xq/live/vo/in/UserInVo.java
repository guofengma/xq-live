package com.xq.live.vo.in;

/**
 * 用户入参
 *
 * @author zhangpeng32
 * @create 2018-02-07 15:32
 **/
public class UserInVo extends BaseInVo {
    private Long id;
    private String userName;
    private String nickName;
    private Integer sourceType;
    private String userIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
