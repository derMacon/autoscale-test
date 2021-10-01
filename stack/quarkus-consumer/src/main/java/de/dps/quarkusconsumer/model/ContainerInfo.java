package de.dps.quarkusconsumer.model;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

//@Singleton
@ApplicationScoped
public class ContainerInfo {

    private final String id;
//    private boolean appIsInitialized;

    public ContainerInfo() {
        id = UUID.randomUUID().toString();
//        this.appIsInitialized = false;
    }

    public String getId() {
        return id;
    }

//    public boolean isAppIsInitialized() {
//        return appIsInitialized;
//    }
//
//    public void setAppIsInitialized(boolean appIsInitialized) {
//        this.appIsInitialized = appIsInitialized;
//    }
}
