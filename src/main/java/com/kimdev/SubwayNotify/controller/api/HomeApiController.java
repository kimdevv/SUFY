package com.kimdev.SubwayNotify.controller.api;

import com.kimdev.SubwayNotify.model.Alarm;
import com.kimdev.SubwayNotify.model.Station;
import com.kimdev.SubwayNotify.service.HomeService;
import com.kimdev.SubwayNotify.DTO.FindStationDTO;
import com.kimdev.SubwayNotify.DTO.saveAlarmToDBDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class HomeApiController {

    @Autowired
    HomeService homeService;

    @PostMapping("/findStation")
    public ArrayList<Station> findStation(@RequestBody FindStationDTO findStationDto) {
        ArrayList<Station> result = homeService.findStation(findStationDto);

        return result;
    }

    @GetMapping("/findSubway")
    public String findSubway(@RequestParam String stationName, int subwayID) {
        String result = homeService.findSubway(stationName, subwayID);

        return result;
    }

    @GetMapping("/findStationName")
    public String findStationName(@RequestParam String stationID) {
        String result = homeService.findStationName(stationID);

        return result;
    }

    @PostMapping("/saveAlarmToDB")
    public int saveAlarmToDB(@RequestBody saveAlarmToDBDTO saveAlarmToDBDto) {
        homeService.saveAlarmToDB(saveAlarmToDBDto);

        return 0;
    }

    @GetMapping("/alarmList")
    public ArrayList<Alarm> alarmList(@RequestParam String username) {
        ArrayList<Alarm> alarms = homeService.alarmList(username);

        return alarms;
    }

    @DeleteMapping("/alarmDelete")
    public void alarmDelete(@RequestParam int alarmId, @RequestParam int subwayLine, @RequestParam String stationID) {
        homeService.alarmDelete(alarmId, subwayLine, stationID);
    }

}
