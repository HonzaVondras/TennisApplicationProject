package com.inqoolApp.tennis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.inqoolApp.tennis.court.PriceList;
import com.inqoolApp.tennis.court.SurfaceType;

public class PriceListTest {
    
    private static final PriceList priceList = new PriceList();

    
    @Test
	void priceListTest() {
        priceList.setPrice(SurfaceType.ARTIFICIAL_GRASS, 10.0);
        priceList.setPrice(SurfaceType.GRASS, 12.0);
        priceList.setPrice(SurfaceType.ASPHALT, 14.0);
        priceList.setPrice(SurfaceType.CLAY, 16.0);

        assertEquals(priceList.getPrice(SurfaceType.ARTIFICIAL_GRASS), 10.0);
        assertEquals(priceList.getPrice(SurfaceType.ASPHALT), 14.0);
        assertEquals(priceList.getPrice(SurfaceType.GRASS), 12.0);
        assertEquals(priceList.getPrice(SurfaceType.CLAY), 16.0);

    }
}
