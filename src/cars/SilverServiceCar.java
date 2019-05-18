package cars;

import utilities.DateTime;

import utilities.DateUtilities;
import utilities.InvalidBooking;
import utilities.InvalidRefreshments;

/*
 * Class:		SilverServiceCar
 * Description:	This class (child class) extends the Car class (parent class) 
 * 				and has extra attributes.
 * Author:		Paula Kurniawan [s3782041]
 */

public class SilverServiceCar extends Car
{
	private String[] refreshments = new String[10];
	private double bookingFee;
	
	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity, String[] refreshments, double bookingFee)
	{
		super(regNo, make, model, driverName, passengerCapacity);
		this.refreshments = refreshments;
		this.bookingFee = bookingFee;
		
		super.setBookingFee(bookingFee);
	}
	
	//adds refreshments to refreshment array;
	public void addRefreshments(String refreshment)
	{
		for (int i = 0; i<refreshments.length ; i++)
		{
			if(refreshments[i]!=null)
			{
				refreshments[i] = refreshment;
			}
		}
	}
	
	
	/*
	 * ALGORITHM to override the Car class book method
	 * 
	 * BEGIN:
	 * 		ASSIGN boolean variable to true
	 * 		IF date is greater than three days
	 * 			THEN throw new InvalidBooking exception
	 * 			ASSIGN boolean variable to false
	 * 		END IF
	 * 
	 * 		CALL book method of car class
	 * 		RETURN boolean value (which will be used as a check to see 
	 * 				if booking has been completed)
	 * END.
	 */
	
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking
	{	
		boolean booked = true;
		if (!dateIsValid3Days(required))
		{
			booked = false;
			throw new InvalidBooking("The date is invalid.");
		}
		
		super.book(firstName, lastName, required, numPassengers);
		return booked;
	}
	
	@Override
	public String getDetails()
	{
		String bookingFee = String.format("%-17s%s%n", "Standard Fee:", this.bookingFee);
		String refreshmentsString = String.format("%n%-13s%25s%n", "REFRESHMENTS AVAILABLE: ", refreshmentsDisplay());
		String getDetails = super.carDetails() + bookingFee + refreshmentsString + super.currentBookingsDisplay() + super.pastBookingsDisplay();	
		return getDetails;
	}
	
	//returns formatted refreshments for getDetails
	private String refreshmentsDisplay()
	{
		String snacks = "";
		int count = 0;
		for(int i = 0; i < refreshments.length; i++)
		{
			if(refreshments[i]!=null)
			{
				count++;
				snacks += String.format("%n%-17s%s", "Item " + 
						count, refreshments[i]);
			}
			else
			{
				snacks += String.format("%n", "Refreshments unavailable");
			}
		}
		
		return snacks + "\n__________________________________________________________________";
	}
		
	@Override
	public String toString()
	{
		return super.carToString() + bookingFee + refreshmentsToString() + super.currentBookingsToString() + super.pastBookingsToString();
	}
	
	@Override
	public String carToString()
	{
		String SScarPersistance = refreshmentsToString() + ":" + bookingFee;
		return super.carToString() + SScarPersistance; 
	}
	
	public String refreshmentsToString()
	{
		String[] refreshmentsToStringArray = new String[10];
		String refreshmentsFinal = "";
		int count =0;
		for(int i = 0; i<refreshments.length; i++)
		{
			if(refreshments[i]!=null)
			{
				count++;
				refreshmentsToStringArray[i] = ":Item " + count + " " + refreshments[i] +"";
				refreshmentsFinal += refreshmentsToStringArray[i] ;
			}
		} 
		return refreshmentsFinal;
	}
	
	//checks to see if date is valid, i.e. if greater than three days
	private boolean dateIsValid3Days(DateTime date)
	{
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan3Days(date);
	}
	
	
	/*
	 * ALGORITHM to validate the user input of standard booking fee
	 * 
	 * BEGIN: 
	 * 		ASSIGN a boolean variable to false
	 * 		IF booking Fee >= 3.0 
	 * 			THEN return boolean variable as true
	 * 		ELSE
	 * 			THEN return boolean variable
	 * 		END IF
	 * END.
	 * 			
	 */
	public boolean feeValidation(double bookingFee)
	{
		boolean checkBookingFee = false;
		if(bookingFee >= 3.0)
		{
			return checkBookingFee = true;
		}
		else
		{
			return checkBookingFee;
		}	
	}
	
	//checks to see if there are at least 3 refreshments that user has entered
	public void checkRefreshmentsArray(String[] refreshments) throws InvalidRefreshments
	{
		final int MINIMUM_AMOUNT = 3;
		if(refreshments.length < MINIMUM_AMOUNT)
		{
			throw new InvalidRefreshments("There must be at least three refreshments entered. Please try again.");
		}	
	}
	
	//checks to see if there are any duplicate refreshments in the array
	public void checkRefreshmentsDuplicate(String[] refreshments) throws InvalidRefreshments
	{
		for(int i = 0; i< refreshments.length; i++)
		{
			for (int j = i + 1; j< refreshments.length; j++)
			{
				if(refreshments[i] != null && refreshments[j] != null)
				{
					if (refreshments[i].equals(refreshments[j]))
					{

						throw new InvalidRefreshments("There are duplicate refreshments. Please try again.");
					}
				}
			}
		}
	}
	

	
}
