package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "results_archive")
public class TResultsArchive {

    @Id
    @Column(name = "Pid", nullable = false)
    private Integer pid;

    @Column(name = "Eventid")
    private String eventId;

    @Column(name = "Chipcode")
    private String chipCode;

    @Column(name = "Bib")
    private String bib;

    @Column(name = "Cat")
    private String cat;

    @Column(name = "Subcat")
    private String subCat;

    @Column(name = "Category")
    private String category;

    @Column(name = "Name", length = 500)
    private String name;

    @Column(name = "Chiname")
    private String chiName;

    @Column(name = "Teamno")
    private String teamNo;

    @Column(name = "Team")
    private String team;

    @Column(name = "Team1k")
    private String team1K;

    @Column(name = "Age")
    private String age;

    @Column(name = "Nationality")
    private String nationality;

    @Column(name = "Country")
    private String country;

    @Column(name = "sn")
    private String sn;

    @Column(name = "Race")
    private Short race;

    @Column(name = "Rank1cat")
    private Integer rank1cat;

    @Column(name = "Rank1mix")
    private Integer rank1mix;

    @Column(name = "Rank1tot")
    private Integer rank1tot;

    @Column(name = "Rank1team")
    private Integer rank1team;

    @Column(name = "Rank2team")
    private Integer rank2team;

    @Column(name = "Lap")
    private Integer lap;

    @Column(name = "Bonuslap")
    private Integer bonuslap;

    @Column(name = "DQlap")
    private Short dqLap;

    @Column(name = "NR")
    private Boolean nr;

    @Column(name = "DNS")
    private Boolean dns;

    @Column(name = "DNF")
    private Boolean dnf;

    @Column(name = "DQ")
    private Boolean dq;

    @Column(name = "FS")
    private Boolean fs;

