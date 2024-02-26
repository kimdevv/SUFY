package com.kimdev.SubwayNotify.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 알림을 설정한 유저
    @ManyToOne
    @JoinColumn(name="userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account user;

    // 탑승한 지하철 정보
    private int subwayLine;
    private int subwayNo;
    private String subwayName;

    // 목적지 역 ID, 이름
    private String stationID;
    private String stationName;

    // 상행인지 하행인지
    private String goingRoute;
}
