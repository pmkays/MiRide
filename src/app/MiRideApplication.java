package app;

import java.io.*;
import java.util.Scanner;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.DateUtilities;
import utilities.InvalidBooking;
import utilities.InvalidRefreshments;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker & Paula Kurniawan [s3782041]. 
 */
public class MiRideApplication
{
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars = new String[12];
	private String[] refreshmentsArray = new String[10];
	private Car[] availableCarsArray;	
	private SilverServiceCar car;
	Car[] SS = new Car[15]; 
	Car[] SD = new Car[15];
	Car SDcar;
	SilverServiceCar SScar;
	File file;
	File fileBackup;
	
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
			return "New Car added successfully for registion number: " 
			+ cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}
	
	public String createSSCar(String id, String make, String model, String driverName, 
			int numPassengers, double bookingFee, String[] refreshments) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) 
		{
			try
			{
				cars[itemCount] = new SilverServiceCar(id, make, model, driverName, 
						numPassengers, refreshments, bookingFee);
			} 
			catch (InvalidRefreshments | InvalidBooking e)
			{

				return e.getMessage();
			}
			itemCount++;
			return "New Car added successfully for registration number: " 
			+ cars[itemCount-1].getRegistrationNumber();
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
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + 
							". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	public String book(String firstName, String lastName, DateTime required, 
			int numPassengers, String registrationNumber)
	{
		Car car = getCarById(registrationNumber);
		if(car != null)
        {
			try
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
			catch (InvalidBooking e)
			{
				return e.getMessage();
			}
        }       
		return "Car with registration number: " + registrationNumber + " was not found.";
	}
	
	public String SSbook(String firstName, String lastName, DateTime required, 
			int numPassengers, String registrationNumber)
	{
		SilverServiceCar car = getSSCarById(registrationNumber);
		if(car != null)
		{
			try
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
			catch (InvalidBooking e)
			{
				return e.getMessage();
			}
        }       
		return "Car with registration number: " + registrationNumber + " was not found.";
	}

	
	public String completeBooking(String firstName, String lastName, 
			DateTime dateOfBooking, double kilometers)
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
	
	public String completeBooking(String firstName, String lastName, 
			String registrationNumber, double kilometers)
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
	
	public boolean seedData() throws InvalidBooking, InvalidRefreshments 
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
		itemCount++;
		
		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
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
		
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		//Will be displayed in current bookings of getDetails
		rover.book("Rodney", "Cocker", new DateTime(1), 3);
		//Will be displayed in past bookings of getDetails
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays,75);

		
		SilverServiceCar ssCar1 = new SilverServiceCar("AAA123", "Toyota", "Camry", 
				"Ben Gibbard", 7,  new String[] {"Soda", "Mints", "Chocolate"}, 9.0);
		cars[itemCount] = ssCar1;
		itemCount++;
		
		SilverServiceCar ssCar2 = new SilverServiceCar("BHS678", "Mini", "Cooper", 
				"Andy Hull", 6,  new String[] {"Fruit", "Alcohol", "Crackers"}, 4.0);
		cars[itemCount] = ssCar2;
		itemCount++;
		
		SilverServiceCar ssCar3 = new SilverServiceCar("TJH903", "Honda", "Odyssey", 
				"John Frusciante", 8,  new String[] {"Lollies", "Water", "Juice"}, 6.0);
		cars[itemCount] = ssCar3;
		itemCount++;
		ssCar3.book("John", "Richards", new DateTime(1), 3);
		
		
		SilverServiceCar ssCar4 = new SilverServiceCar("BEK502", "Suzuki", "Swift", 
				"Gina Esposito", 7,  new String[] {"Salad", "Smoothie", "Soda"}, 7.0);
		cars[itemCount] = ssCar4;
		itemCount++;
		ssCar4.book("John", "Richards", new DateTime(1), 6);
		
		SilverServiceCar ssCar5 = new SilverServiceCar("KLI695", "Volkswagen", "Polo", 
				"Emma Ragnarson", 9,  new String[] {"Soda", "Mints", "Chocolate"}, 5.0);
		cars[itemCount] = ssCar5;
		itemCount++;
		ssCar5.book("Matthew","Eriks", new DateTime(2), 7);
		ssCar5.book("Paula", "Kurniawan", new DateTime(1), 7);
		ssCar5.completeBooking("Paula", "Kurniawan", 23);
		
		SilverServiceCar ssCar6 = new SilverServiceCar("ZBY789", "Honda", "Jazz", 
				"Samuel Jones", 9,  new String[] {"Alcohol", "Chocolate", "Lollies"}, 8.0);
		cars[itemCount] = ssCar6;
		itemCount++;
		ssCar6.book("Matthew","Eriks", new DateTime(2), 8);
		ssCar6.book("Paula", "Kurniawan", new DateTime(1), 5);
		ssCar6.completeBooking("Paula", "Kurniawan", 22);
		
		return true;

	}

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
	
	//splits the refreshments array by a user inputed comma. 
	public String[] splitRefreshments(String refreshments)
	{
		refreshmentsArray = refreshments.split(",");
		return refreshmentsArray;
	}
	
	public boolean dateValidation(String date) throws InvalidBooking
	{
		final int NUM_OF_DATE = 10;
		if(date.length() != NUM_OF_DATE)
		{
			throw new InvalidBooking("sd");
		}
		
		return true;
	}

	public String availableCarsDetails(DateTime userDate, String carType)
	{
		boolean check = false;
		
		for(int i = 0; i< cars.length; i++)
		{
				//validates car is not booked on userDate, i.e car must be free this day
			if(cars[i] != null && cars[i].notCurrentlyBookedOnDate(userDate))
			{
				//validates user input carType and date not more than three days
				if(carType.equals("SS") && cars[i] instanceof SilverServiceCar && 
						DateUtilities.dateIsNotMoreThan3Days(userDate)) 
				{
					System.out.println(cars[i].getDetails());
					check = true;
				}
				/*
				 * validates user input carType, date not more than seven days and
				 * there are less than 5 current bookings
				 */
				else if (carType.equals("SD") && !(cars[i] instanceof SilverServiceCar) && 
						 DateUtilities.dateIsNotMoreThan7Days(userDate) && cars[i].getAvailability())
				{
					System.out.println(cars[i].getDetails());
					check = true; 
				}
			}
		}
		if(!check) 
		{			
			return "Error - No cars were found on this date.";
		} 
		else
		{
			return "";
		}
	}
	
	
	
	/* ALGORITHM to check which type of Car was chosen (determined by returned boolean values) 
	 * and to populate individual SDCars and SSCars array
	 * 
	 * BEGIN:
	 * 		ASSIGN boolean value to true 
	 * 		LOOP through cars array
	 * 			IF cars array is not empty
	 * 				IF user input is "SS" and it is a SSCar 
	 * 					THEN add that car into an array of just SSCars
	 * 				ELSE IF user input is "SD" and it is !SScar 
	 * 					THEN add that car into an array of just SDCars 
	 * 					ASSIGN boolean value to false
	 * 				END IF
	 * 			END IF
	 * 		RETURN boolean value where true represents "SS" has been chosen and 
	 * 			   false represents "SD" has been chosen
	 * END 
	 */
	
	public boolean typeOfCar (String carType)
	{
		boolean check = true;
		int count = 0;
		for(int i =0; i < cars.length; i++)
		{
			if(cars[i]!= null)
			{
				if(carType.equals("SS") && cars[i] instanceof SilverServiceCar)
				{
					SS[count]= cars[i];
					count++;

				}
				else if(carType.equals("SD") && !(cars[i] instanceof SilverServiceCar))
				{
					SD[count] = cars[i]; 
					count++;
					check = false;
				}
			}
		}
		return check;
	}
	
	/* ALGORITHM to sort SSCars' regNos ascending using bubble sorting; other sorting algorithms 
	 * implemented follow a similar structure. 
	 * 
	 * BEGIN: 
	 * 		LOOP through SSCars array using integer variable e.g. i. 
	 * 			LOOP through SSCars array using another integer variable + 1 e.g. j = i+1
	 * 				IF SSCars array is not null
	 * 					IF SS[i]'s initial character of regNO is higher than SS[j] when compared to 
	 * 						SWAP SS[i] and SS[j]
	 * 					END IF
	 * 				END IF
	 * 
	 * 		LOOP through SSCar array
	 * 			IF SSCar array is not null 
	 * 				PRINT the details of that SSCar
	 * 			END IF
	 * END
	 */
	
	public void SSsortCarsA()
	{
		for(int i =0; i<SS.length; i++)
		{
			for (int j=i+1; j < SS.length; j++)
			{
				if(SS[i]!= null && SS[j]!= null)
				{
					if(SS[i].getRegistrationNumber().compareTo(SS[j].getRegistrationNumber()) > 0)
					{
						SScar = (SilverServiceCar) SS[i];
						SS[i]=SS[j];
						SS[j]= SScar;
					}
				}
			}
		}
		
		for(int i=0; i<SS.length; i++)
		{
			if(SS[i] != null)
			{
				System.out.println(SS[i].getDetails());	
			}
		}
	}
	
	public void SDsortCarsA()
	{
		for(int i =0; i<SD.length; i++)
		{
			for (int j=i+1; j < SD.length; j++)
			{
				if(SD[i] != null && SD[j]!=null)
				{
					if(SD[i].getRegistrationNumber().compareTo(SD[j].getRegistrationNumber()) > 0)
					{
						SDcar = SD[i];
						SD[i]=SD[j];
						SD[j]= SDcar;				
					}
				}
			}		
		}
		
		for(int i=0; i<SD.length; i++)
		{
			if (SD[i] != null)
			{
				System.out.println(SD[i].getDetails());
			}
		}
	}
	
	public void SSsortCarsD()
	{
		for(int i =0; i<SS.length; i++)
		{
			for (int j=i+1; j < SS.length; j++)
			{
				if(SS[i]!= null && SS[j] != null)
				{
					if(SS[i].getRegistrationNumber().compareTo(SS[j].getRegistrationNumber()) < 0)
					{
						SScar = (SilverServiceCar) SS[i];
						SS[i]=SS[j];
						SS[j]= SScar;
					}
				}
			}
		}
		
		for(int i=0; i<SS.length; i++)
		{
			if (SS[i]!= null)
			{
				System.out.println(SS[i].getDetails());	
			}
		}
	}		

	public void SDsortCarsD()
	{
		for(int i =0; i<SD.length; i++)
		{
			for (int j=i+1; j < SD.length; j++)
			{
				if (SD[i]!= null && SD[j] != null)
				{
					if(SD[i].getRegistrationNumber().compareTo(SD[j].getRegistrationNumber()) < 0)
					{
						SDcar = SD[i];
						SD[i]=SD[j];
						SD[j]= SDcar;
					}
				}
			}		
		}
		
		for(int i=0; i<SD.length; i++)
		{
			if (SD[i]!= null)
			{
				System.out.println(SD[i].getDetails());	
			}
		}
	}
	
	public String displayAllBookings(String carType, String sortOrder)
	{
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}

		StringBuilder sb = new StringBuilder();

		if (typeOfCar(carType))
		{
			if(sortOrder.equals("A"))
			{
				SSsortCarsA();
			}
			else
			{
				SSsortCarsD();
			}
		}
		else
		{
			if(sortOrder.equals("A"))
			{
				SDsortCarsA();
			}
			else
			{
				SDsortCarsD();
			}		
		}
		
		if (carType.equals("SS") && SS[0] == null)
		{
			return "No Silver Service Cars have been added to the system.";
		}
		else if (carType.equals("SD") && SD[0] ==null)
		{
			return "No Standard Cars have been added to the system.";
		}
		return sb.toString();
	}
	
	//writes a .txt file when EX in menu is selected and saves all the car details
	public void writingFile()
	{
		file = new File("Cars.txt");
		fileBackup = new File("Cars_Backup.txt");
		PrintWriter output = null;
		PrintWriter outputBackup = null;
		try
		{
			output = new PrintWriter(new FileOutputStream(file, true));	
			outputBackup = new PrintWriter(new FileOutputStream(fileBackup, true));	
			
			for (int i =0; i< cars.length; i++)
			{

				if (cars[i]!= null && cars[i] instanceof SilverServiceCar)
				{
					output.println(cars[i].carToString());
					outputBackup.println(cars[i].carToString());
				}
				else if (cars[i]!= null && !(cars[i] instanceof SilverServiceCar))
				{
					output.println(cars[i].carToString());
					outputBackup.println(cars[i].carToString());
				}
			}
			
			System.out.println("Writing successful!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		output.close();
		outputBackup.close();
	}

	//reads file when program starts up
	public void readFile()
	{
		Scanner input = null;
		try
		{
			input = new Scanner(new File("Cars.txt"));
			System.out.println("Data loaded successfully!");
			addAllCarsPersistance(input);	
			input.close();
		}
		catch (FileNotFoundException e)
		{
			try
			{
				input = new Scanner(new File("Cars_Backup.txt"));
				System.out.println("Data was located from a backup file!");
				addAllCarsPersistance(input);	
				input.close();
			}
			catch (FileNotFoundException error)
			{
				System.out.println("No booking data was loaded! ");
			}
		}
	}
	
	//assigns text information to variables which then gets passed through as car details
	public void addAllCarsPersistance(Scanner input)
	{
		int count =0;
		final int INDEX_OF_REFRESHMENTS = 6;
		double fee = 0.0;
		String[] carArray = new String[15];
		String[] details = new String[10];

		input.useDelimiter("\n");
		while (input.hasNextLine())
		{
			carArray[count] = input.nextLine();	
			if(count < carArray.length-1)
			{
				count++;
			}
		}
		
		for(int i =0; i<carArray.length; i++)
		{
			if (carArray[i]!=null && !(carArray[i].contains("Item")))
			{
				details = carArray[i].split(":");		
				String id = details[0];
				String make = details[1];
				String model = details[2];
				String name = details[3];
				int passengerCapacity = Integer.parseInt(details[4]);
				String availability = (details[5]);
				cars[itemCount]= new Car(id, make, model, name, passengerCapacity);
				if(itemCount < cars.length - 1)
				{
					itemCount++;
				}	
			}
			else if (carArray[i]!=null && carArray[i].contains("Item"))
			{
				int counter = 0;
				details = carArray[i].split(":");	
				String[] refreshmentsArray = new String[5];
				String id = details[0];
				String make = details[1];
				String model = details[2];
				String name = details[3];
				int passengerCapacity = Integer.parseInt(details[4]);
				String availability = (details[5]);
				
				for (int j = INDEX_OF_REFRESHMENTS; j < details.length; j++)
				{
					if (details[j]!= null && details[j].contains("Item"))
					{
						refreshmentsArray[counter] = details[j].substring(7);
						counter++;
						
					}
					else if (details[j]!= null && details[j].contains("."))
					{
						fee = Double.parseDouble(details[j]);
					}
				}
				try
				{
					cars[itemCount]= new SilverServiceCar(id, make, model, name, 
						passengerCapacity, refreshmentsArray, fee);	
				}
				catch (InvalidRefreshments | InvalidBooking e)
				{
					e.toString();
				}
				
				if(itemCount < cars.length -1)
				{
					itemCount++;
				}
				counter = 0;
			}
		}
	}
}



