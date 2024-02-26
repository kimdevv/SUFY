package com.kimdev.SubwayNotify.service;

import com.kimdev.SubwayNotify.DTO.FindStationDTO;
import com.kimdev.SubwayNotify.DTO.saveAlarmToDBDTO;
import com.kimdev.SubwayNotify.Repository.AccountRepository;
import com.kimdev.SubwayNotify.Repository.AlarmRepository;
import com.kimdev.SubwayNotify.Repository.StationRepository;
import com.kimdev.SubwayNotify.hidden.HiddenData;
import com.kimdev.SubwayNotify.model.Account;
import com.kimdev.SubwayNotify.model.Alarm;
import com.kimdev.SubwayNotify.model.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class HomeService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    AlarmRepository alarmRepository;

    @Transactional
    public ArrayList<Station> findStation(FindStationDTO findStationDto) {
        String stationName = findStationDto.getStnName();
        int subwayId = findStationDto.getSubwayID();

        ArrayList<Station> result = stationRepository.findAllByStationNameLikeAndSubwayId("%"+stationName+"%", subwayId);

        if (result.size() == 0) { // 해당 이름을 포함하는 이름의 역이 없을 경우
            return null;
        } else {
            return result;
        }
    }

    @Transactional
    public String findSubway(String stationName, int subwayId) {
        RestTemplate rt = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> Request = new HttpEntity<>(params);

        String url = "http://swopenAPI.seoul.go.kr/api/subway/" + HiddenData.subwayKey + "/json/realtimeStationArrival/0/10/" + stationName;
        ResponseEntity<String> response = rt.exchange(
                url, // https://{요청할 서버 주소}
                HttpMethod.GET, // 요청할 방식
                Request, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        return response.getBody();
    }

    @Transactional(readOnly = true)
    public String findStationName(String stationID) {
        Station station = stationRepository.findByStationId(stationID);

        return station.getStationName();
    }

    @Transactional
    public void saveAlarmToDB(saveAlarmToDBDTO saveAlarmToDBDto) {
        String stationID = saveAlarmToDBDto.getStationID();
        Station station = stationRepository.findByStationId(stationID);
        station.setSettedAlarm(station.getSettedAlarm() + 1); // 해당 역에 설정된 알림의 개수를 1개 증가시킨다

        String username = saveAlarmToDBDto.getUsername();
        Account user = accountRepository.findByUsername(username);

        int subwayID = saveAlarmToDBDto.getSubwayID();
        int subwayNo = saveAlarmToDBDto.getSubwayNo();
        String subwayName = saveAlarmToDBDto.getSubwayName();
        String stationName = saveAlarmToDBDto.getStationName();
        String gointRoute = saveAlarmToDBDto.getGoingRoute();

        Alarm alarm = new Alarm();
        alarm.setUser(user);
        alarm.setSubwayLine(subwayID);
        alarm.setSubwayNo(subwayNo);
        alarm.setSubwayName(subwayName);
        alarm.setStationID(stationID);
        alarm.setStationName(stationName);
        alarm.setGoingRoute(gointRoute);


        alarmRepository.save(alarm);
    }

    @Transactional(readOnly = true)
    public ArrayList<Alarm> alarmList(String username) {
        Account user = accountRepository.findByUsername(username);
        ArrayList<Alarm> alarms = alarmRepository.findAllByUser(user);

        return alarms;
    }

    @Transactional
    public void alarmDelete(int alarmId, int subwayLine, String stationID) {
        // 해당 알림을 DB에서 삭제
        alarmRepository.deleteById(alarmId);

        // 역에 등록된 알림 개수를 하나 감소
        Station station = stationRepository.findBySubwayIdAndStationId(subwayLine, stationID);
        station.setSettedAlarm(station.getSettedAlarm() - 1);
    }

    public String findSubwayWithId(int subwayId) {
        String s_Name;
        switch (subwayId) {
            case 1001:
                s_Name = "1호선";
                break;
            case 1002:
                s_Name = "2호선";
                break;
            case 1003:
                s_Name = "3호선";
                break;
            case 1004:
                s_Name = "4호선";
                break;
            case 1005:
                s_Name = "5호선";
                break;
            case 1006:
                s_Name = "6호선";
                break;
            case 1007:
                s_Name = "7호선";
                break;
            case 1008:
                s_Name = "8호선";
                break;
            case 1009:
                s_Name = "9호선";
                break;
            case 1063:
                s_Name = "경의중앙선";
                break;
            case 1065:
                s_Name = "공항철도";
                break;
            case 1067:
                s_Name = "경춘선";
                break;
            case 1075:
                s_Name = "수인분당선";
                break;
            case 1077:
                s_Name = "신분당선";
                break;
            case 1081:
                s_Name = "경강선";
                break;
            case 1092:
                s_Name = "우이신설선";
                break;
            case 1093:
                s_Name = "서해선";
                break;
            default:
                s_Name = "Error";
                break;
        }
        return s_Name;
    }
}
