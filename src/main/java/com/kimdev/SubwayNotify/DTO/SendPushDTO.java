package com.kimdev.SubwayNotify.DTO;

import com.kimdev.SubwayNotify.model.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SendPushDTO {
    private String title;
    private String content;
    private Account user;
}
