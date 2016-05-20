package com.epam.dbrepositories;

import com.epam.AppConfig;
import com.epam.entities.WeatherCondition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by gavin on 16-5-20.
 */
@ContextConfiguration(locations = {"classpath:config/application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class WeatherConditionRepositoryTest {

    @Autowired
    WeatherConditionRepository repo;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSave(){
        WeatherCondition w = new WeatherCondition();
        w.setLabel("test");
        w.setCode(100);

        repo.save(w);

        WeatherCondition stored = repo.findOne(100);
        assertThat(stored, notNullValue());
        assertThat(stored.getCode(), is(100));
        assertThat(stored.getLabel(), equalTo("test"));


    }

}