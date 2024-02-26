package com.kimdev.SubwayNotify.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindStationDTO {
    private String stnName;
    private int subwayID;
}
