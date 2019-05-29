package exceptionhandling;

public class InvalidBooking extends Exception
{

	public InvalidBooking()
	{
		// TODO Auto-generated constructor stub
	}

	public InvalidBooking(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidBooking(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBooking(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBooking(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
