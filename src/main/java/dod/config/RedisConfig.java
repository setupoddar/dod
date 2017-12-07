package dod.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by manas.kirti on 19/11/17.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisConfig {

    private String masterName;
    private String host;
    private Integer port;
    private String password;
    private int timeout;
    private GenericObjectPoolConfig poolConfig;
}