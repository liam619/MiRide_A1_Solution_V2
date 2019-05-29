package main;

import app.Menu;
import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;

public class Driver {

	public static void main(String[] args) 
	{
//		String[] anything = {"water", "OJ", "Applejuice"};
//		SilverServiceCar s = new SilverServiceCar ("ALI123", "HI", "123", "BIG", 5, 3.5, anything);
//				System.out.println(s.getDetails());
//		System.out.println(s.toString());
//		
//		}
	    
	    String[] anything = {"water", "OJ", "Applejuice"};
        // 1 car booked five times (not available)
        Car rover = new SilverServiceCar("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7, 3.5, anything);
//        cars[itemCount] = rover;
//        itemCount++;
        rover.book("Rodney", "Cocker", new DateTime(1), 3);
        // rover.completeBooking("Rodney", "Cocker", 75);
        DateTime inTwoDays = new DateTime(2);
        rover.book("Rodney", "Cocker", inTwoDays, 3);
        rover.completeBooking("Rodney", "Cocker", inTwoDays, 75);
//        
        System.out.println(rover.getDetails());
        
        String msg = rover.getBookingID("Rodney", "Cocker", new DateTime(1));
        int x = rover.getBookingByName("Rodney", "Cocker");
        System.out.println(msg);
        System.out.println(x);
//		Menu menu = new Menu();
//		menu.run();
	}
}
