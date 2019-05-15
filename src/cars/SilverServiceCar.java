package cars;

import utilities.DateTime;

import utilities.DateUtilities;
import utilities.InvalidBooking;
import utilities.InvalidRefreshments;

/*
 * Class:		SilverServiceCar
 * Description:	This class (child class) extends the Car class (parent class) 
 * and has extra attributes when compared to the car class.
 * Author:		Paula Kurniawan
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
	
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking
	{
//		boolean booked = false;
//		if (!dateIsValid3Days(required))
//		{
//			super.book(firstName, lastName, required, numPassengers);
//			booked = true;
//		}
//		return booked;
		
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
		String refreshmentsString = String.format("%n%-13s%25s%n", "REFRESHMENTS AVAILABLE: ", refreshmentsDisplay());
		String getDetails = super.carDetails() + refreshmentsString + super.currentBookingsDisplay() + super.pastBookingsDisplay();	
		return getDetails;
	}
	
	private String refreshmentsDisplay()
	{
		String str = "";
		int count = 0;
		for(int i = 0; i < refreshments.length; i++)
		{
			if(refreshments[i]!=null)
			{
				count++;
				str += String.format("%n%-17s%s", "Item " + 
						count, refreshments[i]);
			}
			else
			{
				str += String.format("%n", "Refreshments unavailable");
			}
		}
		
		return str + "\n__________________________________________________________________";
	}
		
	@Override
	public String toString()
	{
		return super.carToString() + refreshmentsToString() + super.currentBookingsToString() + super.pastBookingsToString();
	}
	
	private String refreshmentsToString()
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
	
	
//	@Override
//	public String getDetails()
//	{
//		StringBuilder StringBuilder = new StringBuilder();
//		int count = 1;
//		
//		super.getDetails(); 
//		
//		StringBuilder.append(getGetRecordMarker());
//		
//		StringBuilder.append(String.format("%n", "Refreshments available"));
//		for(int i = 0; i < refreshments.length; i++)
//		{
//			if(refreshments[i]!=null)
//			{
//				StringBuilder.append(String.format("-15s %s%n", "Item " + 
//						count + "      " + refreshments[i]));
//				count++;
//			}
//			else
//			{
//				StringBuilder.append(String.format("%n", "Refreshments unavailable"));
//			}
//		}
//		
//		StringBuilder.append(String.format("%n", "CURRENT BOOKINGS:"));
//		StringBuilder.append(getGetRecordMarker());
//		
//		for(int i = 0; i < getCurrentBooking().length; i++)
//		{
//			if(getSpecificCurrentBooking(i)!=null)
//			{
//				StringBuilder.append(String.format("%n", getSpecificCurrentBooking(i).getDetails()));
//				StringBuilder.append(getGetRecordMarker());
//			}
//			else
//			{
//				StringBuilder.append(String.format("%n", "There are no current bookings"));
//			}
//		}
//		
//		StringBuilder.append(String.format("%n", "PAST BOOKINGS:"));
//		for(int i = 0; i < getPastBooking().length; i++)
//		{
//			if(getSpecificPastBooking(i)!=null)
//			{
//				StringBuilder.append(String.format("%n", getSpecificPastBooking(i).getDetails()));
//				StringBuilder.append(getGetRecordMarker());
//			}
//			else
//			{
//				StringBuilder.append(String.format("%n", "There are no past bookings"));
//			}
//		}
//		return StringBuilder.toString();
//	}
//	
//	@Override
//	public String toString()
//	{
//		StringBuilder StringBuilder = new StringBuilder();
//		super.toString();
//		for(int i = 0; i < getCurrentBooking().length; i++)
//		{
//			if(getSpecificCurrentBooking(i)!=null)
//			{
//				StringBuilder.append(getSpecificCurrentBooking(i).toString());
//			}
//			else
//			{
//				return null;
//			}
//		}
//		
//		for(int i = 0; i < getPastBooking().length; i++)
//		{
//			if(getSpecificPastBooking(i)!=null)
//			{
//				StringBuilder.append(getSpecificPastBooking(i).toString());
//			}
//			else
//			{
//				return null;
//			}
//		}
//		return StringBuilder.toString();
//	}
//	
//	
	private boolean dateIsValid3Days(DateTime date)
	{
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan3Days(date);
	}
	

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
	
//	public boolean checkRefreshmentsArray(String[] refreshments) throws InvalidRefreshments
//	{
//		boolean empty = false;
//		for (int i =0; i<refreshments.length; i++)
//		{
//			if(refreshments[i]!=null)
//			{
//				if(i ==2)
//				{
//					empty = true;
//					throw new InvalidRefreshments("There must be at least three refreshments entered");
//				}
//			}
//		}
//		return empty;
//	}
	public void checkRefreshmentsArray(String[] refreshments) throws InvalidRefreshments
	{
		final int MINIMUM_AMOUNT = 3;
		if(refreshments.length < MINIMUM_AMOUNT)
		{
			throw new InvalidRefreshments("There must be at least three refreshments entered. Please try again.");
		}	
	}
	
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
