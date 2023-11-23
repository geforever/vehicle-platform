package org.platform.vehicle.web.config;

import io.github.yezhihao.netmc.core.HandlerMapping;
import io.github.yezhihao.netmc.core.SpringHandlerMapping;
import io.github.yezhihao.netmc.session.SessionListener;
import io.github.yezhihao.netmc.session.SessionManager;
import io.github.yezhihao.protostar.SchemaManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.platform.vehicle.codec.DataFrameMessageDecoder;
import org.platform.vehicle.codec.JTMessageAdapter;
import org.platform.vehicle.codec.JTMessageEncoder;
import org.platform.vehicle.codec.MultiPacketDecoder;
import org.platform.vehicle.web.endpoint.JTHandlerInterceptor;
import org.platform.vehicle.web.endpoint.JTMultiPacketListener;
import org.platform.vehicle.web.endpoint.JTSessionListener;
import org.platform.vehicle.web.model.enums.SessionKey;

@Configuration
public class JTBeanConfig {

    @Bean
    public HandlerMapping handlerMapping() {
        return new SpringHandlerMapping();
    }

    @Bean
    public JTHandlerInterceptor handlerInterceptor() {
        return new JTHandlerInterceptor();
    }

    @Bean
    public SessionListener sessionListener() {
        return new JTSessionListener();
    }

    @Bean
    public SessionManager sessionManager(SessionListener sessionListener) {
        return new SessionManager(SessionKey.class, sessionListener);
    }

    @Bean
    public SchemaManager schemaManager() {
        return new SchemaManager("org.yzh.protocol");
    }

    @Bean
    public JTMessageAdapter messageAdapter(SchemaManager schemaManager) {
        JTMessageEncoder encoder = new JTMessageEncoder(schemaManager);
        MultiPacketDecoder decoder = new MultiPacketDecoder(schemaManager, new JTMultiPacketListener(10));
        return new WebLogAdapter(encoder, decoder);
    }

    @Bean
    public JTMessageAdapter alarmFileMessageAdapter(SchemaManager schemaManager) {
        JTMessageEncoder encoder = new JTMessageEncoder(schemaManager);
        DataFrameMessageDecoder decoder = new DataFrameMessageDecoder(schemaManager, new byte[]{0x30, 0x31, 0x63, 0x64});
        return new WebLogAdapter(encoder, decoder);
    }

    @Bean
    public MultiPacketDecoder multiPacketDecoder(SchemaManager schemaManager) {
        return new MultiPacketDecoder(schemaManager);
    }
}
