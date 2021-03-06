package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Alin Cimpean
 */
public class MiRideApplication {
    // Static value
    private static final int carSize = 15; 
    private static final int refreshSize = 5; 
    private static final int minRefreshSize = 3; 
    
    private Car[] cars = new Car[carSize];
    private int itemCount = 0;
    private String[] availableCars;

    public MiRideApplication() {
        // seedData();
    }

    public String createCar(String id, String make, String model, String driverName, int numPassengers) {
        String validId = isValidId(id);

        if (isValidId(id).contains("Error:")) {
            return validId;
        }

        if (!checkIfCarExists(id)) {
            cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
            itemCount++;
            return "New Car added successfully for registion number: " + cars[itemCount - 1].getRegistrationNumber();
        }

        return "Error: Already exists in the system.";
    }

    // Create SilverServiceCar
    public String createSilverServiceCar(String id, String make, String model, String driverName, int numPassengers,
            double fee, String refresh) {
        String validId = isValidId(id);

        if (isValidId(id).contains("Error:")) {
            return validId;
        }
        
        // validate booking fee before create new car
        if(fee >= 3 ) {
            // read from refresh into listRefrehsment
            String [] listRefreshment = new String[refreshSize]; // 5
            
            StringTokenizer token = new StringTokenizer(refresh, ",");
            
            int post = 0;
            // check more than 3
            if(token.countTokens() >= minRefreshSize && token.countTokens() <= minRefreshSize) { // 3
                
                // add refreshment into array
                int pos = 0;
                while(token.hasMoreTokens()) {
                    listRefreshment[post] = token.nextToken();
                    pos++;
                }
                
                // check for duplicate
                boolean duplicate = false;
                for (int i = 0; i < pos; i++) {
                    for (int j = i + 1; j < listRefreshment.length; j++) {
                        if (listRefreshment[i].equals(listRefreshment[j])) {
                            duplicate = true;
                        }
                    }
                }
                
                // no duplicate found
                if(!duplicate) {
                    if (!checkIfCarExists(id)) {
                        cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, fee, listRefreshment);
                        itemCount++;
                        return "New Car added successfully for registion number: " + cars[itemCount - 1].getRegistrationNumber();
                    }
                } else {
                    return "Error: Duplicate refreshment!";
                }
            } else {
                return "Error: Need at least 3 refreshment!";
            }
        } else {
            return "Error: Booking fee must greater or equals to $3.00.";
        }

