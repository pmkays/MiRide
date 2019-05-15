package main;

import app.Menu;
import utilities.InvalidBooking;
import utilities.InvalidRefreshments;

public class Driver {

	public static void main(String[] args) throws InvalidBooking, InvalidRefreshments 
	{
		Menu menu = new Menu();
		menu.run();
	}

}
