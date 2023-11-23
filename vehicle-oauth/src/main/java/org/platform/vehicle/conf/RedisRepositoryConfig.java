package org.platform.vehicle.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis相关配置
 *
 * @author gejiawei
 * @date 2020/6/19
 */
@Configuration
@EnableRedisRepositories
@EnableCaching
public class RedisRepositoryConfig {

    @Value("${redis.sentinel.master}")
    private String master;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.database}")
    private Integer dbIndex;

    @Value("${redis.sentinel.nodes}")
    private String nodes;

    @Bean
    @Lazy
    public RedissonClient redissonClient() {
        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        for(String node : nodes.split(",")){
            sentinelServersConfig.addSentinelAddress("redis://"+node);
        }
        sentinelServersConfig.setMasterName(master).setPassword(password).setDatabase(dbIndex);

        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(getConnectionFactory());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(stringRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisConnectionFactory getConnectionFactory() {
        //哨兵模式
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.setMaster(master);
        configuration.setPassword(password);
        configuration.setDatabase(dbIndex);
        nodes.split(",");
        for(String node : nodes.split(",")){
            String[] str = node.split(":");
            RedisNode redisServer = new RedisServer(str[0], Integer.parseInt(str[1]));
            configuration.sentinel(redisServer);
        }
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, getPool());
        //使用前先校验连接,这个最好是要配置：不然会带来connection reset by peer
        factory.setValidateConnection(true);
        return factory;
    }

    @Bean
    public LettuceClientConfiguration getPool() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        //redis客户端配置:超时时间默认
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder = LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(60000));
        //连接池配置
        RedisProperties.Pool pool = new RedisProperties.Pool();
        genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
        builder.shutdownTimeout(Duration.ofMillis(4000));
        builder.poolConfig(genericObjectPoolConfig);
        return builder.build();
    }
}
