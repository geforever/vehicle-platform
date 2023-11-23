package org.platform.vehicle.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.platform.vehicle.constant.WxConstant;
import org.platform.vehicle.entity.WxUserInfo;
import org.platform.vehicle.mapper.WxUserInfoMapper;
import org.platform.vehicle.param.JssdkParam;
import org.platform.vehicle.service.WxService;
import org.platform.vehicle.util.MessageUtil;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WxServiceImpl implements WxService {

    @Value("${wxpay.gzhId}")
    private String gzhId;
    @Value("${wxpay.gzhSecret}")
    private String gzhSecret;
    @Value("${wxpay.token}")
    private String gzhToken;
    @Value("${wxpay.appId}")
    private String appId;
    @Value("${wxpay.appSecret}")
    private String appSecret;
    @Value("${spring.profiles.active}")
    private String active;

    private final RedisTemplate<String, Object> redisTemplate;

    private final WxUserInfoMapper wxUserInfoMapper;


    /**
     * 微信公众号授权
     *
     * @param code
     * @return
     */
    @Override
    public BaseResponse mpAuthorize(String code) {
        if (StrUtil.isBlank(code)) {
            return BaseResponse.failure("code不能为空");
        }
        //通过 code 换取网页授权access_token
        Map<String, Object> map = new HashMap<>();
        map.put("appid", gzhId);
        map.put("secret", gzhSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        //发送请求
        String result = HttpUtil.get(url, map);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(result);
        if (ObjectUtil.isAllNotEmpty(json.getString("openid"), json.getString("unionid"))) {
            String officialAccountId = json.getString("openid");
            String unionId = json.getString("unionid");
            StaticLog.info(StrUtil.format("微信公众号获取授权成功openId: {},unionId: {}",
                    officialAccountId,
                    unionId));
//            this.saveWxUserOfficialAccountId(userId, unionId, officialAccountId);
            return BaseResponse.ok(officialAccountId);
        } else {
            StaticLog.info(result);
            return BaseResponse.failure("微信公众号授权失败");
        }
    }

    @Override
    public BaseResponse jssdk(JssdkParam param) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            StaticLog.info(param.getUrl());
            String ticket = getTicket();
            Long timestamp = System.currentTimeMillis() / 1000;
            String nonceStr = RandomUtil.randomString(10);
            String str =
                    "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp
                            + "&url=" + URLDecoder.decode(param.getUrl(), "utf-8");
            String sha1 = SecureUtil.sha1(str);
            StaticLog.info("jssdkstr" + str);
            StaticLog.info("jssdksha1" + sha1);
            resultMap.put("timestamp", timestamp);
            resultMap.put("nonceStr", nonceStr);
            resultMap.put("signature", sha1);
        } catch (Exception e) {
            e.printStackTrace();
            StaticLog.info("jssdk失败" + e);
        }

        return BaseResponse.ok(resultMap);
    }

    @Override
    public String getGzhAccessToken() {
        String accessToken;
        Object objToken = redisTemplate.opsForValue().get(WxConstant.WX_GZH_TOKEN);
        if (ObjectUtil.isNull(objToken)) {
            //先获取access_token
            Map<String, Object> map = new HashMap<>();
            map.put("grant_type", "client_credential");
            map.put("appid", gzhId);
            map.put("secret", gzhSecret);
            String url = "https://api.weixin.qq.com/cgi-bin/token";
            //发送请求
            String result = HttpUtil.get(url, map);
            //解析相应内容（转换成json对象）
            JSONObject json = JSONObject.parseObject(result);
            if (ObjectUtil.isNotNull(json.get("access_token"))) {
                accessToken = json.getString("access_token");
                StaticLog.info("获取access_token成功：" + accessToken);
                redisTemplate.opsForValue()
                        .set(WxConstant.WX_GZH_TOKEN, accessToken, 3600, TimeUnit.SECONDS);
            } else {
                StaticLog.info("获取access_token失败：" + result);
                accessToken = null;
            }
        } else {
            accessToken = (String) objToken;
        }
        StaticLog.info("获取accessToken缓存成功：" + accessToken);
        return accessToken;
    }

    @Override
    public BaseResponse getAccountLoginShow() {
//        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Config::getCode, "accountLogin");
//        Config config = configMapper.selectOne(wrapper);
//        return BaseResponse.ok(config.getStatus());
        return null;
    }

    private String getTicket() {
        Object js_sdk_ticket = redisTemplate.opsForValue().get(WxConstant.JS_SDK_TICKET); // 先从缓存中取
        if (ObjectUtil.isNotNull(js_sdk_ticket)) {
            return String.valueOf(js_sdk_ticket);
        }

        String accessToken = getGzhAccessToken();

        // 根据token获取签名
        if (StrUtil.isNotBlank(accessToken)) {
            Map<String, Object> map = new HashMap<>();
            map.put("access_token", accessToken);
            map.put("type", "jsapi");
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
            //发送请求
            String result = HttpUtil.get(url, map);
            //解析相应内容（转换成json对象）
            JSONObject json = JSONObject.parseObject(result);
            if (ObjectUtil.isNotNull(json.get("ticket"))) {
                String ticket = json.getString("ticket");
                StaticLog.info("获取ticket成功：" + ticket);
                redisTemplate.opsForValue()
                        .set(WxConstant.JS_SDK_TICKET, ticket, 3600, TimeUnit.SECONDS);
                return ticket;
            } else {
                StaticLog.info("jssdk获得ticket失败" + result);
            }
        } else {
            StaticLog.info("access_token获取失败");
        }
        return null;
    }


    @Override
    public String getMiniAppAccessToken() {
        Object tokenObj = redisTemplate.opsForValue().get(WxConstant.MINI_APP_TOKEN); // 先从缓存中取
        if (ObjectUtil.isNotNull(tokenObj)) {
            return String.valueOf(tokenObj);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", appId);
        map.put("secret", appSecret);
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        //发送请求
        String result = HttpUtil.get(url, map);
        JSONObject json = JSONObject.parseObject(result);
        if (json.get("access_token") != null) {
            redisTemplate.opsForValue()
                    .set(WxConstant.MINI_APP_TOKEN, json.getString("access_token"), 3600,
                            TimeUnit.SECONDS);
            return json.getString("access_token");
        }
        return null;
    }

    @Override
    public JSONObject authorizeWxPhone(String jsCode) {
        // 获取	接口调用凭证
        String access_token = getMiniAppAccessToken();
        if (StrUtil.isBlank(access_token)) {
            return null;
        }
        //请求参数
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";
        //发送请求
        JSONObject params = new JSONObject();
        params.put("code", jsCode);
        String result = HttpRequest.post(url + access_token)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(params))
                .execute().body();
        log.info("微信授权 获取result：" + result);
        JSONObject json = JSONObject.parseObject(result);

        if (ObjectUtil.isNotNull(json.get("errcode")) && json.getString("errcode")
                .equals("40001")) {
            // accesstoken失效
            redisTemplate.delete(WxConstant.MINI_APP_TOKEN);
            // 重试
            access_token = getMiniAppAccessToken();
            result = HttpRequest.post(url + access_token)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(params))
                    .execute().body();
            log.info("微信授权 获取result2：" + result);
            json = JSONObject.parseObject(result);
        }
        return json;
    }

    /**
     * 生成临时二维码
     *
     * @return
     */
    @Override
    public BaseResponse getQrCode() {
        UserVo currentSysUser = UserContext.getUser();
//        WxUserInfo wxUserInfo = wxUserInfoMapper.selectOne(new LambdaQueryWrapper<WxUserInfo>()
//                .eq(WxUserInfo::getUserId, currentSysUser.getUserId()));
//        if (wxUserInfo == null) {
//            return BaseResponse.failure("用户未绑定微信");
//        }
        String resultUrl = "";
        String gzhAccessToken = this.getGzhAccessToken();
//        String gzhAccessToken = "access_token";
        if (StrUtil.isBlank(gzhAccessToken)) {
            StaticLog.info("access_token获取失败");
            return BaseResponse.failure("access_token获取失败");
        }
        JSONObject sceneObj = new JSONObject();
        sceneObj.put("scene_str", String.valueOf(currentSysUser.getUserId()));
        JSONObject actionInfoObj = new JSONObject();
        actionInfoObj.put("scene", sceneObj);
        JSONObject params = new JSONObject();
        params.put("expire_seconds", 604800);
        params.put("action_name", "QR_STR_SCENE");
        params.put("action_info", actionInfoObj);
        log.info("gzhQrCode获取参数：" + JSONUtil.toJsonStr(params));
        String url =
                "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + gzhAccessToken;
        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(params))
                .execute().body();
        StaticLog.info("gzhQrCode获取结果：" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (StrUtil.isNotBlank(jsonObject.getString("ticket"))) {
            resultUrl =
                    "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + jsonObject.getString(
                            "ticket");
        }
        return BaseResponse.ok(resultUrl);
    }

    /**
     * 处理微信公众号回调事件
     *
     * @param request
     * @return
     */
    @Override
    public String processRequest(HttpServletRequest request) {
        // xml格式的消息数据
        String respXml = "";
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            log.info("微信公众号回调info: {}", JSONObject.toJSONString(requestMap));
            // 发送方账号 发送方帐号（一个OpenID）
            String fromUserName = requestMap.get("FromUserName");
            // 开发者微信号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            String eventKey = requestMap.get("EventKey");
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                if (ObjectUtil.isNotNull(eventKey)) {
                    if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                        log.info("微信公众号回调关注事件: {}", JSONObject.toJSONString(requestMap));
                        // 未关注扫码
                        // 根据微信小程序openId 获取OpenId对应的UnionId
                        JSONObject userJson = this.getUserInfoByGzhOpenId(
                                fromUserName);
                        Integer userId = Integer.valueOf(
                                String.valueOf(userJson.get("qr_scene_str")));
                        // 根据微信小程序openId 同步更新微信公众号openId说明这个用户已经关注
                        this.saveWxUserOfficialAccountId(userId, userJson);
                        //调用客服接口
                        try {
                            this.sendGzhText("你好，欢迎关注面心科技公众号！\n" + "\n"
                                            + "点击下方【账号绑定】，可接收告警消息推送。",
                                    fromUserName);
                        } catch (Exception e) {
                            log.info("公众号消息发送失败" + e);
                        }

                    } else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                        log.info("微信公众号回调取消关注事件: {}",
                                JSONObject.toJSONString(requestMap));
                        // 用户取消关注
                        this.unsubscribe(fromUserName);
                    }
                }
            }
        } catch (Exception e) {
            StaticLog.error("微信回调任务失败" + JSONObject.toJSONString(e));
        }
        return respXml;
    }

    private void unsubscribe(String fromUserName) {
        LambdaUpdateWrapper<WxUserInfo> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WxUserInfo::getOpenId, fromUserName);
        wxUserInfoMapper.delete(updateWrapper);
    }

    private void saveWxUserOfficialAccountId(Integer userId, JSONObject userJson) {
        String openid = (String) userJson.get("openid");
        // 根据openId查询历史记录
        WxUserInfo wxUserInfoHistory = wxUserInfoMapper.selectOne(
                new LambdaQueryWrapper<WxUserInfo>()
                        .eq(WxUserInfo::getOpenId, openid));
        if (wxUserInfoHistory == null) {
            String country = String.valueOf(userJson.get("country"));
            String city = String.valueOf(userJson.get("city"));
            String province = String.valueOf(userJson.get("province"));
            String nickname = String.valueOf(userJson.get("nickname"));
            String avatarUrl = String.valueOf(userJson.get("headimgurl"));
            String unionId = String.valueOf(userJson.get("unionid"));
            WxUserInfo wxUserInfo = new WxUserInfo();
            wxUserInfo.setUserId(userId);
            wxUserInfo.setAvatarUrl(avatarUrl);
            wxUserInfo.setNickName(nickname);
            wxUserInfo.setOpenId(openid);
            if (StringUtils.isNotBlank(unionId)) {
                wxUserInfo.setUnionId(unionId);
            }
            wxUserInfo.setProvince(province);
            wxUserInfo.setCity(city);
            wxUserInfo.setCountry(country);
            wxUserInfoMapper.insert(wxUserInfo);
        }
    }

    private JSONObject getUserInfoByGzhOpenId(String openId) {
        String gzhAccessToken = this.getGzhAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        Map<String, Object> map = new HashMap<>(3);
        map.put("access_token", gzhAccessToken);
        map.put("openid", openId);
        map.put("lang", "zh_CN");
        //发送请求
        String result = HttpUtil.get(url, map);
        StaticLog.info("getWxUnionIdByGzhOpen" + result);
        JSONObject json = JSONObject.parseObject(result);
        if (ObjectUtil.isNotEmpty(json.get("errcode"))) {
            StaticLog.info("getWxUnionIdByGzhOpen失败" + openId);
            return null;
        }
//        if (ObjectUtil.isNotEmpty(json.get("unionid"))) {
//            return json.getString("unionid");
//        }
        return json;
    }

    @Override
    public boolean check(String timestamp, String nonce, String signature) {
        //1)将token,timestamp,nonce三个参数进行字典排序
        String[] strs = new String[]{gzhToken, timestamp, nonce};
        Arrays.sort(strs);
        //2)将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);
        //3)开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return mysig.equalsIgnoreCase(signature);
    }

    private static String sha1(String src) {
        try {
            //获取一个加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(src.getBytes());
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                    'e', 'f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信公众号发送客服消息 -- 文本类型
     *
     * @param text      消息内容
     * @param gzhOpenId 公众号OpenId
     */
    public void sendGzhText(String text, String gzhOpenId) {
        String gzhAccessToken = getGzhAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                + gzhAccessToken;

        JSONObject content = new JSONObject();
        content.put("content", text);
        JSONObject params = new JSONObject();
        params.put("touser", gzhOpenId);
        params.put("msgtype", MessageUtil.REQ_MESSAGE_TYPE_TEXT);
        params.put("text", content);
        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(params))
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        String errcode = jsonObject.getString("errcode");
        if (!"0".equals(errcode)) {
            StaticLog.info("客服消息发送失败" + result);
        }
    }
}
