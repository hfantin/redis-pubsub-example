package com.github.hfantin.resources;

import com.github.hfantin.models.ProcessDTO;
import com.github.hfantin.publisher.Publisher;
import com.github.hfantin.subscriber.Subscriber;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.github.hfantin.models.Constants.PROCESS_STATUS_CHANNEL;

@Path("/status")
public class StatusResource {


    @Inject
    private Publisher publisher;

    @Inject
    private Subscriber subscriber;

    private static final Logger LOGGER = Logger.getLogger(StatusResource.class.getSimpleName());

    void onStart(@Observes StartupEvent ev) {
        ProcessDTO processDTO = publisher.get(PROCESS_STATUS_CHANNEL);
        LOGGER.info("starting application - current status: " + processDTO);
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("stopping applictation");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/publish/{status}")
    public String publish(@PathParam("status") String status) {
        LOGGER.info("publishing status" + status);
        try {
            publisher.set(PROCESS_STATUS_CHANNEL, status);
            return "status " + status + " publicado com sucesso!";
        } catch (IllegalArgumentException iae) {
            return "Não foi possível publicar o status: " + iae.getMessage();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        LOGGER.info("get status");
        return "status atual: " + publisher.get(PROCESS_STATUS_CHANNEL);
    }

}