package com.zbum.example.socket.server.web.repository;

import com.zbum.example.socket.server.web.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by tao on 2017/6/5.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    public Device findDeviceByChannelKey(String channelKey);
}
