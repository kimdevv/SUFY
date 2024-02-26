package com.kimdev.SubwayNotify.Repository;


import com.kimdev.SubwayNotify.model.Account;
import com.kimdev.SubwayNotify.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface StationRepository extends JpaRepository<Station, Integer> {
    Station findByStationId(String stationID);
    Station findBySubwayIdAndStationId(int subwayID, String stationID);

    ArrayList<Station> findAllByStationNameLikeAndSubwayId(String stationName, int subwayId);
    ArrayList<Station> findAllBySettedAlarmGreaterThanEqual(int settedAlarm);
}
