package com.zbum.example.socket.server.web.controller;

import com.zbum.example.socket.server.netty.ChannelRepository;
import com.zbum.example.socket.server.web.entity.Device;
import com.zbum.example.socket.server.web.repository.DeviceRepository;
import com.zbum.example.socket.server.web.repository.MsgRepository;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by tao on 2017/6/5.
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MsgRepository msgRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @GetMapping
    public Collection<Device> list(){
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device one(@PathVariable(value = "id") Long id){
        return deviceRepository.findOne(id);
    }

    @GetMapping("/s")
    public Device searchOne(@RequestParam(value = "channelKey") String key){
        return deviceRepository.findDeviceByChannelKey(key);
    }

    @GetMapping("/{id}/push")
    public String pushData(@PathVariable(value = "id") Long id,
                           @RequestParam(value = "data") String data){
        String channelKey = deviceRepository.findOne(id).getChannelKey();
        Channel channel = channelRepository.get(channelKey);
        if (channel != null) {
            channel.writeAndFlush(data);
            return "success";
        } else {
            return "failed";
        }
    }
}
