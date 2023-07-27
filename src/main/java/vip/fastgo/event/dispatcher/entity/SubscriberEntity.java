package vip.fastgo.event.dispatcher.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriberEntity {
    private Integer id;
    private String sn;
    private String name;
    private String url;
    private String event;
    private String token;
    private String comments;
    private Date createdData;
    private Date updatedData;
}
