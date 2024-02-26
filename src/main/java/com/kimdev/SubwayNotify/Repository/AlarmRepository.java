package com.kimdev.SubwayNotify.Repository;


import com.kimdev.SubwayNotify.model.Account;
import com.kimdev.SubwayNotify.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    ArrayList<Alarm> findAllBySubwayLineAndSubwayNoAndStationIDAndGoingRoute(int subwayLine, int subwayNo, String stationID, String goingRoute);
    ArrayList<Alarm> findAllByUser(Account user);
}
