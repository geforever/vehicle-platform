package org.platform.vehicle.utils;


import org.platform.vehicle.vo.UserVo;

/**
 * @author
 */
public class UserContext {

    private UserContext() {
    }

    private static final ThreadLocal<UserVo> SYS_USER_LOCAL = new ThreadLocal<UserVo>();

    public static void init(UserVo sysUser) {
        SYS_USER_LOCAL.set(sysUser);
    }

    public static UserVo getUser() {
        return SYS_USER_LOCAL.get();
    }

    public static void clean() {
        SYS_USER_LOCAL.remove();
    }

}

