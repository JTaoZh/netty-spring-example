/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zbum.example.socket.server.netty.handler;

import com.zbum.example.socket.server.netty.ChannelRepository;
import com.zbum.example.socket.server.web.entity.Device;
import com.zbum.example.socket.server.web.entity.Msg;
import com.zbum.example.socket.server.web.repository.DeviceRepository;
import com.zbum.example.socket.server.web.repository.MsgRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * event handler to process receiving messages
 *
 * @author Jibeom Jung
 */
@Component
@Qualifier("somethingServerHandler")
@ChannelHandler.Sharable
public class SomethingServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MsgRepository msgRepository;

    private static Logger logger = Logger.getLogger(SomethingServerHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");

        ctx.fireChannelActive();
        logger.debug(ctx.channel().remoteAddress());
        String channelKey = ctx.channel().remoteAddress().toString();
        channelRepository.put(channelKey, ctx.channel());

        Device device = deviceRepository.findDeviceByChannelKey(channelKey);
        if (device == null)
            deviceRepository.save(new Device(channelKey, true));
        else {
            device.setOnline(true);
            deviceRepository.save(device);
        }

        //initial write to client
        ctx.writeAndFlush("Your channel key is " + channelKey + "\n\r");

        logger.debug("Binded Channel Count is " + this.channelRepository.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String stringMessage = (String) msg;
        byte[] bytes = ((String) msg).getBytes();
        String channelKey = ctx.channel().remoteAddress().toString();
        logger.debug(channelKey + ": " + Arrays.toString(bytes));
        Device device = deviceRepository.findDeviceByChannelKey(channelKey);

        msgRepository.save(new Msg(device, stringMessage));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        //ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        Assert.notNull(ctx);

        String channelKey = ctx.channel().remoteAddress().toString();
        this.channelRepository.remove(channelKey);
        Device device = this.deviceRepository.findDeviceByChannelKey(channelKey);
        device.setOnline(false);
        this.deviceRepository.save(device);

        logger.debug("Binded Channel Count is " + this.channelRepository.size());
    }

    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
}
