package com.github.hfantin.subscriber;

import com.github.hfantin.models.ProcessDTO;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import org.jboss.logging.Logger;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.function.Consumer;

import static com.github.hfantin.models.Constants.PROCESS_STATUS_CHANNEL;

@Singleton
@Startup // We want to create the bean instance on startup to subscribe to the channel.
public class Subscriber implements Consumer<ProcessDTO> {

    private static final Logger LOGGER = Logger.getLogger(Subscriber.class.getSimpleName());
    private final PubSubCommands<ProcessDTO> pub;
    private final PubSubCommands.RedisSubscriber subscriber;

    public Subscriber(RedisDataSource ds) {
        LOGGER.info("subscriber - constructor");
        pub = ds.pubsub(ProcessDTO.class);
        subscriber = pub.subscribe(PROCESS_STATUS_CHANNEL, this);
    }

    @Override
    public void accept(ProcessDTO processDTO) {
        // Receive the notification
        LOGGER.info("subscriber - accept - processo=" + processDTO);
    }

    @PreDestroy
    public void terminate() {
        LOGGER.info("subscriber - terminate");
        subscriber.unsubscribe(); // Unsubscribe from all subscribed channels
    }
}
