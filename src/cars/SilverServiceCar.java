package cars;

import java.util.Scanner;
import utilities.DateTime;
import utilities.DateUtilities;

public class SilverServiceCar extends Car {
    private String[] refreshments;
    private double bookingFee = 3.50;
    private double tripFee = 0;

    public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity,
            double bookingFee, String[] refreshments) {

        super(regNo, make, model, driverName, passengerCapacity);

//        if (bookingFee >= 3) {
            this.bookingFee = bookingFee;
//        }
        this.refreshments = refreshments;
    }

    public static boolean dateIsNotMoreThan3Days(DateTime date) {
        boolean within3Days = false;
        DateTime today = new DateTime();
        DateTime nextWeek = new DateTime(3);

        int daysInFuture = DateTime.diffDays(nextWeek, date);
        if (daysInFuture > 0 && daysInFuture < 4) {
            within3Days = true;
        }
        return within3Days;
    }

    @Override
    public boolean book(String firstName, String lastName, DateTime required, int numPassengers) {
        boolean dateValid = dateIsValid(required);
        return super.book(firstName, lastName, required, numPassengers);
    }

    private boolean dateIsValid(DateTime date) {
        return DateUtilities.dateIsNotInPast(date) && dateIsNotMoreThan3Days(date);
    }

    @Override
    public String getDetails() {
        String Details = super.getDetails() + String.format("\n%-15s %s", "Standard Fee:", this.bookingFee)
                + "\nRefreshments Available:\n";
        
        for (int i = 0; i < refreshments.length; i++) {
            if (refreshments[i] != null) {
                Details += "Item " + Integer.toString(i + 1) + " " + refreshments[i] + "\n";
            }
        }

        return Details;
    }

    @Override
    public String toString() {
        String Details = super.toString() + ":" + this.bookingFee;
        for (int i = 0; i < refreshments.length; i++) {
            if (refreshments[i] != null) {
                Details += ":Item " + Integer.toString(i + 1) + " " + refreshments[i];
            }
        }

        return Details;
    }

}