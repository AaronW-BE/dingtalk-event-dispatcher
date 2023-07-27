package vip.fastgo.event.dispatcher.core;

import java.util.Map;

public abstract class DingtalkEventHandler {
    abstract boolean handle(Map<?, ?> content, String plainText);
}
