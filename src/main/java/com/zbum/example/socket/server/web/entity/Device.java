package com.zbum.example.socket.server.web.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tao on 2017/6/5.
 */
@Entity
public class Device {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String channelKey;

    private boolean isOnline;

    @OneToMany(mappedBy =  "device")
    private Set<Msg> msgs = new HashSet<Msg>();

    public Device() {
    }

    public Device(String ip) {
        this.channelKey = ip;
        this.isOnline = false;
    }

    public Device(String ip, boolean isOnline) {
        this.channelKey = ip;
        this.isOnline = isOnline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Set<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(Set<Msg> msgs) {
        this.msgs = msgs;
    }
}
