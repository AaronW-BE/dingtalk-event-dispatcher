package vip.fastgo.event.dispatcher.web;

import vip.fastgo.event.dispatcher.core.Subscriber;
import vip.fastgo.event.dispatcher.core.SubscriberRepo;
import vip.fastgo.event.dispatcher.entity.SubscriberEntity;
import vip.fastgo.event.dispatcher.mapper.SubscriberEntityMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Data
class CreateSubscriberForm {
    private String subName;
    private String webhook;
    private String token;
    private String eventType;
}

@Slf4j
@Controller
@RequestMapping("subscriber")
public class SubscriberController {

    @Autowired
    SubscriberEntityMapper subscriberEntityMapper;

    public SubscriberController(SubscriberEntityMapper subscriberEntityMapper) {
        this.subscriberEntityMapper = subscriberEntityMapper;
    }

    @RequestMapping("get")
    @ResponseBody
    public String get() {
        SubscriberRepo instance = SubscriberRepo.getInstance();
        instance.fetch("check_in").forEach(subscriber -> {
            log.info("subscriber: {}", subscriber);
        });

        log.info("get none exist event type");
        instance.fetch("none").forEach(subscriber -> {
            log.info("subscriber: {}", subscriber);
        });
        return "ok";
    }

    @RequestMapping("add")
    @ResponseBody
    public String add() {
        SubscriberRepo instance = SubscriberRepo.getInstance();
        instance.add("check_in", new Subscriber("check_in", "http://localhost:8080/webhook", "123456"));
        return "containers: " + instance.fetch("check_in");
    }

    @GetMapping("del")
    public String del(@RequestParam("sn") String sn) {
        if (StringUtils.hasText(sn)) {
            subscriberEntityMapper.deleteBySn(sn);
        }
        return "redirect:configuration";
    }


    @RequestMapping("configuration")
    public String index(HttpServletRequest request, Model model) {

        model.addAttribute("subscribers", subscriberEntityMapper.findAll());
        return "subscriber-configuration";
    }

    @PostMapping("create")
    public String createSubscriber(CreateSubscriberForm form) {
        log.info("create subscriber: {}", form);

        String webhook = form.getWebhook();
        // check webhook is valid url
        if (!webhook.startsWith("http")) {
            log.error("invalid webhook: {}", webhook);
            return "subscriber-configuration";
        }

        // TODO save subscriber to database
        Subscriber subscriber = new Subscriber();
        subscriber.setUrl(webhook);
        subscriber.setToken(form.getToken());
        subscriber.setEventType("check_in");
        boolean validated = subscriber.validate();
        log.info("subscriber: {} is valid: {}", subscriber, validated);

        if (validated) {
            SubscriberEntity subscriberEntity = new SubscriberEntity();
            subscriberEntity.setSn(UUID.randomUUID().toString().replaceAll("-", ""));
            subscriberEntity.setName(form.getSubName());
            subscriberEntity.setUrl(form.getWebhook());
            subscriberEntity.setEvent(form.getEventType());
            subscriberEntity.setComments(form.getSubName());
            subscriberEntity.setToken(form.getToken());
            subscriberEntityMapper.insert(subscriberEntity);
        }

        return "redirect:/subscriber/configuration";
    }
}
