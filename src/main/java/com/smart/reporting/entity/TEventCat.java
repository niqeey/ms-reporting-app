package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "t_event_cat")
public class TEventCat {

    @Id
    @Column(name = "cat_id")
    private Integer catId;

    @Column(name = "event_id", length = 36, nullable = false)
    private String eventId;

    @Column(name = "cat", length = 100, nullable = false)
    private String cat;

    @Column(name = "category", length = 100, nullable = false)
    private String category;

    @Column(name = "distance", nullable = false, precision = 10, scale = 0)
    private BigDecimal distance;

    @Column(name = "race", length = 100, nullable = false)
    private String race;

    @Column(name = "gender", length = 100, nullable = false)
    private String gender;

    @Column(name = "Timegun")
    private Integer timegun;

    @Column(name = "Timegun1")
    private Integer timegun1;

    @Column(name = "Timegun2")
    private Integer timegun2;

    @Column(name = "Timegun3")
    private Integer timegun3;

    @Column(name = "Timegun4")
    private Integer timegun4;

    @Column(name = "Timegun5")
    private Integer timegun5;

    @Column(name = "Timegun6")
    private Integer timegun6;

    @Column(name = "CPLIST", length = 200)
    private String cplist;

    @Column(name = "is_Lap")
    private Byte isLap;

    @Column(name = "racemode", length = 100)
    private String racemode;

    @Column(name = "Top")
    private Integer top;

    @Column(name = "is_Live")
    private Integer isLive;

    @Column(name = "is_result")
    private Integer isResult;

}