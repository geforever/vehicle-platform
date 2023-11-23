package org.platform.vehicle.util;

import com.alibaba.fastjson.JSON;
import com.github.dadiyang.equator.Equator;
import com.github.dadiyang.equator.FieldInfo;
import com.github.dadiyang.equator.GetterBaseEquator;
import org.platform.vehicle.aspect.FieldName;
import org.platform.vehicle.constant.OperateLogConstant;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.entity.SysOperateLog;
import org.platform.vehicle.mapper.SysOperateLogMapper;
import org.platform.vehicle.vo.UserVo;
import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2021/9/24 4:43 下午
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OperateLogAsync {

    public static final String BASE = "【%s】字段由 {%s} 变更为 {%s};";


    @Autowired
    private SysOperateLogMapper sysOperateLogMapper;


    @Async("asyncTaskExecutor")
    public <T> void pushAddLog(T obj, OperateModuleEnum operateModuleEnum, String code,
            UserVo user, HttpServletRequest request) {
        this.saveLog(operateModuleEnum, code, user, OperateLogConstant.ADD, request,
                JSON.toJSONString(obj));
    }

    @Async("asyncTaskExecutor")
    public <T> void pushEditLog(Class<T> cls, T oldObj, T newObj,
            OperateModuleEnum operateModuleEnum, String code, UserVo user,
            HttpServletRequest request) {
        if (oldObj == null || newObj == null) {
            return;
        }
        // 比较对象属性差异
        Equator equator = new GetterBaseEquator();
        List<FieldInfo> diff = equator.getDiffFields(oldObj, newObj);
        if (diff.isEmpty()) {
            return;
        }
        StringBuilder diffMsg = this.getDiffMessage(cls, diff);
        if (StringUtils.isNotBlank(diffMsg.toString())) {
            this.saveLog(operateModuleEnum, code, user, OperateLogConstant.EDIT, request,
                    diffMsg.toString());
        }
    }

    @Async("asyncTaskExecutor")
    public <T> void pushDeleteLog(OperateModuleEnum operateModuleEnum, String code,
            UserVo user, HttpServletRequest request, String diff) {
        this.saveLog(operateModuleEnum, code, user, OperateLogConstant.DELETE, request, diff);
    }

    private void saveLog(OperateModuleEnum operateModuleEnum, String code, UserVo user,
            Integer type, HttpServletRequest request, String diffMsg) {
        SysOperateLog sysOperateLog = new SysOperateLog();
        sysOperateLog.setPhone(user.getPhone());
//        if (request != null && request.getRequestURL() != null) {
//            sysOperateLog.setUrl(request.getRequestURL().toString());
//            sysOperateLog.setMethod(request.getMethod());
//        }
        sysOperateLog.setCode(code);
        sysOperateLog.setModule(operateModuleEnum.getModule());
        sysOperateLog.setOperation(operateModuleEnum.getOperation());
        sysOperateLog.setMessage(diffMsg);
        sysOperateLog.setSource(user.getCustomerType());
        sysOperateLog.setType(type);
        sysOperateLog.setCreatePerson(user.getName());
        sysOperateLogMapper.insert(sysOperateLog);
    }

    /**
     * 比较对象差异
     *
     * @param cls
     * @param diff
     * @param <T>
     * @return
     */
    private <T> StringBuilder getDiffMessage(Class<T> cls, List<FieldInfo> diff) {
        StringBuilder sb = new StringBuilder();
        for (FieldInfo fieldInfo : diff) {
            if (fieldInfo.getSecondVal() != null) {
                Field field = null;
                try {
                    field = cls.getDeclaredField(fieldInfo.getFieldName());
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if (fieldName != null) {
                        String value = fieldName.value();
                        Object firstValObj = fieldInfo.getFirstVal();
                        String firstVal = " ";
                        if (firstValObj != null) {
                            firstVal = String.valueOf(firstValObj);
                        }
                        Object secondValObj = fieldInfo.getSecondVal();
                        String secondVal = " ";
                        if (secondValObj != null) {
                            secondVal = String.valueOf(secondValObj);
                        }
                        String message = String.format(BASE, value, firstVal, secondVal);
                        sb.append(message);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    log.error("获取字段注解失败，field：{}", fieldInfo.getFieldName());
                }
            }
        }
        return sb;
    }
}
