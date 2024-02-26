package com.kimdev.SubwayNotify.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class saveAlarmToDBDTO {
    private String username;
    private int subwayID;
    private int subwayNo;
    private String subwayName;
    private String stationID;
    private String stationName;
    private String goingRoute;
}
