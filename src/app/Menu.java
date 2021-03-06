package app;

import java.util.InputMismatchException;
import java.util.Scanner;
import utilities.DateTime;
import utilities.DateUtilities;
import utilities.InvalidBooking;
import utilities.InvalidRefreshments;

/*
 * Class:		Menu
 * Description:	The menu class is used to interact with the user. 
 * Author:		Rodney Cocker & Paula Kurniawan [s3782041]. 
 */
public class Menu
{
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;
	String[] refreshmentsArray;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run()
	{
		application.readFile();
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		do
		{
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH)
			{
				System.out.println("Error - selection must be two characters!");
			} 
			else
			{
				System.out.println();

				switch (input)
				{
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
					displayAllCars();
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					searchAvailableCars();
					break;
				case "SD":
					try
					{
						application.seedData();
					}
					catch (InvalidBooking | InvalidRefreshments e)
					{
						e.toString();
					}
					break;
				case "EX":
					choice = "EX";
					application.writingFile();
					System.out.println("Exiting Program ... Goodbye!");
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
					break;
				}
			}
		} 
		while (!choice.equals("EX"));
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar()
	{
		try
		{
			String id = "", make, model, driverName;
			int numPassengers = 0;
	
			System.out.print("Enter registration number: ");
			id = promptUserForRegNo();
			if (id.length() != 0)
			{
				System.out.print("Enter Make: ");
				make = console.nextLine();
	
				System.out.print("Enter Model: ");
				model = console.nextLine();
	
				System.out.print("Enter Driver Name: ");
				driverName = console.nextLine();
	
				System.out.print("Enter number of passengers: ");
				numPassengers = promptForPassengerNumbers();
				
				System.out.print("Enter service type (SD/SS): ");
				String serviceType = console.nextLine().toUpperCase();
	
				boolean result = application.checkIfCarExists(id);
	
				if (!result && serviceType.equals(("SD")))
				{
					String SDcarRegistrationNumber = application.createCar
							(id, make, model, driverName, numPassengers);
					System.out.println(SDcarRegistrationNumber);
				} 
				//check if regNo doesn't already exist + regNo is six characters + validates passenger capacity
				else if (!result && serviceType.equals(("SS"))) 
				{
					System.out.println("Enter standard fee:");
					double SDbookingFee = console.nextDouble();
					console.nextLine();
					System.out.println("Enter list of refreshments (separated by a comma, no spaces): ");
					String refreshments = console.nextLine();
					refreshmentsArray = application.splitRefreshments(refreshments);

					String SScarRegistrationNumber = application.createSSCar
							(id, make, model, driverName, numPassengers, SDbookingFee, refreshmentsArray);
					System.out.println(SScarRegistrationNumber);					
				}
				else
				{
					System.out.println("Error - Specified service type does not exist.");
				}
			}
		}
		catch (NumberFormatException | IndexOutOfBoundsException | InputMismatchException | NullPointerException e)
		{
			System.out.println(e.toString());
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private void book()
	{
		try
		{
			System.out.println("Enter date car required: ");
			System.out.println("(format DD/MM/YYYY)");
			String dateEntered = console.nextLine();
			
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
			DateTime dateRequired = new DateTime(day, month, year);
			
			String[] availableCars = application.book(dateRequired);
			for (int i = 0; i < availableCars.length; i++)
			{
				System.out.println(availableCars[i]);
			}
			if (availableCars.length != 0)
			{
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
				
				if(application.checkWhichCar(regNo))
				{
					String SSresult = application.SSbook(firstName, lastName, dateRequired, numPassengers, regNo);
					System.out.println(SSresult);
				}
				else
				{
				String SDresult = application.book(firstName, lastName, dateRequired, numPassengers, regNo);
				System.out.println(SDresult);
				}
			} 
			else
			{
				System.out.println("There are no available cars on this date.");
			}
		}
		catch (NumberFormatException | IndexOutOfBoundsException | InputMismatchException | NullPointerException e)
		{
			System.out.println(e.toString());
		}
	}
		
	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking()
	{
		try
		{
			String result;
			
			System.out.print("Enter Registration or Booking Date:");
			String response = console.nextLine().toUpperCase();
			
			if (response.contains("/"))
			{
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
			} 
			else
			{
				
				System.out.print("Enter First Name:");
				String firstName = console.nextLine();
				
				System.out.print("Enter Last Name:");
				String lastName = console.nextLine();
				
				if(application.getBookingByName(firstName, lastName, response))
				{
					System.out.print("Enter kilometers:");
					double kilometers = Double.parseDouble(console.nextLine());
					
					result = application.completeBooking(firstName, lastName, response, kilometers);
					System.out.println(result);
				}
				else
				{
					System.out.println("Error: Booking not found.");
				}
			}
		}
		catch (NumberFormatException | IndexOutOfBoundsException | InputMismatchException | NullPointerException e)
		{
			System.out.println(e.toString());
		}
		
	}
	
	private int promptForPassengerNumbers()
	{
		int numPassengers = 0;
		boolean validPassengerNumbers = false;
		if (!testingWithValidation)
		{
			return Integer.parseInt(console.nextLine());
		} 
		else
		{
			while (!validPassengerNumbers)
			{
				numPassengers = Integer.parseInt(console.nextLine());

				String validId = application.isValidPassengerCapacity(numPassengers);
				if (validId.contains("Error:"))
				{
					System.out.println(validId);
					System.out.println("Enter passenger capacity: ");
					System.out.println("(or hit ENTER to exit)");
				} else
				{
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
	private String promptUserForRegNo()
	{
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation)
		{
			return console.nextLine().toUpperCase();
		} 
		else
		{
			while (!validRegistrationNumber)
			{
				regNo = console.nextLine().toUpperCase();
				boolean exists = application.checkIfCarExists(regNo);
				if(exists)
				{
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0)
				{
					break;
				}

				String validId = application.isValidId(regNo);
				if (validId.contains("Error:"))
				{
					System.out.println(validId);
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				} else
				{
					validRegistrationNumber = true;
				}
			}
			return regNo;
		}
	}
	
	/* ALGORITHM to search for available cars using user input
	 * 
	 * BEGIN:
	 * 		PROMPT user for type of car (SD/SS)
	 * 		ASSIGN String variable to user input of type of car
	 * 		PROMPT user to enter date
	 * 		ASSIGN String variable to user input of date
	 * 		SEPARATE the date entered and convert to integers
	 * 		MAKE a new DateTime object and pass through the converted integers
	 * 		ASSIGN a string variable to the returned string from a validation method in application class
	 * 		PRINT that string variable
	 * END
	 */
	public void searchAvailableCars()
	{
		try
		{
			System.out.println("Enter type (SD/SS): ");
			String carType = console.nextLine().toUpperCase();
			
			System.out.println("Enter date:");
			String dateEntered = console.nextLine();
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
			DateTime dateRequired = new DateTime(day, month, year);
			
			String result = application.availableCarsDetails(dateRequired, carType);
			System.out.println(result);
		}
		catch (NumberFormatException | IndexOutOfBoundsException | InputMismatchException e)
		{
			System.out.println(e.toString());
		}
	}
	
	public void displayAllCars()
	{
		try
		{
			System.out.println("Enter Type (SD/SS): ");
			String carType = console.nextLine().toUpperCase(); 
			
			System.out.println("Enter Sort Order: ");
			String sortOrder = console.nextLine().toUpperCase();
			
			String result = application.displayAllBookings(carType, sortOrder);
			System.out.println(result);
		}
		catch (NumberFormatException | IndexOutOfBoundsException | InputMismatchException e)
		{
			System.out.println(e.toString());
		}
	}

	/*
	 * Prints the menu.
	 */
	private void printMenu()
	{
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
}
