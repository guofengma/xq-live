package com.xq.live.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
public class UsernameIsExitedException extends AuthenticationException {
    public UsernameIsExitedException(String msg) {
        super(msg);
    }

    public UsernameIsExitedException(String msg, Throwable t) {
        super(msg, t);
    }
}
