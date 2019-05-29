package app;

import java.util.Scanner;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Author:		Alin Cimpean
 */
public class Menu {
    private Scanner console;// = new Scanner(System.in);
    private MiRideApplication application = new MiRideApplication();
    // Allows me to turn validation on/off for testing business logic in the
    // classes.
    private boolean testingWithValidation = true;

    /*
     * Runs the menu in a loop until the user decides to exit the system.
     */
    public void run() {
        boolean run = true;
        
        do {
            printMenu();
            
            console = new Scanner(System.in);
            String input = console.nextLine().toUpperCase();

            switch (input) {
            case "CC":
                createCar();
                break;
            case "BC":
                book();
                break;
            case "CB":
                completeBooking();
                break;
            case "DA":
                displayAllBookings();
//                System.out.println(application.displayAllBookings());
                break;
            case "SS":
                System.out.print("Enter Registration Number: ");
                System.out.println(application.displaySpecificCar(console.nextLine()));
                break;
            case "SA":
                searchForAvailable();
                break;
            case "SD":
                application.seedData();
                break;
            case "EX":
                System.out.println("Exiting Program ... Goodbye!");
                writeToFile();
                run = false;
                break;
            default:
                System.out.println("Error, invalid option selected!");
                System.out.println("Please try Again...");
                break;
            }
        } while (run);
    }

    /*
     * Creates cars for use in the system available or booking.
     */
    private void createCar() {
        // String id = "", make, model, driverName;
        // int numPassengers = 0;
        String carRegistrationNumber = "";

        System.out.print("Enter registration number: ");
        String id = promptUserForRegNo();

        if (id.length() != 0) {
            // Validate before proceed
            boolean result = application.checkIfCarExists(id);

            if (!result) {
                // Get details required for creating a car.
                System.out.print("Enter Make: ");
                String make = console.nextLine();

                System.out.print("Enter Model: ");
                String model = console.nextLine();

                System.out.print("Enter Driver Name: ");
                String driverName = console.nextLine();

                System.out.print("Enter number of passengers: ");
                int numPassengers = promptForPassengerNumbers();

                System.out.print("Enter Service Type (SD/SS): ");
                String serviceType = console.next().trim().toUpperCase();

                switch (serviceType) {
                case "SD":
                    carRegistrationNumber = application.createCar(id, make, model, driverName, numPassengers);
                    break;
                case "SS":
                    System.out.print("Enter Standard Fee: ");
                    double fee = console.nextDouble();

                    System.out.print("Enter List of Refreshments: ");
                    String refreshments = console.next().trim();

                    carRegistrationNumber = application.createSilverServiceCar(id, make, model, driverName,
                            numPassengers, fee, refreshments);
                    break;
                default:
                    break;
                }

                System.out.println(carRegistrationNumber);
            } else {
                System.out.println("Error - Already exists in the system");
            }
        }
    }

