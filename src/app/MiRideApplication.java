package app;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker
 */
public class MiRideApplication
{
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;
	private String[] refreshmentsArray;
	private SilverServiceCar car;

	public MiRideApplication()
	{
		//seedData();
	}
	
	public String createCar(String id, String make, String model, String driverName, int numPassengers) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}
	
	public String createSSCar(String id, String make, String model, String driverName, int numPassengers, double bookingFee, String[] refreshments) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) 
		{
			cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, refreshments, bookingFee);
			itemCount++;
			return "New Car added successfully for registration number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	public String[] book(DateTime dateRequired)
	{
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for(int i=0; i<cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					numberOfAvailableCars++;
				}
			}
		}
		if(numberOfAvailableCars == 0)
		{
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for(int i=0; i<cars.length;i++)
		{
			
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	public String book(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber)
	{
		Car car = getCarById(registrationNumber);
		if(car != null)
        {
			if(car.book(firstName, lastName, required, numPassengers))
			{

				String message = "Thank you for your booking. \n" + car.getDriverName() 
		        + " will pick you up on " + required.getFormattedDate() + ". \n"
				+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
				return message;
			}
			else
			{
				String message = "Booking could not be completed.";
				return message;
			}
        }
        else
        {
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
	}
	
	public String SSbook(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber)
	{
		SilverServiceCar car = getSSCarById(registrationNumber);
		if(car != null)
        {
			if(car.book(firstName, lastName, required, numPassengers))
			{

				String message = "Thank you for your booking. \n" + car.getDriverName() 
		        + " will pick you up on " + required.getFormattedDate() + ". \n"
				+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
				return message;
			}
			else
			{
				String message = "Booking could not be completed.";
				return message;
			}
        }
        else
        {
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
	}

	
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
	{
		String result = "";
		
		// Search all cars for bookings on a particular date.
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if(cars[i].isCarBookedOnDate(dateOfBooking))
				{
					return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				}
			}
		}
		return "Booking not found.";
	}
	
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers)
	{
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}

		if (car == null)
		{
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1)
		{
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}
	
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber)
	{
		String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}	
		if(car == null)
		{
			return false;
		}
		if(car.getBookingByName(firstName, lastName) == -1)
		{
			return false;
		}
		return true;
	}
	public String displaySpecificCar(String regNo)
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(cars[i].getRegistrationNumber().equals(regNo))
				{
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}
	
	public boolean seedData()
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
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
		//rover.completeBooking("Rodney", "Cocker", 75);
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays,75);
		return true;
	}

	public String displayAllBookings()
	{
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Summary of all cars: ");
		sb.append("\n");

		for (int i = 0; i < itemCount; i++)
		{
			sb.append(cars[i].getDetails());
		}
		return sb.toString();
	}
	
	public String displayAllSortedBookings(String carType, String sortOrdeer)
	{
		return "hello";
	}
	/*displayallBookings sort from passengercapacity
	 * int count = 0
	 * loop through cars array using one integer
	 * loop through cars arary using another integer
	 * if cars isnt null
	 * 	if cars[i].passengercapacity < cars[j].passengercapacity
	 * 	then cars[i] = cars[j] (how do i put object of car j into that array index) 
	 * 
	 * another for loop to print out the new array sorting. 
	 * cars[i].getDetails
	 * 
	 */

	public String displayBooking(String id, String seatId)
	{
		Car booking = getCarById(id);
		if(booking == null)
		{
			return "Booking not found";
		}
		return booking.getDetails();
	}
	
	public String isValidId(String id)
	{
		return MiRidesUtilities.isRegNoValid(id);
	}
	
	public String isValidPassengerCapacity(int passengerNumber)
	{
		return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
	}

	public boolean checkIfCarExists(String regNo)
	{
		Car car = null;
		if (regNo.length() != 6)
		{
			return false;
		}
		car = getCarById(regNo);
		if (car == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private Car getCarById(String regNo)
	{
		Car car = null;

		for (int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(regNo))
				{
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	//assign the regNo to a car first using getCarById
	//use that car to check if it is an instance of SSCar.
	//if it is return true 
	public boolean checkWhichCar(String regNo)
	{
		boolean checked = false;
		//uses the regNo to make a car object, 
		if (getCarById(regNo) instanceof SilverServiceCar)
		{
			return checked = true;
		}
		
		return checked;	
	}
	
	private SilverServiceCar getSSCarById(String regNo)
	{
		SilverServiceCar car = null;

		for (int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(regNo))
				{
					car = (SilverServiceCar) cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	/*if call bookingFee validates, loop through cars array. 
	 * if cars array is not empty,  add that SS car in. 
	 * 
	 * 
	 */
	
	public boolean SSvalidation(double bookingFee, String regNo)//need to check for fee validation 
	{
		boolean bf = false; 
		SilverServiceCar car = new SilverServiceCar(regNo, null, null, null, 0, new String[] {}, bookingFee);

		 if(car.feeValidation(bookingFee))
		 {
			 bf = true;
		 }
		 return bf;
	}
	
	public String[] splitRefreshments(String refreshments)
	{
		refreshmentsArray = refreshments.split(",");
		return refreshmentsArray;
	}
	
	
	
//	public String searchAvailableCars(DateTime userDate, String carChoice)
//	{
//		if(!availableCarsDateValidation())
//		{
//			return "Error - no cars were found on this date";
//		}
//		else if (carChoice.equals("SD"))
//		{
//			//if object in array dont have double value???
//			//cycle through array adn print that objectl; how to differentiate SD and SS
//		}
//		else
//		{
//			//if obejcts in array have refreshments/standardbookingfee
//			//cyclethrough that array and print that object
//		}	
//	}
	
	public boolean availableCarsDateValidation()
	{
		return true; //how to validate if a car is available? take out code segment from this.book method?
	}
	
//	public String searchAvailableCars()
//	{
//		if(itemCount == 0)
//		{
//			return "There are no available cars.";
//		}
//		StringBuilder sb = new StringBuilder();
//		sb.append("Summary of all cars: ");
//		sb.append("\n");
//
//		for (int i = 0; i < itemCount; i++)
//		{
//			for(int j =0; j < itemCount; i++)
//			{
//				if(cars[j] != null && availableCars[i] != null)
//				{
//					if(cars[j].getRegistrationNumber().contains(availableCars[i]))
//					{
//						return cars[j].getDetails();
//					}
//				}				
//			}
//		}
//		return sb.toString();
//	}
}


