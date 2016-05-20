package com.epam.dbrepositories;

import com.epam.entities.Accident;
import com.epam.entities.RoadSurface;
import com.epam.entities.TimeosDay;
import com.epam.entities.WeatherCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

/**
 * Created by gavin on 16-5-20.
 */
@ContextConfiguration(locations = {"classpath:config/application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class AccidentRepositoryTest {

    public static final String LOCAL_TIME_FORMAT = "HH:mm:ss:SSS";

    public static final Time ZERO_MIN = Time.valueOf("00:00:00");
    public static final Time ZERO_MAX = Time.valueOf("23:59:59");
    public static final Time SIX = Time.valueOf("06:00:00");
    public static final Time TWELVE = Time.valueOf("12:00:00");
    public static final Time EIGHTEEN = Time.valueOf("18:00:00");

    @Autowired
    AccidentRepository accidentRepository;



    @Test
    public void findOne(){
        WeatherCondition w = new WeatherCondition();
        w.setCode(100);
        w.setLabel("weather condition - test");

        Accident a = new Accident();
        a.setAccidentIndex("a10000");
        a.setLatitude(1000.0);
        a.setWeatherCondition(w);

        accidentRepository.save(a);

        Accident stored = accidentRepository.findOne("a10000");
        assertThat(stored, notNullValue());
        assertThat(stored.getAccidentIndex(), equalTo("a10000"));

        assertThat(stored.getAccidentSeverity(), nullValue());

        assertThat(stored.getWeatherCondition(), notNullValue());
        assertThat(stored.getWeatherCondition().getCode(), is(100));
    }


    @Test
    public void getAllAccidentsByRoadCondition() throws Exception {
        WeatherCondition w = new WeatherCondition();
        w.setCode(100);
        w.setLabel("weather condition - test");

        Accident a1 = new Accident();
        a1.setAccidentIndex("a1001");
        a1.setLatitude(1000.0);
        a1.setWeatherCondition(w);

        accidentRepository.save(a1);

        RoadSurface r = new RoadSurface();
        r.setCode(200);
        r.setLabel("road surface condition - test");

        Accident a2 = new Accident();
        a2.setAccidentIndex("a1002");
        a2.setRoadSurface(r);

        accidentRepository.save(a2);

        Iterable<Accident> accidentIterable = accidentRepository.getAllAccidentsByRoadCondition(200);

        assertThat(accidentIterable, notNullValue());
        accidentIterable.forEach((acc)->{
            assertThat(acc.getAccidentIndex(), equalTo("a1002"));
            assertThat(acc.getRoadSurface(), notNullValue());
            assertThat(acc.getRoadSurface().getCode(), is(200));
        });

    }

    @Test
    public void getAllAccidentsByWeatherConditionAndYear() throws Exception {
        WeatherCondition w = new WeatherCondition();
        w.setCode(100);
        w.setLabel("weather condition - test");

        Accident a = new Accident();
        a.setAccidentIndex("a10000");
        a.setLatitude(1000.0);
        a.setWeatherCondition(w);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        a.setOccurOn(df.parse("10/6/2009"));

        accidentRepository.save(a);

        Iterable<Accident> falseAccidentIterable = accidentRepository.getAllAccidentsByWeatherConditionAndYear(100, "2010");

        assertThat(falseAccidentIterable, notNullValue());
        assertThat(falseAccidentIterable.iterator().hasNext(), is(false));

        Iterable<Accident> trueAccidentIterable = accidentRepository.getAllAccidentsByWeatherConditionAndYear(100, "2009");
        assertThat(trueAccidentIterable, notNullValue());
        assertThat(trueAccidentIterable.iterator().hasNext(), is(true));

        trueAccidentIterable.forEach((element)->{
            assertThat(element, notNullValue());
            assertThat(element.getAccidentIndex(), equalTo("a10000"));
        });

    }

    @Test
    public void getAllAccidentsByDate() throws Exception {
        WeatherCondition w = new WeatherCondition();
        w.setCode(100);
        w.setLabel("weather condition - test");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date occurOn = df.parse("10/6/2009");


        Accident a1 = new Accident();
        a1.setAccidentIndex("a10000");
        a1.setLatitude(1000.0);
        a1.setWeatherCondition(w);
        a1.setOccurOn(occurOn);

        Accident a2 = new Accident();
        a2.setAccidentIndex("a10001");
        a2.setLatitude(90.0);
        a2.setWeatherCondition(w);
        a2.setOccurOn(occurOn);

        Accident a3 = new Accident();
        a3.setAccidentIndex("a10002");
        a3.setLatitude(90.0);
        a3.setWeatherCondition(w);
        a3.setOccurOn(df.parse("19/8/2009"));

        accidentRepository.save(a1);
        accidentRepository.save(a2);
        accidentRepository.save(a3);

        Iterable<Accident> stored = accidentRepository.getAllAccidentsByDate(occurOn);

        List<Accident> foundAccidents = new ArrayList<>();
        stored.forEach((a)->{
            assertThat(a, notNullValue());
            foundAccidents.add(a);
        });

        assertThat(foundAccidents.isEmpty(), is(false));
        assertThat(foundAccidents.size(), is(2));

    }

    @Test
    public void update() throws Exception {
        WeatherCondition w = new WeatherCondition();
        w.setCode(100);
        w.setLabel("weather condition - test");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date occurOn = df.parse("10/6/2009");


        Accident a1 = new Accident();
        a1.setAccidentIndex("a10000");
        a1.setLatitude(1000.0);
        a1.setWeatherCondition(w);
        a1.setOccurOn(occurOn);
        a1.setOccurAt(Time.valueOf("10:11:30"));

        Accident a2 = new Accident();
        a2.setAccidentIndex("a10001");
        a2.setLatitude(90.0);
        a2.setWeatherCondition(w);
        a2.setOccurOn(occurOn);
        a2.setOccurAt(Time.valueOf("3:09:22"));

        Accident a3 = new Accident();
        a3.setAccidentIndex("a10002");
        a3.setLatitude(90.0);
        a3.setWeatherCondition(w);
        a3.setOccurOn(df.parse("19/8/2009"));
        a3.setOccurAt(Time.valueOf("6:9:30"));

        accidentRepository.save(a1);
        accidentRepository.save(a2);
        accidentRepository.save(a3);

        Iterable<Accident> stored = accidentRepository.getAllAccidentsByDate(df.parse("10/6/2009"));

        stored.forEach((a)->{
            assertThat(a, notNullValue());
            TimeosDay timeosDay = decideTimeosDay(a.getOccurAt());
            a.setTimeosDay(timeosDay.name());
            accidentRepository.save(a);
        });

        Accident expected = accidentRepository.findOne("a10000");
        assertThat(expected, notNullValue());
        assertThat(expected.getTimeosDay(), notNullValue());
        assertThat(expected.getTimeosDay(), equalTo(TimeosDay.MORNING.name()));

    }

    private TimeosDay decideTimeosDay(Time time){
        if(time == null){
            return null;
        }

        if ((time.compareTo(ZERO_MIN) >= 0) && (time.compareTo(SIX) < 0)) {
            return TimeosDay.NIGHT;
        } else if ((time.compareTo(SIX) >= 0) && (time.compareTo(TWELVE) < 0)) {
            return TimeosDay.MORNING;
        } else if ((time.compareTo(TWELVE) >= 0) && (time.compareTo(EIGHTEEN) < 0)) {
            return TimeosDay.AFTERNOON;
        } else if ((time.compareTo(EIGHTEEN) >= 0) && (time.compareTo(ZERO_MAX) <= 0)) {
            return TimeosDay.EVENING;
        } else {
            return null;
        }
    }

}