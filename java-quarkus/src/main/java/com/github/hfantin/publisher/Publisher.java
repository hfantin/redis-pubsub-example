package com.github.hfantin.publisher;

import com.github.hfantin.models.ProcessDTO;
import com.github.hfantin.models.ProcessStatus;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class Publisher {

    private static final Logger LOGGER = Logger.getLogger(Publisher.class.getSimpleName());

    private final ValueCommands<String, ProcessDTO> commands;
    private final PubSubCommands<ProcessDTO> pub;

    public Publisher(RedisDataSource ds) {
        commands = ds.value(ProcessDTO.class);
        pub = ds.pubsub(ProcessDTO.class);
    }

    public ProcessDTO get(final String key) {
        var value = commands.get(key);
        LOGGER.info("publisher - get " + key + " " + value);
        return value;
    }

    public void set(final String key, final String value) {
        LOGGER.info("publisher - set " + key + " " + value);
        ProcessDTO processDTO = new ProcessDTO(ProcessStatus.getByName(value), LocalDateTime.now());
        commands.set(key, processDTO);
        pub.publish(key, processDTO);
    }
}
