package vip.fastgo.event.dispatcher.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import vip.fastgo.event.dispatcher.configuration.AppConfiguration;
import vip.fastgo.event.dispatcher.core.support.DingCallbackCrypto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
public class Dispatcher {
    private final SubscriberRepo subscriberRepo;

    private final AppConfiguration configuration;


    public Dispatcher(AppConfiguration appConfiguration) {
        this.configuration = appConfiguration;
        subscriberRepo = SubscriberRepo.getInstance();
    }

    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws IOException, DingCallbackCrypto.DingTalkEncryptException {
        log.info("Dispatcher.handle");
        response.setStatus(200);

        String signature = request.getParameter("signature");
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        // get body from request
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        log.info("body: {}", body);

        if (!StringUtils.hasText(body)) {
            log.error("body is empty, ignoring the request");
            return false;
        }

        String encrypt = new ObjectMapper().readTree(body).get("encrypt").asText();

        DingCallbackCrypto crypto = new DingCallbackCrypto(
                configuration.getAesToken(), configuration.getAesKey(), configuration.getAppKey()
        );

        String decryptMsg = crypto.getDecryptMsg(msgSignature, timestamp, nonce, encrypt);

        log.info("decryptMsg: {}", decryptMsg);

        String eventType = new ObjectMapper().readTree(decryptMsg).get("EventType").asText();

        if (eventType.equals("check_url")) {
            log.info("check_url");
            replyDingtalkServer(response, crypto, "success");

            log.info("check url success");
            return false;
        }

        // dispatch event to subscribers
        List<Subscriber> subscribers = subscriberRepo.fetch(eventType);
        if (subscribers == null) {
            log.error("no subscriber for event type: {}", eventType);
            return false;
        }

        Map<?, ?> content = new ObjectMapper().readValue(decryptMsg, Map.class);
        log.info("will handle msg");
        subscribers.forEach(subscriber -> {

            boolean handled = subscriber.handle(content, decryptMsg);

            log.info("event handle result: {}", handled);
        });

        // reply dingtalk server
        replyDingtalkServer(response, crypto, "success");

        return false;
    }

    private void replyDingtalkServer(HttpServletResponse response, DingCallbackCrypto crypto, String msg) throws IOException, DingCallbackCrypto.DingTalkEncryptException {
        Map<String, String> responseBody = crypto.getEncryptedMap(msg);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
}
