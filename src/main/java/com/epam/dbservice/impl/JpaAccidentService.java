package com.epam.dbservice.impl;

import com.epam.dbrepositories.AccidentRepository;
import com.epam.dbservice.AccidentService;
import com.epam.entities.Accident;
import com.epam.entities.RoadSurface;
import com.epam.entities.WeatherCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by gavin on 16-5-20.
 */
@Service
public class JpaAccidentService implements AccidentService {
    @Autowired
    private AccidentRepository accidentRepository;

    @Override
    public Accident findOne(String accidentId) {
        return accidentRepository.findOne(accidentId);
    }

    @Override
    public Iterable<Accident> getAllAccidentsByRoadCondition(RoadSurface roadCondition) {

        return accidentRepository.getAllAccidentsByRoadCondition(roadCondition.getCode());
    }

    @Override
    public Iterable<Accident> getAllAccidentsByWeatherConditionAndYear(WeatherCondition weatherCondition, String year) {
        return accidentRepository.getAllAccidentsByWeatherConditionAndYear(weatherCondition.getCode(), year);
    }

    @Override
    public Iterable<Accident> getAllAccidentsByDate(Date date) {
        return accidentRepository.getAllAccidentsByDate(date);
    }

    @Override
    public Boolean update(Accident roadAccident) {

        return (accidentRepository.save(roadAccident) != null);
    }
}
