package com.epam.dbrepositories;

import com.epam.entities.WeatherCondition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gavin on 16-5-13.
 */
@Repository
public interface WeatherConditionRepository extends CrudRepository<WeatherCondition,Integer>{

}
