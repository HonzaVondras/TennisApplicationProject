package com.inqoolApp.tennis.court;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that takes care of prices of SurfaceType
 *
 * @author Jan Vondrasek
*/

public class PriceList {
    private Map<SurfaceType, Double> prices = new HashMap<>();

    /**
    * Sets the price for a specific surface type.
    *
    * This method sets the price for the specified surface type in the internal map.
    *
    * @param type the surface type for which to set the price
    * @param price the price to set for the specified surface type
    */
    public void setPrice(SurfaceType type, double price) {
        prices.put(type, price);
    }

    /**
    * Retrieves the price for a specific surface type.
    *
    * This method retrieves the price for the specified surface type from the internal map.
    * If the price for the specified surface type is not found, it returns a default value of 0.0.
    *
    * @param type the surface type for which to retrieve the price
    * @return the price for the specified surface type, or 1.0 if the price is not found
    */
    public double getPrice(SurfaceType type) {
        return prices.getOrDefault(type, 1.0);
    }
}

