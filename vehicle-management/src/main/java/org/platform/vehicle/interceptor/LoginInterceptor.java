package org.platform.vehicle.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.constants.OauthConstant;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.response.ResponseEnum;
import org.platform.vehicle.utils.JsonBinderUtil;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器
 */

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //预请求不做处理
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        log.info("拦截请求:" + request.getRequestURI());
        //解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Origin, Accept, assets-session-token");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        try {
            String token = request.getHeader(OauthConstant.LOGIN_HEAD_TOKEN);
            if (token == null) {
                //下载时从请求参数中取数据
                token = request.getParameter(OauthConstant.LOGIN_HEAD_TOKEN);
            }
            if (token == null) {
                log.info("请求中没有token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println(JsonBinderUtil.toJson(new BaseResponse<>(ResponseEnum.NOT_LOGIN_EXCEPTION)));
                return false;
            }
            log.info("loginToken is " + token);
            Object userInfoObj = redisTemplate.opsForValue().get(token);
            if (userInfoObj == null) {
                log.info("redis中数据已过期 " + token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println(JsonBinderUtil.toJson(new BaseResponse(ResponseEnum.NOT_LOGIN_EXCEPTION)));
                return false;
            }
            UserVo userVo = JSONObject.parseObject(String.valueOf(userInfoObj), UserVo.class);
            UserContext.init(userVo);
            return true;
        } catch (Exception e) {
            log.error("IO异常", e);
            return false;
        }
    }

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        UserContext.clean();
    }

}
