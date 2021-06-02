package io.spaship.operator.event;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class EventSourcingEngine2 {
    public EventSourcingEngine2(){
        System.out.println("\n");
        System.out.println("APPLICATION SCOPED BEAN ################################################## initialized " +
                "#################################### INSTANCE ID "+ UUID.randomUUID().toString());
        System.out.println("\n");
    }

    public void doesNothing(){
        System.out.println("PRINTS HELLOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW ");
    }
}