        return "Error: Already exists in the system.";
    }

    public String[] book(DateTime dateRequired) {
        int numberOfAvailableCars = 0;
        // finds number of available cars to determine the size of the array required.
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (!cars[i].isCarBookedOnDate(dateRequired)) {
                    numberOfAvailableCars++;
                }
            }
        }

        if (numberOfAvailableCars == 0) {
            String[] result = new String[0];
            return result;
        }

        availableCars = new String[numberOfAvailableCars];
        int availableCarsIndex = 0;
        // Populate available cars with registration numbers
        for (int i = 0; i < cars.length; i++) {

            if (cars[i] != null) {
                if (!cars[i].isCarBookedOnDate(dateRequired)) {
                    availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
                    availableCarsIndex++;
                }
            }
        }
        
        return availableCars;
    }

    public String book(String firstName, String lastName, DateTime required, int numPassengers,
            String registrationNumber) {
        Car car = getCarById(registrationNumber);
        if (car != null) {
            if (car.book(firstName, lastName, required, numPassengers)) {

                String message = "Thank you for your booking. \n" + car.getDriverName() + " will pick you up on "
                        + required.getFormattedDate() + ". \n" + "Your booking reference is: "
                        + car.getBookingID(firstName, lastName, required);
                return message;
            } else {
                String message = "Booking could not be completed.";
                return message;
            }
        } else {
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
    }

    public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) {
        String result = "";

        // Search all cars for bookings on a particular date.
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (cars[i].isCarBookedOnDate(dateOfBooking)) {
                    return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
                }
                // else
                // {
                //
                // }
                // if(!result.equals("Booking not found"))
                // {
                // return result;
                // }
            }
        }
        return "Booking not found.";
    }

    public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers) {
        String carNotFound = "Car not found";
        Car car = null;
        // Search for car with registration number
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (cars[i].getRegistrationNumber().equals(registrationNumber)) {
                    car = cars[i];
                    break;
                }
            }
        }

        if (car == null) {
            return carNotFound;
        }
        if (car.getBookingByName(firstName, lastName) != -1) {
            return car.completeBooking(firstName, lastName, kilometers);
        }
        return "Error: Booking not found.";
    }

    public boolean getBookingByName(String firstName, String lastName, String registrationNumber) {
        String bookingNotFound = "Error: Booking not found";
        Car car = null;
        // Search for car with registration number
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (cars[i].getRegistrationNumber().equals(registrationNumber)) {
                    car = cars[i];
                    break;
                }
            }
        }

        if (car == null) {
            return false;
        }
        if (car.getBookingByName(firstName, lastName) == -1) {
            return false;
        }
        return true;
    }

    public String displaySpecificCar(String regNo) {
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (cars[i].getRegistrationNumber().equals(regNo)) {
                    return cars[i].getDetails();
                }
            }
        }
        return "Error: The car could not be located.";
    }

    public boolean seedData() {
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                return false;
            }
        }
        // 2 cars not booked
        Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
        cars[itemCount] = honda;
        honda.book("Craig", "Cocker", new DateTime(1), 3);
        itemCount++;

        Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
        cars[itemCount] = lexus;
        lexus.book("Craig", "Cocker", new DateTime(1), 3);
        itemCount++;

        // 2 cars booked
        Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
        cars[itemCount] = bmw;
        itemCount++;
        bmw.book("Craig", "Cocker", new DateTime(1), 3);

        Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
        cars[itemCount] = audi;
        itemCount++;
        audi.book("Rodney", "Cocker", new DateTime(1), 4);

        // 1 car booked five times (not available)
        Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
        cars[itemCount] = toyota;
        itemCount++;
        toyota.book("Rodney", "Cocker", new DateTime(1), 3);
        toyota.book("Craig", "Cocker", new DateTime(2), 7);
        toyota.book("Alan", "Smith", new DateTime(3), 3);
        toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
        toyota.book("Paul", "Scarlett", new DateTime(5), 7);
        toyota.book("Paul", "Scarlett", new DateTime(6), 7);
        toyota.book("Paul", "Scarlett", new DateTime(7), 7);

        // 1 car booked five times (not available)
        Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
        cars[itemCount] = rover;
        itemCount++;
        rover.book("Rodney", "Cocker", new DateTime(1), 3);
        // rover.completeBooking("Rodney", "Cocker", 75);
        DateTime inTwoDays = new DateTime(2);
        rover.book("Rodney", "Cocker", inTwoDays, 3);
        rover.completeBooking("Rodney", "Cocker", inTwoDays, 75);
        return true;
    }

    public String displayAllBookings() {
        if (itemCount == 0) {
            return "No cars have been added to the system.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Summary of all cars: ");
        sb.append("\n");

        for (int i = 0; i < itemCount; i++) {
            sb.append(cars[i].getDetails());
        }
        return sb.toString();
    }

    public String displayBooking(String id, String seatId) {
        Car booking = getCarById(id);
        if (booking == null) {
            return "Booking not found";
        }
        return booking.getDetails();
    }

    public String isValidId(String id) {
        return MiRidesUtilities.isRegNoValid(id);
    }

    public String isValidPassengerCapacity(int passengerNumber) {
        return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
    }

    public boolean checkIfCarExists(String regNo) {
        
        if (regNo.length() != 6) {
            return false;
        }

        Car car = getCarById(regNo);

        if (car == null) {
            return false;
        } else {
            return true;
        }
    }

    private Car getCarById(String regNo) {
        Car car = null;

        for (int i = 0; i < cars.length; i++) {
            if (cars[i] != null) {
                if (cars[i].getRegistrationNumber().equals(regNo)) {
                    car = cars[i];
                    return car;
                }
            }
        }
        return car;
    }
    
    public void writeToFile() {
        PrintWriter pw = null;
        
        try {
            pw = new PrintWriter(new File("main.txt"));

            if (itemCount > 0) {
                for (int x = 0; x < itemCount; x++) {
                    pw.write(cars[x].toString());
                    pw.println();
                }
            }

            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
