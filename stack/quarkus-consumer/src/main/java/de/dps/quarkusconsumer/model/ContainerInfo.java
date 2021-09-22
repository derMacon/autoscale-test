package de.dps.quarkusconsumer.model;

import lombok.Getter;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Getter
public class ContainerInfo {

    private String id;

    public ContainerInfo() {
        id = UUID.randomUUID().toString();
    }

}
