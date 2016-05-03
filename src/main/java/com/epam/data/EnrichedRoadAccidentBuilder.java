package com.epam.data;

import com.epam.TimeosDay;

public class EnrichedRoadAccidentBuilder extends RoadAccidentBuilder {
	String forceContact;
	TimeosDay timeosDay;

	public EnrichedRoadAccidentBuilder(RoadAccident ra) {
		super(ra.getAccidentId());
		this.withLongitude(ra.getLongitude())
        .withLatitude(ra.getLatitude())
        .withPoliceForce(ra.getPoliceForce())
        .withAccidentSeverity(ra.getAccidentSeverity())
        .withNumberOfVehicles(ra.getNumberOfVehicles())
        .withNumberOfCasualties(ra.getNumberOfCasualties())
        .withDate(ra.getDate())
        .withTime(ra.getTime())
        .withDistrictAuthority(ra.getDistrictAuthority())
        .withLightConditions(ra.getLightConditions())
        .withWeatherConditions(ra.getWeatherConditions())
        .withRoadSurfaceConditions(ra.getRoadSurfaceConditions());
	}
	
	public EnrichedRoadAccidentBuilder withForceContact(String forceContact){
		this.forceContact = forceContact;
		return this;
	}
	
	public EnrichedRoadAccidentBuilder withTimeosDay(TimeosDay timeosDay){
		this.timeosDay = timeosDay;
		return this;
	}
	
	public EnrichedRoadAccident build(){
        return new EnrichedRoadAccident(this);
    }

}
