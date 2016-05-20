package com.epam.dbrepositories;

import com.epam.entities.Accident;
import com.epam.entities.RoadSurface;
import com.epam.entities.WeatherCondition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AccidentRepository extends CrudRepository<Accident, String> {

    // declare your query methods for default and if you want to execute any custom queries use @Query annotation.
//    1. Find all the accidents by ID(Note: We can use findOne method which will accept the Accident ID as PK).
//    2. Find all the accidents count groupby all road surface conditions .
    @Query("from Accident a  where a.roadSurface.code = :roadConditionCode")
    Iterable<Accident> getAllAccidentsByRoadCondition(@Param("roadConditionCode") Integer roadConditionCode);

    //    3. Find all the accidents count groupby accident year and weather condition .( For eg: in year 2009 we need to know the number of accidents based on each weather condition).
    @Query("from Accident a where a.weatherCondition.code = :weatherConditionCode and concat(year(a.occurOn),'') = :year" )
    Iterable<Accident> getAllAccidentsByWeatherConditionAndYear(
            @Param("weatherConditionCode") Integer weatherConditionCode,
            @Param("year") String year);

    @Query("from Accident a where a.occurOn = ?")
    Iterable<Accident> getAllAccidentsByDate(Date date);


    //Boolean update(Accident roadAccident);


}
