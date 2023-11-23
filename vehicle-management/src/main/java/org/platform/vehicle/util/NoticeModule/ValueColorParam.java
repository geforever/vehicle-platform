package org.platform.vehicle.util.NoticeModule;

import lombok.Data;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 15:55
 */
@Data
public class ValueColorParam {

    private Object value;
    private String color;

    public ValueColorParam() {
    }

    public ValueColorParam(Object value, String color) {
        this.value = value;
        this.color = color;
    }

}