    /*
     * Book a car by finding available cars for a specified date.
     */
    private boolean book() {
        System.out.println("Enter date car required: ");
        System.out.println("format (DD/MM/YYYY)");
        String dateEntered = console.nextLine();
        int day = Integer.parseInt(dateEntered.substring(0, 2));
        int month = Integer.parseInt(dateEntered.substring(3, 5));
        int year = Integer.parseInt(dateEntered.substring(6));
        DateTime dateRequired = new DateTime(day, month, year);

        if (!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThan7Days(dateRequired)) {
            System.out.println("Date is invalid, must be within the coming week.");
            return false;
        }

        String[] availableCars = application.book(dateRequired);
        for (int i = 0; i < availableCars.length; i++) {
            System.out.println(availableCars[i]);
        }
        if (availableCars.length != 0) {
            System.out.println("Please enter a number from the list:");
            int itemSelected = Integer.parseInt(console.nextLine());

            String regNo = availableCars[itemSelected - 1];
            regNo = regNo.substring(regNo.length() - 6);
            System.out.println("Please enter your first name:");
            String firstName = console.nextLine();
            System.out.println("Please enter your last name:");
            String lastName = console.nextLine();
            System.out.println("Please enter the number of passengers:");
            int numPassengers = Integer.parseInt(console.nextLine());
            String result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);

            System.out.println(result);
        } else {
            System.out.println("There are no available cars on this date.");
        }
        return true;
    }

    /*
     * Complete bookings found by either registration number or booking date.
     */
    private void completeBooking() {
        System.out.print("Enter Registration or Booking Date:");
        String response = console.nextLine();

        String result;
        // User entered a booking date
        if (response.contains("/")) {
            System.out.print("Enter First Name:");
            String firstName = console.nextLine();
            System.out.print("Enter Last Name:");
            String lastName = console.nextLine();
            System.out.print("Enter kilometers:");
            double kilometers = Double.parseDouble(console.nextLine());
            int day = Integer.parseInt(response.substring(0, 2));
            int month = Integer.parseInt(response.substring(3, 5));
            int year = Integer.parseInt(response.substring(6));
            DateTime dateOfBooking = new DateTime(day, month, year);
            result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
            System.out.println(result);
        } else {

            System.out.print("Enter First Name:");
            String firstName = console.nextLine();
            System.out.print("Enter Last Name:");
            String lastName = console.nextLine();
            if (application.getBookingByName(firstName, lastName, response)) {
                System.out.print("Enter kilometers:");
                double kilometers = Double.parseDouble(console.nextLine());
                result = application.completeBooking(firstName, lastName, response, kilometers);
                System.out.println(result);
            } else {
                System.out.println("Error: Booking not found.");
            }
        }
    }
    
    private void displayAllBookings() {
        
        System.out.print("Enter Type (SD/SS): ");
        String carType = console.next().trim().toUpperCase();
        
        if(carType.equalsIgnoreCase("SS") || carType.equalsIgnoreCase("SD")) {
            
            System.out.print("Enter Sort Order (A/D): ");
            String sort = console.next().trim().toUpperCase();
            
            if(sort.equalsIgnoreCase("A") || sort.equalsIgnoreCase("D")) {
                System.out.println(application.displayAllBookings());
            } else {
                System.out.println("Error");
            }
        } else {
            System.out.println("error");
        }
    }

    private void searchForAvailable() {

        System.out.print("Enter Type (SD/SS): ");
        String carType = console.next().trim().toUpperCase();

        if(carType.equalsIgnoreCase("SD") || carType.equalsIgnoreCase("SS")) {
            
            System.out.print("Date format (DD/MM/YYYY): ");
            String searchDate = console.next().trim();
            
            switch (carType) {
            case "SD":
                break;
            case "SS":
                break;
            default:
                break;
            }
        } else {
            System.out.println("Invalid type!");
        }
    }

    private int promptForPassengerNumbers() {
        int numPassengers = 0;
        boolean validPassengerNumbers = false;
        // By pass user input validation.
        if (!testingWithValidation) {
            return Integer.parseInt(console.nextLine());
        } else {
            while (!validPassengerNumbers) {
                numPassengers = Integer.parseInt(console.nextLine());

                String validId = application.isValidPassengerCapacity(numPassengers);
                if (validId.contains("Error:")) {
                    System.out.println(validId);
                    System.out.println("Enter passenger capacity: ");
                    System.out.println("(or hit ENTER to exit)");
                } else {
                    validPassengerNumbers = true;
                }
            }
            return numPassengers;
        }
    }

    /*
     * Prompt user for registration number and validate it is in the correct form.
     * Boolean value for indicating test mode allows by passing validation to test
     * program without user input validation.
     */
    private String promptUserForRegNo() {
        String regNo = "";
        boolean validRegistrationNumber = false;
        // By pass user input validation.
        if (!testingWithValidation) {
            return console.nextLine();
        } else {
            while (!validRegistrationNumber) {
                regNo = console.nextLine();
                boolean exists = application.checkIfCarExists(regNo);
                if (exists) {
                    // Empty string means the menu will not try to process
                    // the registration number
                    System.out.println("Error: Reg Number already exists");
                    return "";
                }
                if (regNo.length() == 0) {
                    break;
                }

                String validId = application.isValidId(regNo);
                if (validId.contains("Error:")) {
                    System.out.println(validId);
                    System.out.println("Enter registration number: ");
                    System.out.println("(or hit ENTER to exit)");
                } else {
                    validRegistrationNumber = true;
                }
            }
            return regNo;
        }
    }

    /*
     * Prints the menu.
     */
    private void printMenu() {
        System.out.printf("\n********** MiRide System Menu **********\n\n");

        System.out.printf("%-30s %s\n", "Create Car", "CC");
        System.out.printf("%-30s %s\n", "Book Car", "BC");
        System.out.printf("%-30s %s\n", "Complete Booking", "CB");
        System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
        System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
        System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
        System.out.printf("%-30s %s\n", "Seed Data", "SD");
        System.out.printf("%-30s %s\n", "Exit Program", "EX");
        System.out.println("\nEnter your selection: ");
        System.out.println("(Hit enter to cancel any operation)");
    }
    
    private void writeToFile() {
        application.writeToFile();
    }
}
