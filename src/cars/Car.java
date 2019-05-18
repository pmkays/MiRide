package cars;

import utilities.DateTime;
import utilities.DateUtilities;
import utilities.InvalidBooking;
import utilities.MiRidesUtilities;

/*
 * Class:		Car
 * Description:	The class represents a car in a ride sharing system. 
 * Author:		Rodney Cocker & Paula Kurniawan
 */
public class Car
{
	// Car attributes
	private String regNo;
	private String make;
	private String model;
	private String driverName;
	private int passengerCapacity;

	// Tracking bookings
	private Booking[] currentBookings;
	private Booking[] pastBookings;
	private boolean available;
	private int bookingSpotAvailable = 0;
	private double tripFee = 0;

	// Constants
	private double bookingFee = 1.5;
	private final int MAXIUM_PASSENGER_CAPACITY = 10;
	private final int MINIMUM_PASSENGER_CAPACITY = 1;
	private final int NAME_MINIMUM_LENGTH = 3;

	public Car(String regNo, String make, String model, String driverName, int passengerCapacity)
	{
		setRegNo(regNo); // Validates and sets registration number
		setPassengerCapacity(passengerCapacity); // Validates and sets passenger capacity

		this.make = make;
		this.model = model;
		this.driverName = driverName;
		available = true;
		currentBookings = new Booking[5];
		pastBookings = new Booking[10];
	}
	/*
	 * ALGORITHM to validate all necessary business rules before making a booking
	 * 
	 * BEGIN:
	 * 		IF the date is not valid, i.e. in the past or greater than 7 days in the future
	 * 			THROW a new InvalidBooking exception
	 * 		END IF
	 * 		
	 * 		IF not available, i.e. has more than five current bookings
	 * 			THROW a new InvalidBooking exception
	 * 		END IF
	 * 
	 * 		IF currently booked on that date
	 * 			THROW a new InvalidBooking exception 
	 * 		END IF
	 * 
	 * 		IF the number of passengers is less than passenger capacity && the length 
	 * 				of the first name and last name are >= 3 characters
	 * 			ASSIGN trip fee to the booking fee
	 * 			COMPLETE a new booking and store it in currentBookings array
	 * 			Increment booking spot index for next booking 
	 * 		END IF
	 * END 		
	 * 		
	 */
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking
	{
		boolean booked = false;
		
		//checks if date is valid
		if(!dateIsValid(required))
		{
			throw new InvalidBooking("The date is invalid.");
		}
		
		//checks if car has more than 5 current bookings
		if (!available)
		{
			throw new InvalidBooking("No more bookings can be made as there are already 5 current bookings.");
		}
		
		//checks if car has already been booked for the day
		if(!notCurrentlyBookedOnDate(required))
		{
			throw new InvalidBooking("The car cannot be booked for this day.");
		}

		//checks of passengers does not exceed the passenger capacity and is not zero.
		boolean validPassengerNumber = numberOfPassengersIsValid(numPassengers);
		
		//checks name length is greater than 3 characters
		boolean nameValidation = nameValidation(firstName, lastName);

		if (validPassengerNumber && nameValidation)
		{
			tripFee = bookingFee;
			Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
			currentBookings[bookingSpotAvailable] = booking;
			bookingSpotAvailable++;
			booked = true;
		}
		available = bookingAvailable();
		return booked;
	}
	
