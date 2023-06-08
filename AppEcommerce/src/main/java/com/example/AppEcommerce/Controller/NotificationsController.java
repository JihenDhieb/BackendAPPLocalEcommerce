package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.DeviceDto;
import com.example.AppEcommerce.Model.Device;
import com.example.AppEcommerce.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class NotificationsController {
    @Autowired
    NotificationService notificationService;
    @RequestMapping(value="/addDevice", method = RequestMethod.POST)
    public Device addDevice(@Valid @RequestBody DeviceDto deviceDto){
        return notificationService.addDevice(deviceDto.getToken(), deviceDto.getEmail());
    }


}
