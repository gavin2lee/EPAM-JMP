package com.epam.entities;

import javax.persistence.*;
import java.util.Date;
import java.sql.Time;

@Entity
public class Accident {
    private String accidentIndex;
    private Double longitude;
    private Double latitude;
    private PoliceForce policeForce;
    private AccidentSeverity accidentSeverity;
    private Integer numberOfVehicles;
    private Integer numberOfCasualties;
    private Date occurOn;
    private Integer dayOfWeek;
    private Time occurAt;
    private DistrictAuthority districtAuthority;
    private LightCondition lightCondition;
    private WeatherCondition weatherCondition;
    private RoadSurface roadSurface;
    private String timeosDay;

    @Id
    public String getAccidentIndex() {
        return accidentIndex;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public PoliceForce getPoliceForce() {
        return policeForce;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public AccidentSeverity getAccidentSeverity() {
        return accidentSeverity;
    }

    public Integer getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public Integer getNumberOfCasualties() {
        return numberOfCasualties;
    }

    public Date getOccurOn() {
        return occurOn;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Time getOccurAt() {
        return occurAt;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public DistrictAuthority getDistrictAuthority() {
        return districtAuthority;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public LightCondition getLightCondition() {
        return lightCondition;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(referencedColumnName = "code")
    public RoadSurface getRoadSurface() {
        return roadSurface;
    }

    public String getTimeosDay() {
        return timeosDay;
    }

    public void setAccidentIndex(String accidentIndex) {
        this.accidentIndex = accidentIndex;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setPoliceForce(PoliceForce policeForce) {
        this.policeForce = policeForce;
    }

    public void setAccidentSeverity(AccidentSeverity accidentSeverity) {
        this.accidentSeverity = accidentSeverity;
    }

    public void setNumberOfVehicles(Integer numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public void setNumberOfCasualties(Integer numberOfCasualties) {
        this.numberOfCasualties = numberOfCasualties;
    }

    public void setOccurOn(Date occurOn) {
        this.occurOn = occurOn;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setOccurAt(Time occurAt) {
        this.occurAt = occurAt;
    }

    public void setDistrictAuthority(DistrictAuthority districtAuthority) {
        this.districtAuthority = districtAuthority;
    }

    public void setLightCondition(LightCondition lightCondition) {
        this.lightCondition = lightCondition;
    }

    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public void setRoadSurface(RoadSurface roadSurface) {
        this.roadSurface = roadSurface;
    }


    public void setTimeosDay(String timeosDay) {
        this.timeosDay = timeosDay;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "accidentIndex='" + accidentIndex + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", policeForce=" + policeForce +
                ", accidentSeverity=" + accidentSeverity +
                ", numberOfVehicles=" + numberOfVehicles +
                ", numberOfCasualties=" + numberOfCasualties +
                ", occurOn=" + occurOn +
                ", dayOfWeek=" + dayOfWeek +
                ", occurAt=" + occurAt +
                ", districtAuthority=" + districtAuthority +
                ", lightCondition=" + lightCondition +
                ", weatherCondition=" + weatherCondition +
                ", roadSurface=" + roadSurface +
                ", timeosDay='" + timeosDay + '\'' +
                '}';
    }
}
