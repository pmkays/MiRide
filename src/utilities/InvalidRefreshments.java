package utilities;
/* Class:		InvalidRefreshments
* Description:	This class (child class) extends the in-built Java Exception class (parent class) 
* 				to throw custom exceptions for invalid refreshment rules. 
* Author:		Paula Kurniawan [s3782041]
*/
public class InvalidRefreshments extends Exception
{
	public InvalidRefreshments(String message)
	{
		super(message);
	}

}
