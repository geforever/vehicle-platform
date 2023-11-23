package org.platform.vehicle.web.controller;

import io.github.yezhihao.netmc.session.Session;
import io.github.yezhihao.netmc.session.SessionManager;
import io.github.yezhihao.netmc.util.AdapterCollection;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.platform.vehicle.model.APIResult;
import org.platform.vehicle.util.LogUtils;
import org.platform.vehicle.codec.JTMessageDecoder;
import org.platform.vehicle.web.config.WebLogAdapter;
import org.platform.vehicle.web.model.entity.DeviceDO;
import org.platform.vehicle.web.model.enums.SessionKey;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class OtherController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private JTMessageDecoder decoder;

    @Hidden
    @Operation(hidden = true)
    @GetMapping
    public void doc(HttpServletResponse response) throws IOException {
        response.sendRedirect("doc.html");
    }

    @Operation(summary = "终端实时信息查询")
    @GetMapping("device/all")
    public APIResult<Collection<Session>> all() {
        Collection<Session> all = sessionManager.values();
        return APIResult.ok(all);
    }

    @Operation(summary = "获得当前所有在线设备信息")
    @GetMapping("device/option")
    public APIResult<Collection<DeviceDO>> getClientId(HttpSession httpSession) {
        AdapterCollection<Session, DeviceDO> result = new AdapterCollection<>(sessionManager.values(), session -> {
            DeviceDO device = SessionKey.getDevice(session);
            if (device != null) {
                return device;
            }
            return new DeviceDO().mobileNo(session.getClientId());
        });
        return APIResult.ok(result);
    }

    @Operation(summary = "设备订阅")
    @PostMapping(value = "device/sse", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sseSub(HttpSession httpSession, @RequestParam String clientId, @RequestParam boolean sub) {
        FluxSink<Object> emitter = (FluxSink<Object>) httpSession.getAttribute("emitter");
        if (emitter == null) {
            return "0";
        }
        if (sub) {
            WebLogAdapter.addClient(clientId, emitter);
            ((Set<String>) httpSession.getAttribute("clientIds")).add(clientId);
        } else {
            WebLogAdapter.removeClient(clientId, emitter);
            ((Set<String>) httpSession.getAttribute("clientIds")).remove(clientId);
        }
        return "1";
    }

    @Operation(summary = "设备监控")
    @GetMapping(value = "device/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> sseConnect(HttpSession httpSession, String clientId) {
        return Flux.create(emitter -> {
            Set<String> clientIds = new HashSet<>();
            if (clientId != null) {
                WebLogAdapter.addClient(clientId, emitter);
                clientIds.add(clientId);
            }
            httpSession.setAttribute("clientIds", clientIds);
            httpSession.setAttribute("emitter", emitter);
            emitter.onDispose(() -> clientIds.forEach(id -> WebLogAdapter.removeClient(id, emitter)));
        });
    }

    @Operation(summary = "808协议分析工具")
    @RequestMapping(value = "message/explain", method = {RequestMethod.POST, RequestMethod.GET})
    public String decode(@Parameter(description = "16进制报文") @RequestParam String hex) {
        Explain explain = new Explain();
        hex = hex.replace(" ", "");
        String[] lines = hex.split("\n");
        for (String line : lines) {
            String[] msgs = line.split("7e7e");
            for (String msg : msgs) {
                ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(msg));
                decoder.decode(byteBuf, explain);
            }
        }
        return explain.toString();
    }

    @Operation(summary = "原始消息发送")
    @PostMapping("device/raw")
    public Mono<String> postRaw(@Parameter(description = "终端手机号") @RequestParam String clientId,
                                @Parameter(description = "16进制报文") @RequestParam String message) {
        Session session = sessionManager.get(clientId);
        if (session != null) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(message));

            return session.notify(byteBuf).map(unused -> "success")
                    .timeout(Duration.ofSeconds(10), Mono.just("timeout"))
                    .onErrorResume(throwable -> Mono.just("fail"));
        }
        return Mono.just("offline");
    }

    @Operation(summary = "修改日志级别")
    @GetMapping("logger")
    public String logger(@RequestParam LogUtils.Lv level) {
        return "success";
    }

}
