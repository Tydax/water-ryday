package fr.lille.bour.armand.waterryday;


import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.junit.*;

import fr.lille.bour.armand.waterryday.models.Plant;
import fr.lille.bour.armand.waterryday.models.Plant.WateringState;

import static junit.framework.Assert.assertEquals;

/**
 * Plant test.
 *
 * @author Armand (Tydax) BOUR
 */

public class PlantTest {

    private Plant plant1;
    private Plant plant3;
    private LocalDate date;

    @Before
    public void initialisePlants() {
        this.plant1 = new Plant("", "", "", 1);
        this.plant3 = new Plant("", "", "", 3);
        this.date = new LocalDate();
        Plant.setCurrentDate(this.date);
    }

    private LocalDate wasteOneDay() {
       return wasteNDays(1);
    }

    private LocalDate wasteNDays(final int n) {
        return this.date = this.date.withFieldAdded(DurationFieldType.days(), n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionWhenNegativeWateringFrequency() throws Exception {
        new Plant("", "", "", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionWhenNullWateringFrequency() throws Exception {
        new Plant("", "", "", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setterExceptionWhenNegativeWateringFrequency() throws Exception {
        this.plant1.setWateringFrequency(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setterExceptionWhenNullWateringFrequency() throws Exception {
        this.plant1.setWateringFrequency(0);
    }

    @Test
    public void constructorEverydayWateringState() {
        assertEquals(WateringState.HYDRATED, this.plant1.getWateringState());
    }

    @Test
    public void everydayWateringStateOkay() {
        // After 1 day, should be okay
        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.OKAY, this.plant1.getWateringState());
    }

    @Test
    public void everydayWateringStateThirsty() {
        // After 2 days, should be thirsty
        Plant.setCurrentDate(wasteNDays(2));
        assertEquals(WateringState.THIRSTY, this.plant1.getWateringState());

        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.THIRSTY, this.plant1.getWateringState());
    }

    @Test
    public void everydayWateringOkay() {
        // After 1 day, should be okay
        Plant.setCurrentDate(wasteOneDay());
        this.plant1.water();
        assertEquals(WateringState.HYDRATED, this.plant1.getWateringState());
    }

    @Test
    public void everydayWateringThirsty() {
        // After 2 days, should be thirsty
        Plant.setCurrentDate(wasteNDays(2));
        this.plant1.water();
        assertEquals(WateringState.HYDRATED, this.plant1.getWateringState());
    }

    @Test
    public void constructorEveryThreeDaysWateringState() {
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());
    }

    @Test
    public void everyThreeDaysWateringStateOkay() {
        // After 1 day, should be hydrated
        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());

        // After 2 days, should be okay
        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.OKAY, this.plant3.getWateringState());

        // After 3 days, should be okay
        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.OKAY, this.plant3.getWateringState());
    }

    @Test
    public void everyThreeDaysWateringStateThirsty() {
        // After 4 days, should be thirsty
        Plant.setCurrentDate(wasteNDays(4));
        assertEquals(WateringState.THIRSTY, this.plant3.getWateringState());

        // After 5 days, should be thirsty
        Plant.setCurrentDate(wasteOneDay());
        assertEquals(WateringState.THIRSTY, this.plant3.getWateringState());
    }

    @Test
    public void everyThreeDaysWateringOkay() {
        // After 2 days, should be okay
        Plant.setCurrentDate(wasteNDays(2));
        this.plant3.water();
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());

        // After 3 days, should be okay
        Plant.setCurrentDate(wasteNDays(3));
        this.plant3.water();
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());
    }

    @Test
    public void everyThreeDaysWateringThirsty() {
        // After 4 days, should be thirsty
        Plant.setCurrentDate(wasteNDays(4));
        this.plant3.water();
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());

        // After 5 days, should be thirsty
        Plant.setCurrentDate(wasteNDays( 5));
        this.plant3.water();
        assertEquals(WateringState.HYDRATED, this.plant3.getWateringState());
    }
}
