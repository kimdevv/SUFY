package com.kimdev.SubwayNotify.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int subwayId;

    private String 호선이름;

    private String stationId;

    private String stationName;

    // 몇 개의 알람이 설정되어 있는지
    private int settedAlarm;
}
