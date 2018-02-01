package com.xq.live.model;


import org.springframework.security.core.GrantedAuthority;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
