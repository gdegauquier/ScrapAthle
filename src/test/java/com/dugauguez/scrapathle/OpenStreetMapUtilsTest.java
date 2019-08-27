package com.dugauguez.scrapathle;

import com.dugauguez.scrapathle.utils.OpenStreetMapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@Import(AppConfig.class)
@ComponentScan("com.dugauguez.scrapathle.utils")
public class OpenStreetMapUtilsTest {


    @Autowired
    OpenStreetMapUtils openStreetMapUtils;

    @Test
    public void GetCoordinates() {
        String address = "The White House, Washington DC";
        Map<String, Double> coords = openStreetMapUtils.getCoordinates(address);
        assertEquals("latitude", 38.8976998, coords.get("lat"));
        assertEquals("longitude", -77.0365534886228, coords.get("lon"));
    }

}
