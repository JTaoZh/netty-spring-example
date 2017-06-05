package com.zbum.example.socket.server.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by tao on 2017/6/5.
 */

@Entity
public class Msg {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Device device;

    private byte[] msg;

    public Msg() {
    }

    public Msg(Device device, String msg) {
        this.device = device;
        this.msg = msg.getBytes();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getMsg() {
        return Arrays.toString(msg);
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }
}
