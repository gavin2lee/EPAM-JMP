package com.epam.data;

import com.epam.TimeosDay;

public class EnrichedRoadAccident extends RoadAccident {
	private String forceContact;
	private TimeosDay timeosDay;

	EnrichedRoadAccident(EnrichedRoadAccidentBuilder builder) {
		super(builder);
		forceContact = builder.forceContact;
		timeosDay = builder.timeosDay;
	}

	public String getForceContact() {
		return forceContact;
	}

	public void setForceContact(String forceContact) {
		this.forceContact = forceContact;
	}

	public TimeosDay getTimeosDay() {
		return timeosDay;
	}

	public void setTimeosDay(TimeosDay timeosDay) {
		this.timeosDay = timeosDay;
	}
	
}
