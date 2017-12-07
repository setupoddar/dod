package dod.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@Getter
public class DodConfiguration extends Configuration {

    @NotNull
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory;

    @JsonProperty("redisConfig")
    private RedisConfig redisConfig;

}