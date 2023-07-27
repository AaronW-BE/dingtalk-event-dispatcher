package vip.fastgo.event.dispatcher.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriberRepo {
    Map<String, List<Subscriber>> container;

    static final SubscriberRepo INSTANCE = new SubscriberRepo();

    private SubscriberRepo() {
        container = new HashMap<>();
    }

    public static SubscriberRepo getInstance() {
        return INSTANCE;
    }

    public List<Subscriber> fetch(String eventType) {
        if (!container.containsKey(eventType)) {
            return new ArrayList<>();
        }
        return container.get(eventType);
    }

    public synchronized void add(String eventType, Subscriber subscriber) {
        if (!container.containsKey(eventType)) {
            container.put(eventType, new ArrayList<>());
        }
        container.get(eventType).add(subscriber);
    }

}
