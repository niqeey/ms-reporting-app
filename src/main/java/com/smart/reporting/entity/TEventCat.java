package com.smart.reporting.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_event_cat")
public class TEventCat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Surrogate key for JPA (not in table, but needed for entity identity)

    @Column(name = "event_id", length = 36, nullable = false)
    private String eventId;

    @Column(length = 100, nullable = false)
    private String cat;

    @Column(length = 100, nullable = false)
    private String category;

    @Column(nullable = false, precision = 10, scale = 0)
    private BigDecimal distance;

    @Column(length = 100, nullable = false)
    private String race;

    @Column(length = 100, nullable = false)
    private String gender;

    @Column
    private Integer guntime;

    @Column
    private Integer guntime1;

    @Column
    private Integer guntime2;

    @Column
    private Integer guntime3;

    @Column
    private Integer guntime4;

    @Column
    private Integer guntime5;

    @Column
    private Integer guntime6;

    // Constructors
    public TEventCat() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getCat() { return cat; }
    public void setCat(String cat) { this.cat = cat; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getDistance() { return distance; }
    public void setDistance(BigDecimal distance) { this.distance = distance; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getGuntime() { return guntime; }
    public void setGuntime(Integer guntime) { this.guntime = guntime; }

    public Integer getGuntime1() { return guntime1; }
    public void setGuntime1(Integer guntime1) { this.guntime1 = guntime1; }

    public Integer getGuntime2() { return guntime2; }
    public void setGuntime2(Integer guntime2) { this.guntime2 = guntime2; }

    public Integer getGuntime3() { return guntime3; }
    public void setGuntime3(Integer guntime3) { this.guntime3 = guntime3; }

    public Integer getGuntime4() { return guntime4; }
    public void setGuntime4(Integer guntime4) { this.guntime4 = guntime4; }

    public Integer getGuntime5() { return guntime5; }
    public void setGuntime5(Integer guntime5) { this.guntime5 = guntime5; }

    public Integer getGuntime6() { return guntime6; }
    public void setGuntime6(Integer guntime6) { this.guntime6 = guntime6; }
}