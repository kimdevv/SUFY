package com.kimdev.SubwayNotify.controller.api;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.kimdev.SubwayNotify.DTO.FcmTokenDTO;
import com.kimdev.SubwayNotify.DTO.SendPushDTO;
import com.kimdev.SubwayNotify.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FcmApiController {

    @Autowired
    FcmService fcmService;

    @PutMapping("/saveFcmtoken")
    public int saveFcmtoken(@RequestBody FcmTokenDTO fcmTokenDto) {
        fcmService.saveFcmtoken(fcmTokenDto);
        return 1;
    }

    @PostMapping("/sendPush")
    public int sendPush(@RequestBody SendPushDTO sendPushDto) throws FirebaseMessagingException {
        fcmService.sendPush(sendPushDto);
        return 1;
    }
}
