package com.kimdev.SubwayNotify.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FcmTokenDTO {
    private int accountId;
    private String fcmToken;
}