	private boolean nameValidation(String firstName, String lastName)
	{
		if (firstName.length() >= NAME_MINIMUM_LENGTH  && lastName.length() >= NAME_MINIMUM_LENGTH )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * Completes a booking based on the name of the passenger and the booking date.
	 */
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
	{
		// Find booking in current bookings by passenger and date
		int bookingIndex = getBookingByDate(firstName, lastName, dateOfBooking);

		if (bookingIndex == -1)
		{
			return "Booking not found.";
		}

		return completeBooking(bookingIndex, kilometers);
	}

	/*
	 * Completes a booking based on the name of the passenger.
	 */
	public String completeBooking(String firstName, String lastName, double kilometers)
	{
		int bookingIndex = getBookingByName(firstName, lastName);

		if (bookingIndex == -1)
		{
			return "Booking not found.";
		} else
		{
			return completeBooking(bookingIndex, kilometers);
		}
	}

	/*
	 * ALGORITHm to check the current bookings to see if any of the bookings are for the current
	 * date. 
	 * 
	 * BEGIN: 
	 * 		LOOP through currentBookings array
	 * 			IF currentBookings array is not empty
	 * 				IF there is a date in currentBookings that matches user input
	 * 					THEN return true
	 * 				END IF
	 * 			END IF
	 * END.
	 */
	public boolean isCarBookedOnDate(DateTime dateRequired)
	{
		boolean carIsBookedOnDate = false;
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				if (DateUtilities.datesAreTheSame(dateRequired, currentBookings[i].getBookingDate()))
				{
					carIsBookedOnDate = true;
				}
			}
		}
		return carIsBookedOnDate;
	}

	/*
	 * Retrieves a booking id based on the name and the date of the booking
	 */
	public String getBookingID(String firstName, String lastName, DateTime dateOfBooking)
	{
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				int days = DateTime.diffDays(dateOfBooking, booking.getBookingDate());
				if (firstNameMatch && lastNameMatch && days == 0)
				{
					return booking.getID();
				}
			}
		}
		return "Booking not found";
	}
	
	public String getDetails()
	{

		return carDetails() + currentBookingsDisplay() + pastBookingsDisplay();
	}
	
	protected String carDetails()
	{
		String regNo = String.format("%n%-17s%s", "RegNo:", this.regNo);
		String makeAndModel = String.format("%n%-17s%s", "Make & Model:", 
							  this.make + " " + this.model);
		String driverName = String.format("%n%-17s%s", "Driver Name:", 
				            this.driverName);
		String passengerCapacity = String.format("%n%-17s%d", "Capacity:", 
				       			   this.passengerCapacity);
		String isAvailable = String.format("%n%-17s%s%n", "Available:", available ? "YES" : "NO");
		String carDetails = regNo + makeAndModel + driverName + passengerCapacity + 
							isAvailable;
		return carDetails;
	}
	
	protected String currentBookingsDisplay()
	{
		String bookings="";
		boolean check = false;
		for(int i = 0; i < currentBookings.length; i++)
		{
			if(currentBookings[i]!=null)
			{
				bookings+=(String.format("%n%s", currentBookings[i].getDetails()));
				check = true;
			}
		}
		if(check)
		{
			return String.format("%n%-13s%s%n", "CURRENT BOOKINGS: ", bookings) + "\n__________________________________________________________________";
		}
		else
		{
			return "";
		}
		
	}
	
	protected String pastBookingsDisplay()
	{
		boolean check = false;
		String bookings = "";
		for(int i = 0; i < pastBookings.length; i++)
		{
			if(pastBookings[i]!=null)
			{
				bookings+=(String.format("%n%s", pastBookings[i].getDetails()));
				check= true;
			}
		}
		if(check)
		{
			return  String.format("%n%-13s%s%n", "PAST BOOKINGS: ", bookings) + "\n_________________________________________________________________";
		}
		else
		{
			return "";
		}
	}
	
	public String toString()
	{
		return carToString() + currentBookingsToString() + pastBookingsToString();
	}
	
	public String carToString()
	{
		return regNo + ":" + make + ":" + model + ":" + driverName + 
				":" + passengerCapacity + ":" + (available ? "YES" : "NO");
	}
	
	protected String currentBookingsToString()
	{
		String bookings = "";
		for(int i =0; i<currentBookings.length; i++)
		{
			if(currentBookings[i]!= null)
			{
				bookings += "|" + currentBookings[i].toString();
			}
		}
		return bookings;	
	}
	
	protected String pastBookingsToString()
	{
		String bookings = "";
		for(int i =0; i<pastBookings.length; i++)
		{
			if(pastBookings[i]!= null)
			{
				bookings += "|" + pastBookings[i].toString();
			}
		}
		return bookings;	
	}

	/*
	 * Checks to see if any past bookings have been recorded
	 */
	private boolean hasBookings(Booking[] bookings)
	{
		boolean found = false;
		for (int i = 0; i < bookings.length; i++)
		{
			if (bookings[i] != null)
			{
				found = true;
			}
		}
		return found;
	}

	/*
	 * Processes the completion of the booking
	 */
	private String completeBooking(int bookingIndex, double kilometers)
	{
		tripFee = 0;
		Booking booking = currentBookings[bookingIndex];
		// Remove booking from current bookings array.
		currentBookings[bookingIndex] = null;
		bookingSpotAvailable = bookingIndex;

		double fee = kilometers * (bookingFee * 0.3);
		tripFee += fee;
		booking.completeBooking(kilometers, fee, bookingFee);
		// add booking to past bookings
		for (int i = 0; i < pastBookings.length; i++)
		{
			if (pastBookings[i] == null)
			{
				pastBookings[i] = booking;
				break;
			}
		}
		String result = String.format("Thank you for riding with MiRide.\nWe hope you enjoyed your trip.\n$"
				+ "%.2f has been deducted from your account.", tripFee);
		tripFee = 0;
		return result;
	}
	

	/*
	 * Gets the position in the array of a booking based on a name and date. Returns
	 * the index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	private int getBookingByDate(String firstName, String lastName, DateTime dateOfBooking)
	{
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				boolean dateMatch = DateUtilities.datesAreTheSame(dateOfBooking, currentBookings[i].getBookingDate());
				if (firstNameMatch && lastNameMatch && dateMatch)
				{
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * Gets the position in the array of a booking based on a name. Returns the
	 * index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	public int getBookingByName(String firstName, String lastName)
	{
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				boolean firstNameMatch = currentBookings[i].getFirstName().toUpperCase()
						.equals(firstName.toUpperCase());
				boolean lastNameMatch = currentBookings[i].getLastName().toUpperCase().equals(lastName.toUpperCase());
				if (firstNameMatch && lastNameMatch)
				{
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	private String getRecordMarker()
	{
		final int RECORD_MARKER_WIDTH = 60;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < RECORD_MARKER_WIDTH; i++)
		{
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/*
	 * Checks to see if the number of passengers falls within the accepted range.
	 */
	private boolean numberOfPassengersIsValid(int numPassengers)
	{
		if (numPassengers >= MINIMUM_PASSENGER_CAPACITY && numPassengers < MAXIUM_PASSENGER_CAPACITY
				&& numPassengers <= passengerCapacity)
		{
			return true;
		}
		return false;
	}

	/*
	 * Checks that the date is not in the past or more than 7 days in the future.
	 */
	private boolean dateIsValid(DateTime date)
	{
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan7Days(date);
	}

	/*
	 * Indicates if a booking spot is available. If it is then the index of the
	 * available spot is assigned to bookingSpotFree.
	 */
	public boolean bookingAvailable()
	{
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] == null)
			{
				bookingSpotAvailable = i;
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks to see if if the car is currently booked on the date specified.
	 */
	public boolean notCurrentlyBookedOnDate(DateTime date)
	{
		boolean foundDate = true;
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				int days = DateTime.actualDiffDays(date, currentBookings[i].getBookingDate());
				if (days == 0)  
				{
					return false;
				}
			}
		}
		return foundDate;
	}

	/*
	 * Validates and sets the registration number
	 */
	private void setRegNo(String regNo)
	{
		if (!MiRidesUtilities.isRegNoValid(regNo).contains("Error:"))
		{
			this.regNo = regNo;
		} else
		{
			this.regNo = "Invalid";
		}
	}

	/*
	 * Validates and sets the passenger capacity
	 */
	private void setPassengerCapacity(int passengerCapacity)
	{
		boolean validPasengerCapcity = passengerCapacity >= MINIMUM_PASSENGER_CAPACITY
				&& passengerCapacity < MAXIUM_PASSENGER_CAPACITY;

		if (validPasengerCapcity)
		{
			this.passengerCapacity = passengerCapacity;
		} else
		{
			this.passengerCapacity = -1;
		}
	}
	
	public Booking[] getCurrentBookings()
	{
		return currentBookings;
	}
	
	public Booking getSpecificCurrentBooking(int i)
	{
		return currentBookings[i];
	}
	
	public Booking[] getPastBooking()
	{
		return currentBookings;
	}
	
	public Booking getSpecificPastBooking(int i)
	{
		return pastBookings[i];
	}
	
	protected void setBookingFee(double bookingFee)
	{
		this.bookingFee= bookingFee;
	}
	
	public boolean getAvailability()
	{
		return this.available;
	}
	
	public String getRegistrationNumber()
	{
		return regNo;
	}

	public String getDriverName()
	{
		return driverName;
	}

	public double getTripFee()
	{
		return tripFee;
	}


}
