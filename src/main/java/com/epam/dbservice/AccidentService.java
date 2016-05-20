package com.epam.dbservice;

import com.epam.entities.Accident;
import com.epam.entities.RoadSurface;
import com.epam.entities.WeatherCondition;

import java.util.Date;

public interface AccidentService {
	
	// scenario 1
    Accident findOne(String accidentId);
    
    // scenario 2
    Iterable<Accident> getAllAccidentsByRoadCondition(RoadSurface roadCondition);
    
    // scenario 3
    Iterable<Accident> getAllAccidentsByWeatherConditionAndYear(WeatherCondition weatherCondition, String year);
    
 // scenario 4
    Iterable<Accident> getAllAccidentsByDate(Date date);

    Boolean update(Accident roadAccident);

}
