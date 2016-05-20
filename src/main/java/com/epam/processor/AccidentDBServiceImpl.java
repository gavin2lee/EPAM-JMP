package com.epam.processor;

import java.util.Date;

import com.epam.data.RoadAccident;
import com.epam.dbservice.AccidentService;
import com.epam.entities.Accident;
import com.epam.entities.RoadSurface;
import com.epam.entities.WeatherCondition;

public class AccidentDBServiceImpl implements AccidentService {


	@Override
	public Accident findOne(String accidentId) {
		return null;
	}

	@Override
	public Iterable<Accident> getAllAccidentsByRoadCondition(RoadSurface roadCondition) {
		return null;
	}

	@Override
	public Iterable<Accident> getAllAccidentsByWeatherConditionAndYear(WeatherCondition weatherCondition, String year) {
		return null;
	}

	@Override
	public Iterable<Accident> getAllAccidentsByDate(Date date) {
		return null;
	}

	@Override
	public Boolean update(Accident roadAccident) {
		return null;
	}
}
