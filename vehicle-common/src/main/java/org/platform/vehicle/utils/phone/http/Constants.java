package org.platform.vehicle.utils.phone.http;

/**
 * Created by
 * momo
 * on 2018/02/07
 */
public interface Constants {
    //号码数量限制
    int phoneLenLimit = 20000;
    //批量内容限制
    int batchLenLimit = 500;

    // http 反馈状态
    interface Status {
        // 提交成功
        int OK = 0;
        // http连接失败
        int CONNECT_FAIL = -1;
        // http连接异常
        int EXCEPTION = -2;
        // 批量发送手机数量限制
        int PHONE_LEN_LIMIT = -3;
        // 批量发送内容数量限制
        int BATCH_LEN_LIMIT = -4;
    }
}
