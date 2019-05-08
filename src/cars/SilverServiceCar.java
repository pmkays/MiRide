package cars;

import utilities.DateTime;

import utilities.DateUtilities;

/*
 * Class:		SilverServiceCar
 * Description:	This class (child class) extends the Car class (parent class) 
 * and has extra attributes when compared to the car class.
 * Author:		Paula Kurniawan
 */

public class SilverServiceCar extends Car
{
	private String[] refreshments;
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
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers)
	{
		boolean booked = false;
		if (dateIsValid3Days(required))
		{
			super.book(firstName, lastName, required, numPassengers);
			booked = true;
		}
		return booked;
	}
	
	@Override
	public String getDetails()
	{
		String refreshmentsString = String.format("%-13s%s%n", "Refreshments: ", refreshmentsDisplay());
		String currentBookingsString = String.format("%-13s%s%n", "Current Bookings: ", currentBookingsDisplay());
		String pastBookingsString = String.format("%-13s%s%n", "Past Bookings: ", pastBookingsDisplay());
		return super.getDetails() + refreshmentsString + currentBookingsString + pastBookingsString;
	
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
				str += String.format("%n%s", "Item " + 
						count + "      " + refreshments[i]);
			}
			else
			{
				str += String.format("%n", "Refreshments unavailable");
			}
		}
		
		return str + "\n____________________________________";
	}
		

	private String currentBookingsDisplay()
	{
		String str="";
		for(int i = 0; i < getCurrentBooking().length; i++)
		{
			if(getSpecificCurrentBooking(i)!=null)
			{
				str=(String.format("%n%s", getSpecificCurrentBooking(i).getDetails()));
			}
		}
		return str + "\n____________________________________";
		
	}
	
	private String pastBookingsDisplay()
	{
		String str = "";
		for(int i = 0; i < getCurrentBooking().length; i++)
		{
			if(getSpecificPastBooking(i)!=null)
			{
				str=(String.format("%n%s", getSpecificPastBooking(i).getDetails()));
			}
		}
		return  str + "\n____________________________________";
	}
	
	@Override
	public String toString()
	{
		//fix toString. 
		return super.toString() + ":" + refreshmentsDisplay().toString() + ":" + getCurrentBooking().toString() + ":" + getPastBooking().toString();
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
}
