package org.platform.vehicle.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.platform.vehicle.service.WxService;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信公众号
 */
@Slf4j
@RestController
@RequestMapping("/officialAccount")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WxOfficialAccountController {

    private final WxService wxService;

    /**
     * 获取公众号二维码
     *
     * @return
     */
    @GetMapping("/qrCode")
    public BaseResponse getQrCode() {
        return wxService.getQrCode();
    }

    @GetMapping(value = "/redirectUri")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //校验请求
        if (wxService.check(timestamp, nonce, signature)) {
            //校验成功后原样返回echostr参数
            PrintWriter out = response.getWriter();
            out.print(echostr);
            out.flush();
            out.close();
        } else {
            log.info("接入失败！");
        }
    }

    /**
     * 接收消息和事件推送 此接口的uri必须和你配置的那个服务器地址（URL）对应上
     */
    @PostMapping(value = "/redirectUri")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("接收消息和事件推送");
        // 消息的接收、处理、响应
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 调用核心业务类接收消息、处理消息
        String respXml = wxService.processRequest(request);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.println(respXml);
        out.close();
    }
}
