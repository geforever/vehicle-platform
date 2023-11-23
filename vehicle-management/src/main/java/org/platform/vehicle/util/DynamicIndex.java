package org.platform.vehicle.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/9 12:00
 */
@Component
public class DynamicIndex {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取索引名称后缀
     *
     * @return
     */
    public String getSuffix() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置索引名称后缀
     *
     * @param suffix
     */
    public void setSuffix(String suffix) {
        THREAD_LOCAL.set(suffix);
    }

    /**
     * 移除当前索引
     */
    public void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前索引
     *
     * @return
     */
    public String getIndex() {
        if (StringUtils.isBlank(getSuffix())) {
            return null;
        }
        return getSuffix();
    }

}
