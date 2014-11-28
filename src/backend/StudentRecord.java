/*
 * Author Joseph Gefroh
 * Date 2012/01/15
 * Copyright Joseph Gefroh 2011-2012
 */
package backend;

import java.io.Serializable;

import program.Information;

public class StudentRecord implements Serializable
{
	private static final long 	serialVersionUID =	9001;
	
	private int 	entriesTotal	=	-1;		//The number of entries the student has made without an ID.
	private String	firstName		=	null;	//First name of the student.
	private String	id				=	null;	//ID Number of the student (without the @)
	private String 	lastName		=	null;	//Last name of the student.
	private String 	dates			=	null;	//CSV formatted dates.
	public StudentRecord()
	{
		
	}
	/**
	 * Create a StudentRecord object with the given data.
	 * 
	 * @param id 		(int)The studentID of the student.
	 * @param lastName 	(String)The last name of the student.
	 * @param firstName (String)The first name of the student.
	 * @param entriesTotal (int)Number of entries of user
	 * @param 
	 */
	public StudentRecord(final String id, final String lastName, final String firstName,
							final int entriesTotal, final String dates)
	{
		this.id 			= id;
		this.lastName 		= lastName;
		this.firstName 		= firstName;
		this.entriesTotal	= entriesTotal;
		this.dates 			= dates;
	}
	
	/*
	 * S		SETTERS			S
	 * S		SETTERS			S
	 * S		SETTERS			S
	 */
	public void setEntriesTotal(final int entriesTotal)
	{
		this.entriesTotal = entriesTotal;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public void setID(final String id)
	{
		this.id = id;
	}
	
	public void setDates(final String dates)
	{
		this.dates = dates;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	
	/*
	 * G		GETTERS			G
	 * G		GETTERS			G
	 * G		GETTERS			G
	 */
	public int getEntriesTotal()
	{
		return entriesTotal;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getID()
	{
		return id;
	}
	
	public String getDates()
	{
		return dates;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public long getSerialVersionUID()
	{
		return serialVersionUID;
	}
	
	
	/*
	 * Create a deep copy of this StudentRecord object and return it.
	 */
	public StudentRecord copy()
	{//Creates and returns a deep-copy of the StudentRecord object.
		StudentRecord newCopy = new StudentRecord();
		newCopy.setID(this.id);
		newCopy.setLastName(this.lastName);
		newCopy.setFirstName(this.firstName);
		newCopy.setEntriesTotal(this.entriesTotal);
		newCopy.setDates(this.dates);
		return newCopy;
	}
	
	public String toString()
	{//Converts the StudentRecord object into a CSV formatted String.
		String returnString = "";
		returnString = this.id 
			+ "," + this.firstName 
			+ "," + this.lastName 
			+ "," + this.entriesTotal;
		if(dates!=null)
		{
			returnString += "," + this.dates;
		}
		return returnString;
	}
	
	/**
	 * Test to see if the StudentRecord objects are equal.
	 * @param recordOne (StudentRecord)The first StudentRecord object used in the comparison.
	 * @param recordTwo (StudentRecord)The second StudentRecord object used in the comparison.
	 * 
	 * @return true if the record values are equal, false otherwise.
	 */
	public static boolean equals(final StudentRecord recordOne, final StudentRecord recordTwo)
	{
		if(recordOne.getID() == recordTwo.getID()
				&& recordOne.getLastName().equals(recordTwo.getLastName())
				&& recordOne.getFirstName().equals(recordTwo.getFirstName())
				&& recordOne.getEntriesTotal()==recordTwo.getEntriesTotal()
				&& recordOne.getDates().equals(recordTwo.getDates()))
		{
			
			return true;
		}
		return false;
	}
	
	public String[] toStringArray()
	{
		String[] result = new String[4];
		result[Information.FIELDID] = this.id;
		result[Information.FIELDLAST] = this.lastName;
		result[Information.FIELDFIRST] = this.firstName;
		result[Information.FIELDENTRIES] = this.entriesTotal+"";
		return result;
	}
	
	public String[] datesToStringArray()
	{
		String[] dateArray = null;
		if(dates!=null)
		{
			dateArray = dates.split(",");
		}
		return dateArray;

	}
	
}//Class CB
