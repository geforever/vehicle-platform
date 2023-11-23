package org.platform.vehicle.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import org.platform.vehicle.conf.RedisRepositoryConfig;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.ResponseEnum;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 防止重复提交
 *
 * @author gejiawei
 */
@Slf4j
@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    private RedisRepositoryConfig redissonClient;

    @Pointcut("@annotation(repeatSubmit)")
    public void pointcutNoRepeatSubmit(RepeatSubmit repeatSubmit) {
    }

    @Around(value = "pointcutNoRepeatSubmit(noRepeatSubmit)", argNames = "joinPoint,noRepeatSubmit")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit noRepeatSubmit)
            throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        UserVo user = UserContext.getUser();
        String path = request.getServletPath();
        log.info("RepeatSubmitAspect  => around : 接口{},防止重复提交===>开始", path);
        long lockTime = noRepeatSubmit.lockTime();
        String lockField = noRepeatSubmit.lockField();
        String param = "";
        Object[] args = joinPoint.getArgs();
        // lockField为空,则加密所有参数
        if (StringUtils.isBlank(lockField)) {
            // 参数转md5
            if (CollectionUtil.isNotEmpty(Arrays.asList(args))) {
                param = SecureUtil.md5(args[0].toString());
            }
        } else {
            param = this.getLockField(args, lockField);
        }
        String key = "REPEAT_SUBMIT_" + "_" + param + "_" + path;
        // 分布式锁
        RLock lock = redissonClient.redissonClient().getLock(key);
        log.info("RepeatSubmitAspect  => around : 分布式锁key : {}", key);
        // 尝试加锁，最多等待0秒，上锁以后1秒自动解锁[lockTime默认为5分钟, 用户可以自定义]
        boolean res = lock.tryLock(0, lockTime, TimeUnit.MINUTES);
        if (!res) {
            log.error(
                    "RepeatSubmitAspect => around : 用户ID ：{}，用户名：{}，操作：{} 接口，重复提交被拦截",
                    user.getUserId(), user.getName(), path);
            throw new BaseException(ResponseEnum.REQUEST_PROCESSING);
        }
        try {
            long startTime = System.currentTimeMillis();
            Object object = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info(
                    "RepeatSubmitAspect => around : 用户ID ：{}，用户名：{}，操作：{} 接口，执行时间：{} ms",
                    user.getUserId(), user.getName(), path, endTime - startTime);
            return object;
        } catch (Exception e) {
            log.error("RepeatSubmitAspect => around : 用户ID ：{}，用户名：{}，操作：{} 接口，异常：{}",
                    user.getUserId(), user.getName(), path, e.getMessage());
            throw e;
        } finally {
            if (lock.isLocked()) {
                log.info("RepeatSubmitAspect  => around : 接口{},防止重复提交===>结束", path);
                lock.unlock();
                log.info("redis分布式锁,key:{},解锁成功", key);
            }
        }
    }

    /**
     * 处理输入参数
     *
     * @param args 入参列表
     */
    private String getLockField(Object[] args, String lockField) {
        try {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }
                if (arg != null) {
                    Class<?> clazz = arg.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals(lockField)) {
                            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                            Method readMethod = pd.getReadMethod();
                            Object invoke = readMethod.invoke(arg);
                            if (invoke instanceof String) {
                                return (String) invoke;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis分布式锁异常，param：{}", args);
            throw new BaseException(ResponseEnum.REQUEST_PROCESSING);
        }
        return "";
    }
}
