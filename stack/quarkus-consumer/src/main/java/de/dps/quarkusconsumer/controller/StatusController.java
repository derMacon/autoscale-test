package de.dps.quarkusconsumer.controller;

import io.vertx.mutiny.core.eventbus.EventBus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Path("info")
public class StatusController {

    private int version = 1;

    @Inject
    EventBus bus;

    @Path("version")
    @GET
    public String version() throws IOException {
        return "version: " + version;
    }

    @Path("health")
    @GET
    public Boolean health() {
        return true;
    }


}