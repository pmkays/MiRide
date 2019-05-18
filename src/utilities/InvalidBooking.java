package utilities;
/*
 * Class:		InvalidBooking
 * Description:	This class (child class) extends the in-built Java Exception class (parent class) 
 * 				to throw custom exceptions for invalid booking rules. 
 * Author:		Paula Kurniawan [s3782041]
 */
public class InvalidBooking extends Exception
{	
	public InvalidBooking(String message)
	{
		super(message);
	}
}
