package com.dugauguez.scrapathle;

import com.dugauguez.scrapathle.utils.OpenStreetMapUtils;
import org.junit.Test;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;


@Import(AppConfig.class)
public class OpenStreetMapUtilsTest {


    @Test
    public void GetCoordinates() {
        String address = "The White House, Washington DC";
        Map<String, Double> coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
        assertEquals("latitude", 38.8976998, coords.get("lat"));
        assertEquals("longitude", -77.0365534886228, coords.get("lon"));
    }

}
