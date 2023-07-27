package vip.fastgo.event.dispatcher.listners;

import vip.fastgo.event.dispatcher.core.Subscriber;
import vip.fastgo.event.dispatcher.core.SubscriberRepo;
import vip.fastgo.event.dispatcher.entity.SubscriberEntity;
import vip.fastgo.event.dispatcher.mapper.SubscriberEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final SubscriberEntityMapper subscriberEntityMapper;

    public ApplicationStartedListener(SubscriberEntityMapper subscriberEntityMapper) {
        this.subscriberEntityMapper = subscriberEntityMapper;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("app started, load subscribers from db, event {}", event.getTimeTaken());
        List<SubscriberEntity> subscriberEntityList = subscriberEntityMapper.findAll();

        log.info("subscriber counter: {}", subscriberEntityList.size());

        subscriberEntityList.forEach(subscriberEntity -> {
            SubscriberRepo.getInstance().add(subscriberEntity.getEvent(), new Subscriber() {{
                setEventType(subscriberEntity.getEvent());
                setUrl(subscriberEntity.getUrl());
                setToken(subscriberEntity.getToken());
            }});
        });


    }
}
