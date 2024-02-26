package com.kimdev.SubwayNotify.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.kimdev.SubwayNotify.DTO.FindStationDTO;
import com.kimdev.SubwayNotify.DTO.SendPushDTO;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    FcmService fcmService;

    @Transactional
    @Scheduled(fixedDelay = 3000)
    public void sendAlarm() {

        // settedAlarm이 1 이상인 역들(알람이 하나 이상 설정된 역들)을 모두 찾는다.
        ArrayList<Station> stations = stationRepository.findAllBySettedAlarmGreaterThanEqual(1);

        stations.forEach(station -> {
            RestTemplate rt = new RestTemplate();
            String uri = "http://swopenAPI.seoul.go.kr/api/subway/" + HiddenData.subwayKey + "/json/realtimeStationArrival/0/10/" + station.getStationName();
            String response = rt.getForEntity(uri, String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode subways = objectMapper.readTree(response).get("realtimeArrivalList");

                for (JsonNode subway : subways) {
                    // 해당 열차가 전역을 출발했을 경우
                    if (subway.get("arvlCd").asInt() == 0 || subway.get("arvlCd").asInt() == 3) {
                        int subwayLine = subway.get("subwayId").asInt(); // 지하철 노선
                        int subwayNo = subway.get("btrainNo").asInt(); // 해당 열차 번호
                        String stationID = station.getStationId(); // 목적지 역 id
                        String goingRoute = subway.get("updnLine").asText(); // 상행 or 하행

                        // 해당 역, 열차를 가지고 설정된 알림이 있는지 검사해서 알림 전송
                        ArrayList<Alarm> alarms = alarmRepository.findAllBySubwayLineAndSubwayNoAndStationIDAndGoingRoute(subwayLine, subwayNo, stationID, goingRoute);

                        // 삭제할 알람 목록을 담을 리스트
                        List<Alarm> alarmsToRemove = new ArrayList<>();

                        alarms.forEach(alarm -> {
                            SendPushDTO sdto = new SendPushDTO();
                            sdto.setTitle("목적지 도착 알림");
                            sdto.setContent("탑승하신 열차가 목적지의 전역을 출발했습니다. 이번 정차역에서 내리시면 됩니다!");
                            sdto.setUser(alarm.getUser());
                            try {
                                fcmService.sendPush(sdto);
                            } catch (FirebaseMessagingException e) {
                                e.printStackTrace();
                            }
                            alarmsToRemove.add(alarm);

                            // 해당 역에 설정된 알림 개수를 하나씩 줄인다.
                            Station destination = stationRepository.findByStationId(alarm.getStationID());
                            destination.setSettedAlarm(destination.getSettedAlarm() - 1);
                        });
                        
                        // 푸시 알림 전송을 끝마친 DB 칼럼은 삭제한다
                        alarmRepository.deleteAll(alarmsToRemove);
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
