package cars;

import utilities.DateTime;
import utilities.DateUtilities;
/*
 * Class:		Booking
 * Description:	The class represents a booking in the program. 
 * Author:		Rodney Cocker & Paula Kurniawan [s3782041]
 */
public class Booking {
	
	private String id;
	private String firstName;
	private String lastName;
	private DateTime dateBooked;
	private int numPassengers;
	private double bookingFee;
	private double kilometersTravelled;
	private double tripFee;
	private Car car;
	
	private final int NAME_MINIMUM_LENGTH = 3;

	public Booking(String firstName, String lastName, DateTime required, int numPassengers, Car car) 
	{
		generateId(car.getRegistrationNumber(), firstName, lastName, required);
		validateAndSetDate(required);
		validateName(firstName, lastName);
		this.numPassengers = numPassengers;
		this.car = car;
		this.bookingFee = car.getTripFee();
	}
	
	/*
	 * Updates the booking record with the kilometers traveled, the booking and trip fee.
	 */
	public void completeBooking(double kilometersTravelled, double tripFee, double bookingFee)
	{
		this.kilometersTravelled = kilometersTravelled;
		this.tripFee = tripFee;
		this.bookingFee = bookingFee;
	}
	
	public String getDetails()
	{
		String bookingId = String.format("%17s%-28s%s%n","", "id: ", id.toUpperCase());
		String bookingsFee = String.format("%17s%-28s$%.2f%n","", "Booking Fee: ", 
							 bookingFee);
		String bookingPickUpDate = String.format("%17s%-28s%s%n", "","Pick up Date: ", 
				dateBooked.getFormattedDate());
		String bookingName = String.format("%17s%-28s%s%n", "","Name: ", firstName + " " + 
							 lastName);
		String bookingNumPassengers = String.format("%17s%-28s%s%n","", "Passengers: ", 
									  numPassengers);
		String bookingTravelled = String.format("%17s%-28s%.2f%n","", "Travelled: ", 
                				  kilometersTravelled);
		String bookingTripFee = String.format("%17s%-28s$%.2f%n","", "Trip Fee: ", 
				                tripFee);
		String carId = String.format("%17s%-28s%s%n","", "Car Id: ", car.getRegistrationNumber());
		
		String bookingDetails = bookingId + bookingsFee + bookingPickUpDate + 
								bookingName + bookingNumPassengers + 
								bookingTravelled + bookingTripFee + carId;
		return bookingDetails;
	}
	
	public String toString()
	{
		return  id.toUpperCase() + ":" + bookingFee + ":" + 
				dateBooked.getEightDigitDate() + ":" + firstName + " " + lastName + ":" + 
				numPassengers + ":" + kilometersTravelled + ":" +  
				tripFee + ":" + car.getRegistrationNumber(); 
	}
	
	// Required getters
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public DateTime getBookingDate()
	{
		return dateBooked;
	}
	
	public String getID()
	{
		return id;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	private String getRecordMarker()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 40; i++)
		{
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/*
	 * ALGORITHM to generate an id from regNo, passenger name and the date of the booking.
	 * 
	 * BEGIN:
	 * 		IF length of first name or length of last name is less than 3 or date is empty 
	 * 			THEN ASSIGN the id variable to invalid
	 * 		ELSE
	 * 			ASSIGN id variable to the regNo, first three characters of the first name 
	 * 					and last name, and formatted date 
	 * 		END IF
	 * END. 
	 */
	private void generateId(String regNo, String firstName, String lastName, DateTime date)
	{
		if(firstName.length() < 3 || lastName.length() < 3  || date == null)
		{
			id = "Invalid";
		}
		else
		{
			id = regNo + firstName.substring(0, 3).toUpperCase() + lastName.substring(0, 3).toUpperCase()
				+ date.getEightDigitDate();
		}
	}
	
	/*
	 * Ensures the name is more than three characters
	 */
	private void validateName(String firstName, String lastName)
	{
		if(firstName.length() >= NAME_MINIMUM_LENGTH && lastName.length() >= NAME_MINIMUM_LENGTH)
		{
			this.firstName = firstName;
			this.lastName = lastName;
		}
		else {
			firstName = "Invalid";
			lastName = "Invalid";
		}
	}
	
	/*
	 * Ensures the date is not in the past.
	 */
	private void validateAndSetDate(DateTime date)
	{
		if(DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan7Days(date))
		{
			dateBooked = date;
		}
		else
		{
			dateBooked = null;
		}
	}
}
