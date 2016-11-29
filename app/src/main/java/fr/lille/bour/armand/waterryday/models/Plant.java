package fr.lille.bour.armand.waterryday.models;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Plant} class represents a plant saved by the user in the application.
 *
 * @author Armand (Tydax) BOUR
 */

public class Plant {

    protected static final String EXC_CAUSE_NEGATIVE_NULL_WATERINGFREQUENCY = "Cannot specify a negative or null number.";

    /** The current date can be changed for debug purposes. */
    protected static LocalDate CURRENT_DATE;

    static {
        CURRENT_DATE = new LocalDate();
    }

    /**
     * Gets the day of the currently set date.
     * @return The day of the date.
     */
    public static int getCurrentDay() {
        return CURRENT_DATE.getDayOfMonth();
    }

    /**
     * Gets the month of the currently set date.
     * @return The month of the date, ranging from 1 to 12.
     */
    public static int getCurrentMonth() {
        return CURRENT_DATE.getMonthOfYear();
    }

    /**
     * Gets the year of the currently set date.
     * @return The year of the date.
     */
    public static int getCurrentYear() {
        return CURRENT_DATE.getYear();
    }

    /**
     * Sets the current date.
     * @param date The new current date to use for comparison.
     */
    public static void setCurrentDate(final LocalDate date) {
        CURRENT_DATE = new LocalDate(date);
    }

    /** The plant's id in the database. */
    protected long id;
    /** The plant's name. */
    protected String name;
    /** The specie of the plant. */
    protected String specie;
    /** The location of the plant. */
    protected String location;

    /**
     * The watering frequency in days. Should be greater than or equal to 1.
     * The plant needs to be watered every <em>n</em> days.
     */
    protected int wateringFrequency;
    /** The date when the plant was watered for the last time. */
    protected LocalDate lastWateredDate;

    public Plant(final String name, final String specie, final String location,
                 final int wateringFrequency) {
        this(-1, name, specie, location, wateringFrequency, LocalDate.now());
    }

    /**
     * Creates a new plant with the specified parameters.
     *
     * @param id
     * @param name
     * @param specie
     * @param location
     * @param wateringFrequency
     * @param lastWateredDate
     * @throws IllegalArgumentException when
     */
    public Plant(int id, final String name, final String specie, final String location,
                 final int wateringFrequency, final LocalDate lastWateredDate)
                 throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.specie = specie;
        this.location = location;
        setWateringFrequency(wateringFrequency);
        this.lastWateredDate = new LocalDate(lastWateredDate);
    }

    /**
     * Gets the {@link #name} of the plant.
     * @return The name of the plant.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the {@link #name} of the plant.
     * @param name The new name of the plant.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the {@link #specie} of the plant.
     * @return The specie of the plant.
     */
    public String getSpecie() {
        return specie;
    }

    /**
     * Sets the {@link #specie} of the plant.
     * @param specie The new specie of the plant.
     */
    public void setSpecie(final String specie) {
        this.specie = specie;
    }

    /**
     * Gets the {@link #location} of the plant.
     * @return The location of the plant.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the {@link #location} of the plant.
     * @param location The new location of the plant.
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Gets the watering frequency (see {@link #wateringFrequency}) of the plant.
     * @return The watering frequency in days.
     */
    public int getWateringFrequency() {
        return wateringFrequency;
    }

    /**
     * Gets the {@link #id} of the plant in the database.
     * @return The id of the plant.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the {@link #id} of the plant in the database.
     * @param id The id of the plant in the database.
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Sets the watering frequency (see {@link #wateringFrequency}) of the plant.
     * @param wateringFrequency The new watering frequency of the plant.
     * @throws IllegalArgumentException When wateringFrequency is negative or equal to zero.
     */
    public void setWateringFrequency(final int wateringFrequency) throws IllegalArgumentException {
        if (wateringFrequency < 1) {
            throw new IllegalArgumentException(EXC_CAUSE_NEGATIVE_NULL_WATERINGFREQUENCY);
        }
        this.wateringFrequency = wateringFrequency;
    }

    /**
     * Gets the date when the plant has been watered for the last time.
     * @return The date.
     */
    public LocalDate getLastWateredDate() {
        return lastWateredDate;
    }

    /**
     * Waters the plant and updates {@link #lastWateredDate} to the current date.
     */
    public void water() {
        this.lastWateredDate = new LocalDate(CURRENT_DATE);
    }

    /**
     * Gets the current watering state of the plant (see {@link WateringState}).
     * @return The current watering state of the plant.
     */
    public WateringState getWateringState() {
        final int dayGap = Days.daysBetween(this.lastWateredDate, CURRENT_DATE).getDays();
        final int daysLeft = this.wateringFrequency - dayGap;

        if (dayGap == 0 || daysLeft > 1) {
           return WateringState.HYDRATED;
        }
        if (daysLeft < 0) {
            return WateringState.THIRSTY;
        } else {
            return WateringState.OKAY;
        }
    }

    /**
     * Generates a list of predefined plants.
     * @return A list of plants.
     */
    public static List<Plant> generatePlants() {
        final List<Plant> plants = new ArrayList<>();
        plants.add(new Plant(-1, "Germaine", "Géranium", "Cuisine", 3, LocalDate.now()));
        plants.add(new Plant(-1, "Raymonde", "Basilic", "Salon", 3, LocalDate.now().minusDays(10)));
        plants.add(new Plant(-1, "Robert", "Bananier", "Cuisine", 7, LocalDate.now().minusDays(3)));
        plants.add(new Plant(-1, "Ursula", "Sarracenia ", "Vestibule", 3, LocalDate.now().minusDays(3)));
        plants.add(new Plant(-1, "Léopoldine", "Sarracenia", "Vestibule", 2, LocalDate.now().minusDays(3)));
        plants.add(new Plant(-1, "Victor", "Droséra", "Vestibule", 1, LocalDate.now()));
        plants.add(new Plant(-1, "Louloute", "Hortensia", "Cuvette des WCs", 1, LocalDate.now().minusDays(2)));
        plants.add(new Plant(-1, "Jacinthe", "", "Micro-ondes", 3, LocalDate.now().minusDays(2)));
        plants.add(new Plant(-1, "Froufrou", "Dionée", "Dans mon chausson droit", 7, LocalDate.now().minusDays(6)));
        plants.add(new Plant(-1, "Xavière", "Droséra", "Frigo", 1, LocalDate.now()));
        plants.add(new Plant(-1, "Ulysse", "", "Sous mon diplôme de Master", 5, LocalDate.now().minusDays(5)));

        return plants;
    }

    /**
     * {@link WateringState} represents the state of watering of the plant.
     *
     * @author Armand (Tydax) BOUR
     */
    public enum WateringState {
        /** The plant does not need watering. */
        HYDRATED,
        /** The plant needs to be watered tomorrow or today. */
        OKAY,
        /** The plant needed to be watered earlier. */
        THIRSTY
    }
}
