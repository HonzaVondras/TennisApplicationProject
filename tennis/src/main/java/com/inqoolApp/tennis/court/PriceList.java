package com.inqoolApp.tennis.court;

import java.util.HashMap;
import java.util.Map;

public class PriceList {
    private Map<SurfaceType, Double> prices = new HashMap<>();

    public void setPrice(SurfaceType type, double price) {
        prices.put(type, price);
    }

    public double getPrice(SurfaceType type) {
        return prices.getOrDefault(type, 0.0);
    }
}

