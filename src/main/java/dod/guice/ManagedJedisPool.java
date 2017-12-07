package dod.guice;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dod.config.RedisConfig;
import io.dropwizard.lifecycle.Managed;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manas.kirti on 19/11/17.
 */

@Slf4j
@Singleton
public class ManagedJedisPool extends HealthCheck implements Managed {

    //Without Sentinel
    @Getter
    private JedisPool jedisPool;


    @Inject
    public ManagedJedisPool(RedisConfig redisConfig) {
        jedisPool = new JedisPool(redisConfig.getPoolConfig(),
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeout(),
                StringUtils.isNotEmpty(redisConfig.getPassword()) ? redisConfig.getPassword() : null);


        warmUpJedisPool(redisConfig.getPoolConfig());
    }


    public void start() throws Exception {

    }

    public void stop() throws Exception {
        jedisPool.destroy();
    }

    private void warmUpJedisPool(GenericObjectPoolConfig config) {
        log.debug("Warming up the jedis pool" + jedisPool);
        List<Jedis> jedisList = new ArrayList<>();

        try {
            for (int i = 0; i < config.getMaxTotal(); ++i) {
                jedisList.add(jedisPool.getResource());
            }

            jedisList.forEach(Jedis::close);
        } catch (Exception e) {
            log.error("Got exception when warming up jedis pool: ", e);
            throw new RuntimeException("Jedis pool warming up failed.");
        }

        log.debug("Warmup done");
    }

    protected Result check() throws Exception {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                return Result.unhealthy("Jedis resource from pool is null");
            }
        } catch (Exception e) {
            return Result.unhealthy("Exception while trying to get resource from pool: " + e.getMessage());
        }

        return Result.healthy();
    }
}