    @Column(name = "NSBF")
    private Boolean nsbf;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "Timeteam")
    private Integer timeteam;

    @Column(name = "Timegun")
    private Integer timegun;

    @Column(name = "Timestart")
    private Integer timestart;

    @Column(name = "Timefinish")
    private Integer timefinish;

    @Column(name = "Timecp1")
    private Integer timecp1;

    @Column(name = "Timestart1k")
    private Integer timestart1k;

    @Column(name = "Timefinish1k")
    private Integer timefinish1k;

    @Column(name = "Timeteam1k")
    private Integer timeteam1k;

    @Column(name = "Timegun1k")
    private Integer timegun1k;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "Rank1cat1k")
    private Integer rank1cat1k;

    @Column(name = "Rank1mix1k")
    private Integer rank1mix1k;

    @Column(name = "Rank1tot1k")
    private Integer rank1tot1k;

    @Column(name = "Timecp2")
    private Integer timecp2;

    @Column(name = "Timecp3")
    private Integer timecp3;

    @Column(name = "Timecp4")
    private Integer timecp4;

    @Column(name = "Timecp5")
    private Integer timecp5;

    @Column(name = "Timecp6")
    private Integer timecp6;

    @Column(name = "Timecp7")
    private Integer timecp7;

    @Column(name = "Timecp8")
    private Integer timecp8;

    @Column(name = "Timecp9")
    private Integer timecp9;

    @Column(name = "Timecp10")
    private Integer timecp10;

    	@Column(name = "time1")
    private Integer time1;
    @Column(name = "time2")
    private Integer time2;
    @Column(name = "time3")
    private Integer time3;
    @Column(name = "time4")
    private Integer time4;
    @Column(name = "time5")
    private Integer time5;
    @Column(name = "time6")
    private Integer time6;
    @Column(name = "time7")
    private Integer time7;
    @Column(name = "time8")
    private Integer time8;
    @Column(name = "time9")
    private Integer time9;
    @Column(name = "time10")
    private Integer time10;	
	@Column(name = "time11")
    private Integer time11;
    @Column(name = "time12")
    private Integer time12;
    @Column(name = "time13")
    private Integer time13;
    @Column(name = "time14")
    private Integer time14;
    @Column(name = "time15")
    private Integer time15;
    @Column(name = "time16")
    private Integer time16;
    @Column(name = "time17")
    private Integer time17;
    @Column(name = "time18")
    private Integer time18;
    @Column(name = "time19")
    private Integer time19;
    @Column(name = "time20")
    private Integer time20;	
	@Column(name = "time21")
    private Integer time21;
    @Column(name = "time22")
    private Integer time22;
    @Column(name = "time23")
    private Integer time23;
    @Column(name = "time24")
    private Integer time24;
    @Column(name = "time25")
    private Integer time25;
    @Column(name = "time26")
    private Integer time26;
    @Column(name = "time27")
    private Integer time27;
    @Column(name = "time28")
    private Integer time28;
    @Column(name = "time29")
    private Integer time29;
    @Column(name = "time30")
    private Integer time30;	
	@Column(name = "time31")
    private Integer time31;
    @Column(name = "time32")
    private Integer time32;
    @Column(name = "time33")
    private Integer time33;
    @Column(name = "time34")
    private Integer time34;
    @Column(name = "time35")
    private Integer time35;
    @Column(name = "time36")
    private Integer time36;
    @Column(name = "time37")
    private Integer time37;
    @Column(name = "time38")
    private Integer time38;
    @Column(name = "time39")
    private Integer time39;
    @Column(name = "time40")
    private Integer time40;	
	@Column(name = "time41")
    private Integer time41;
    @Column(name = "time42")
    private Integer time42;
    @Column(name = "time43")
    private Integer time43;
    @Column(name = "time44")
    private Integer time44;
    @Column(name = "time45")
    private Integer time45;
    @Column(name = "time46")
    private Integer time46;
    @Column(name = "time47")
    private Integer time47;
    @Column(name = "time48")
    private Integer time48;
    @Column(name = "time49")
    private Integer time49;
    @Column(name = "time50")
    private Integer time50;	
	@Column(name = "time51")
    private Integer time51;
    @Column(name = "time52")
    private Integer time52;
    @Column(name = "time53")
    private Integer time53;
    @Column(name = "time54")
    private Integer time54;
    @Column(name = "time55")
    private Integer time55;
    @Column(name = "time56")
    private Integer time56;
    @Column(name = "time57")
    private Integer time57;
    @Column(name = "time58")
    private Integer time58;
    @Column(name = "time59")
    private Integer time59;
    @Column(name = "time60")
    private Integer time60;	
	@Column(name = "time61")
    private Integer time61;
    @Column(name = "time62")
    private Integer time62;
    @Column(name = "time63")
    private Integer time63;
    @Column(name = "time64")
    private Integer time64;
    @Column(name = "time65")
    private Integer time65;
    @Column(name = "time66")
    private Integer time66;
    @Column(name = "time67")
    private Integer time67;
    @Column(name = "time68")
    private Integer time68;
    @Column(name = "time69")
    private Integer time69;
    @Column(name = "time70")
    private Integer time70;	
	@Column(name = "time71")
    private Integer time71;
    @Column(name = "time72")
    private Integer time72;
    @Column(name = "time73")
    private Integer time73;
    @Column(name = "time74")
    private Integer time74;
    @Column(name = "time75")
    private Integer time75;
    @Column(name = "time76")
    private Integer time76;
    @Column(name = "time77")
    private Integer time77;
    @Column(name = "time78")
    private Integer time78;
    @Column(name = "time79")
    private Integer time79;
    @Column(name = "time80")
    private Integer time80;	
	@Column(name = "time81")
    private Integer time81;
    @Column(name = "time82")
    private Integer time82;
    @Column(name = "time83")
    private Integer time83;
    @Column(name = "time84")
    private Integer time84;
    @Column(name = "time85")
    private Integer time85;
    @Column(name = "time86")
    private Integer time86;
    @Column(name = "time87")
    private Integer time87;
    @Column(name = "time88")
    private Integer time88;
    @Column(name = "time89")
    private Integer time89;
    @Column(name = "time90")
    private Integer time90;	
	@Column(name = "time91")
    private Integer time91;
    @Column(name = "time92")
    private Integer time92;
    @Column(name = "time93")
    private Integer time93;
    @Column(name = "time94")
    private Integer time94;
    @Column(name = "time95")
    private Integer time95;
    @Column(name = "time96")
    private Integer time96;
    @Column(name = "time97")
    private Integer time97;
    @Column(name = "time98")
    private Integer time98;
    @Column(name = "time99")
    private Integer time99;
    @Column(name = "time100")
    private Integer time100;

    @Column(name = "Bib_1")
    private String bib1;

    @Column(name = "No")
    private Integer no;

    @Column(name = "Bib2")
    private String bib2;

    @Column(name = "Name2")
    private String name2;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "Name3", length = 50)
    private String name3;

    @Column(name = "Distance")
    private BigDecimal distance;

    @Column(name = "NRIC")
    private String nric;

    @Column(name = "company")
    private String company;
}
