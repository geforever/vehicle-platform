package org.platform.vehicle.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LogUtils {

    public enum Lv {
        TRACE(Level.TRACE),
        DEBUG(Level.DEBUG),
        INFO(Level.INFO),
        WARN(Level.WARN),
        ERROR(Level.ERROR);

        Lv(Level value) {
            this.value = value;
        }

        public final Level value;
    }
}
