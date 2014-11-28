package backend;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import program.Information;


/**
 * @author Joseph Gefroh
 */
public class RecordController
{
	private final String outputPathSerializable = "./records.noid";		//Complete file path to the serialized output.
	private final String outputPathCSV 			= "./csv/allrecords.csv"; //Complete file path to the CSV (output).
	private final String inputPathSerializable 	= "./records.noid";	//Complete file path to the serialized input.
	private final String inputPathCSV 			= "./csv/allrecords.csv";	//COmplete fiel path to the CSV (input).
	private final int msg = Information.DBGMSG;	//Debug message type (Message).
	private final int err = Information.DBGERR;	//Debug message type (Error).
	private final int inf = Information.DBGINF;	//Debug message type (Information).
	
	private ArrayList<StudentRecord> recordList = new ArrayList<StudentRecord>();
	private int debugMode = Information.debugMode;	//Turn debug messages on or off (0 off, 1on)
	
	/**
	 * Saves the in-memory array of StudentRecord objects to the location marked by <code>outputPathSerializable</code>.
	 */
	public void commitAsSerialized()
	{//Writes all the files currently in memory to the destination specified in outputPath.
		FileOutputStream 	fileOutStream 	= null;
		ObjectOutputStream 	objOutStream 	= null;
		try
		{
			fileOutStream = new FileOutputStream(outputPathSerializable);
			objOutStream = new ObjectOutputStream(fileOutStream);
			
			StudentRecord[] recordArray = new StudentRecord[recordList.size()];	//Create an array for transfer purposes.
			recordList.toArray(recordArray);	//Store a copy of the ArrayList in the array.
			for(int index=0;index<recordArray.length;index++)	//Write every StudentRecord object to the destination.
			{															//FOR: For all records in the array...
				objOutStream.writeObject(recordArray[index]);			//FOR: Write to the specified location.
			}
			objOutStream.close();
			fileOutStream.close();
		}
		catch(EOFException e)
		{//Thrown if the end of file has been reached.
			DEBUG("EOFException | Failed - Unable to finish writing data: " + e.getMessage(),inf);
		}
		catch(IOException e)
		{
			DEBUG("IOException | Error writing data: " + e.getMessage(),err);
		}
		catch(NullPointerException e)
		{
			DEBUG("NullPointerException | Error writing data: " + e.getMessage(),err);
		}
		finally
		{//Close the input streams (try-catch block is here due to syntax issues).
			try
			{
				objOutStream.close();
				fileOutStream.close();
			}
			catch(IOException e)
			{
				DEBUG("IOException | Unable to close output streams: " + e.getMessage(),err);
			}
			catch(NullPointerException e)
			{
				DEBUG("NullPointerException | Location unavailable: " + e.getMessage(),err);
			}
		}
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Read the serialized StudentRecord file into an in-memory ArrayList.
	 */
	public void prepareRecordArray()
	{
		FileInputStream fileInStream	= null;					
		ObjectInputStream objInStream	= null;
		try
		{
			fileInStream = new FileInputStream(inputPathSerializable);
			objInStream = new ObjectInputStream(fileInStream);
	
			StudentRecord currentObject = null;
			while((currentObject = (StudentRecord)objInStream.readObject())!=null)
			{//While there are still objects to read.
				recordList.add(currentObject);	//Add the object to the memory array.
			}
		}
		catch(EOFException e)
		{
			DEBUG("EOFException | [SUCCESS] Finished reading data: " + e.getMessage(),inf);
		}
		catch(IOException e)
		{
			DEBUG("IOException | Error reading data: " + e.getMessage(),err);
		}
		catch(ClassNotFoundException e)
		{
			DEBUG("ClassNotFoundException | Error reading data: " + e.getMessage(),err);
		}
		catch(NullPointerException e)
		{
			DEBUG("NullPointerException | Error reading data: " + e.getMessage(),err);
		}
		finally
		{//Close the input streams (try-catch block is here due to syntax issues).
			try
			{
				objInStream.close();
				fileInStream.close();
			}
			catch(IOException e)
			{
				DEBUG("IOException | Unable to close input streams: " + e.getMessage(),err);
			}
			catch(NullPointerException e)
			{
				DEBUG("NullPointerException | Unable to find the file: " + e.getMessage(),err);
			}
			
		}
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Delete a record from the in-memory array of StudentRecord objects if the passed ID is valid.
	 * 
	 * @param id The studentID of the record to delete..
	 * @return <code>true</code> if a record was successfully deleted, <code>false</code> otherwise.
	 */
	public boolean deleteRecord(final String id)
	{
		if(validateID(id)==true)
		{//Check to see the ID is valid...
			for(StudentRecord currentRecord : recordList)
			{//If it is valid, search the array.
				if(currentRecord.getID().equals(id))
				{//If a match is found, delete.
					recordList.remove(currentRecord);
					return true;
				}
			}
		}
		DEBUG("Unable to delete record with ID " + id, msg);
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Create a new <code>StudentRecord</code> and store it in the in-memory array.
	 * This will create, store, and return a new <code>StudentRecord</code> object provided the
	 * ID is a valid number and is not currently being used by a record already in-memory.
	 * The number of entries is automatically synchronized with the number of valid dates.
	 * 
	 * @param id 		The id of the student without the <code>@</code> sign.
	 * @param lastName 	The last name of the student.
	 * @param firstName The first name of the student.
	 * @param dates		The CSV formatted dates the student has entered in numeric format (MMDDYYYY).
	 * @return the created <code>StudentRecord</code> object if created successfully, <code>null</code>
	 * if record with <code>id</code> already exists.
	 */
	public StudentRecord createRecordInclude(final String id, final String lastName, final String firstName, 
												final String dates)
	{
		StudentRecord newRecord = null;
		if(validateID(id)==true&&findRecordWithID(id)==null)
		{//If the ID is valid (numeric and non-duplicate)...
			newRecord = new StudentRecord(id, lastName, firstName, 0, dates);	//Set entries to 0.
			recordList.add(newRecord);											//Add new record to in-memory ArrayList.
			updateRecordEqualizeEntriesWithDates(id);							//Get # entries via # dates.
			return newRecord;
		}
		else
		{//else if the ID is invalid...
			DEBUG("Error creating record. Record with ID " + id + " already exists.", err);
			return null;
		}
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Create a new <code>StudentRecord</code> and store it in the in-memory array.
	 * This will process a <code>String[]</code> of dates and use the normal <code>createRecordInclude()</code>
	 * method to validate and create a <code>StudentRecord</code> object.
	 * 
	 * @param id 		The id of the student without the <code>@</code> sign.
	 * @param lastName 	The last name of the student.
	 * @param firstName The first name of the student.
	 * @param dates		The <code>String[]</code> of dates the student has entered in numeric format (MMDDYYYY).
	 * @return the created <code>StudentRecord</code> object if created successfully, <code>null</code>
	 * if record with <code>id</code> already exists.
	 */
	public StudentRecord createRecordInclude(final String id, final String lastName, final String firstName, 
			final String[] dates)
	{
		String strDates = null;
		StudentRecord newRecord = null;
		if(dates!=null&&dates.length>0)
		{//If the array has content...
			String[] scrubbedDates = validateDateArray(dates);		//Validate date
			strDates = buildDateString(scrubbedDates);				//Create CSV formatted date String
			newRecord = createRecordInclude(id, lastName, firstName, strDates);		//Call other recordCreator.	
		}
		else
		{
			newRecord = createRecordInclude(id, lastName, firstName, "");		//Call other recordCreator.
		}
		return newRecord;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Updates the record associated with the passed ID and changes the dates attached to it.
	 * The dates will be validated and the ID will be checked.
	 * 
	 * @param date the CSV formatted date.
	 * @param id the studentID of the record to edit.
	 */
	public boolean updateRecordDates(final String dates, final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);	//Check to make sure the record exists.
		String newDateStr = null;
		if(recordToEdit!=null&&dates!=null)
		{//If the record exists
			newDateStr = validateDateString(dates);			//Scrub the dates and validate them.
			recordToEdit.setDates(newDateStr);	
			return true;
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Updates the record associated with the passed ID and changes the first name associated with it.
	 * 
	 * @param firstName the first name of the student.
	 * @param id the studentID of the record to edit.
	 * @return <code>true</code> if successfully edited, <code>false</code> otherwise.
	 */
	public boolean updateRecordFirstName(final String firstName, final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);
		if(recordToEdit!=null)
		{
			recordToEdit.setFirstName(firstName);
			return true;
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Updates the record associated with the passed ID and changes the last name associated with it.
	 * 
	 * @param lastName the last name of the student.
	 * @param id the studentID of the record to edit.
	 * @return <code>true</code> if successfully edited, <code>false</code> otherwise.
	 */
	public boolean updateRecordLastName(final String lastName, final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);
		if(recordToEdit!=null)
		{
			recordToEdit.setLastName(lastName);
			return true;
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Updates the record associated with the passed ID and changes the ID associated with it if valid.
	 * This method checks to see that the ID is in valid numeric format and also that the new ID is unused.
	 * 
	 * @param firstName the first name of the student.
	 * @param id the studentID of the record to edit.
	 * @return <code>true</code> if successfully edited, <code>false</code> otherwise.
	 */
	public boolean updateRecordID(final String newID, final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);
		if(recordToEdit!=null 
				&& validateID(newID)==true
				&& findRecordWithID(newID)==null)	//if the old ID exists and the new ID is valid..
		{
			recordToEdit.setID(newID);
			return true;
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Updates the record associated with the passed ID and equalizes the # of entries to the # of dates.
	 * 
	 * @param id the studentID of the record to edit.
	 * @return <code>true</code> if successfully edited, <code>false</code> otherwise.
	 */
	public boolean updateRecordEntriesTotal(final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);
		if(recordToEdit!=null)
		{
			updateRecordEqualizeEntriesWithDates(id);
			return true;
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * This will make the entriesTotal value of the appropriate StudentRecord object
	 * match the number of valid dates the object has.
	 * 
	 * @return <code>true</code> if successfully edited, <code>false</code> otherwise.
	 */
	public boolean updateRecordEqualizeEntriesWithDates(final String id)
	{
		StudentRecord recordToEdit = findRecordWithID(id);
		String[] numberOfDates = null;
		if(recordToEdit!=null)
		{
			numberOfDates = splitString(recordToEdit.getDates());
			if(numberOfDates!=null)
			{//if there are dates...
				recordToEdit.setEntriesTotal(numberOfDates.length);
				return true;
			}
			else
			{//if invalid...
				recordToEdit.setEntriesTotal(0);
				return true;
			}
		}
		return false;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Search the in-memory array of StudentRecord objects for a record that matches the id parameter.
	 * 
	 * @param id The ID used to locate the correct <code>StudentRecord</code> object.
	 * @return the found <code>StudentRecord</code> if located, otherwise <code>null</code>.
	 */
	public StudentRecord findRecordWithID(final String id)
	{
		if(validateID(id)==true)
		{//If the ID is valid...
			for(StudentRecord compare:recordList)
			{//Search for record containing ID.
				if(compare.getID().equals(id))
				{//If a record with the same ID was found...
					return compare;
				}
			}
		}//Otherwise...
		return null;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Search the in-memory array of StudentRecord objects for a record that matches/starts with the last name.
	 * 
	 * @param lastName The last name to find.
	 * @return an <code>ArrayList of StudentRecord objects</code> with all the matches, <code>null</code> if 0 found.
	 */
	public ArrayList<StudentRecord> findRecordsWithLastName(final String lastName)
	{
		ArrayList<StudentRecord> matchingRecords = new ArrayList<StudentRecord>();	//Contains all exact matches.
		ArrayList<StudentRecord> closeRecords = new ArrayList<StudentRecord>();		//Contains "close enough" matches.
		for(StudentRecord current:recordList)
		{//For all the records currently in-memory...
			if(current.getLastName().equalsIgnoreCase(lastName))
			{//Check to see if the record's last name equals the search name.
				matchingRecords.add(current);	//If so, add to the list of matches.
			}
			else if(current.getLastName().toUpperCase().startsWith(lastName.toUpperCase()))
			{//Check to see if the record's last name starts with the search name.
				closeRecords.add(current);
			}
		}
		matchingRecords.addAll(closeRecords);	//Merge the two lists together.
		if(matchingRecords.size()>0)
		{//If there were any matches...
			Collections.sort(matchingRecords, new LastNameComparator());	//Sort in order of last name, then by ID.
			return matchingRecords;
		}
		return null;
	}//////////////////////////////////////////////////////////////////////////
	
	/**
	 * Search the in-memory array of StudentRecord objects for a record that matches the first name.
	 * 
	 * @param lastName The last name to find.
	 * @return an <code>ArrayList of StudentRecord objects</code> with all the matches, <code>null</code> if 0 found.
	 */
	public ArrayList<StudentRecord> findRecordsWithFirstName(final String firstName)
	{//Doesn't work on numbers.
		ArrayList<StudentRecord> matchingRecords = new ArrayList<StudentRecord>();
		ArrayList<StudentRecord> closeRecords = new ArrayList<StudentRecord>();
		for(StudentRecord current:recordList)
		{//For all the records currently in-memory...
			if(current.getFirstName().equalsIgnoreCase(firstName)==true)
			{//Check to see if the record's first name equals the search name.
				matchingRecords.add(current);	//IF so, add to a list.
			}
			else if(current.getFirstName().toUpperCase().startsWith(firstName.toUpperCase()))
			{//Check to see if any names are close enough.
				closeRecords.add(current);
			}
		}
		matchingRecords.addAll(closeRecords);
		if(matchingRecords.size()>0)
		{//If there were any matches...
			Collections.sort(matchingRecords, new FirstNameComparator());
			return matchingRecords;
		}
		return null;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Search the in-memory array of StudentRecord objects for a record that matches the range of entries.
	 * Valid ranges consist of:
	 * 		x,y
	 * 		x
	 * 		x+
	 * 		x-
	 * 
	 * @param lastName The last name to find.
	 * @return an <code>ArrayList of StudentRecord objects</code> with all the matches, <code>null</code> if 0 found.
	 */
	public ArrayList<StudentRecord> findRecordsWithTotalEntriesBetweenInc(final String inc)
	{//Currently 
		String[] limits = inc.split(",");
		ArrayList<StudentRecord> matchingRecords = null;
		int upperLimit = 0;
		int lowerLimit = 0;
		try
		{
			if(limits!=null && limits.length==2)
			{//Passed range: x,y
				lowerLimit = Integer.parseInt(limits[0]);
				upperLimit = Integer.parseInt(limits[1]);
			}
			else if(limits!=null&&limits.length==1&&limits[0].contains("+")==true)
			{//Passed range is: >=x
				limits[0] = limits[0].replace("+", "");
				lowerLimit = Integer.parseInt(limits[0]);
				upperLimit = Integer.MAX_VALUE;
			}
			else if(limits!=null&&limits.length==1&&limits[0].contains("-")==true)
			{//Passed range is: <=x
				limits[0] = limits[0].replace("-", "");
				lowerLimit = 0;
				upperLimit = Integer.parseInt(limits[0]);
			}
			else if(limits!=null&&limits.length==1)
			{//Passed range is: x,x
				lowerLimit = Integer.parseInt(limits[0]);
				upperLimit = lowerLimit;
			}
			else
			{
				return null;
			}
		}
		catch(NumberFormatException e)
		{
			return null;
		}
		matchingRecords = new ArrayList<StudentRecord>();
		for(StudentRecord current:recordList)
		{//For all the records currently in-memory...
			if(current.getEntriesTotal()>=lowerLimit&&current.getEntriesTotal()<=upperLimit)
			{//Check to see if the record has the proper amount of entries...
				matchingRecords.add(current);	//IF so, add to a list.
			}
		}
		if(matchingRecords.size()>0)
		{//If there were any matches...
			Collections.sort(matchingRecords, new EntriesComparator());
			return matchingRecords;
		}
		return null;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Builds a CSV-string of dates out of an array of dates without validating.
	 * 
	 * @param dates
	 * @return properly formatted date string
	 */
	public String buildDateString(final String[] dates)
	{
		System.out.println("buildDateString: Dates length: " + dates.length);
		String dateString = null;
		boolean firstDate = true;
		if(dates!=null)
		{//If there are dates to build
			for(int index=0;index<dates.length;index++)
			{//For all the dates in the dateArray, add the date to the string.
				if(firstDate == false)
				{//FORMAT: If it is not the first date...
					dateString += ","+dates[index];	
				}
				else
				{//FORMAT: If it is the first date...
					dateString = dates[index];
					firstDate = false;
				}
			}
			return dateString;
		}
		return null;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Splits the string using a "," as the delimiter.
	 * 
	 * @param dateString
	 * @return
	 */
	public String[] splitString(final String str)
	{
		try
		{
			String[] strSplit = null;		//Secondary storage area of the processed dateString pieces.
			strSplit = str.split(",");
			return strSplit;
		}
		catch (NullPointerException e)
		{
			DEBUG("NullPointerException | Error splitting string: " + e.getMessage(), err);
			return null;
		}
		
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Will make sure that the passed String is a valid date (long, not null, not empty)
	 * @param date (String)Date to validate.
	 * @return <code>true</code> if the passed String is valid, <code>false</code> otherwise.
	 */
	private boolean validateDate(final String date)
	{
		try
		{//Try to see if it is a Long.
			Long.parseLong(date);
			return true;
		}
		catch(NumberFormatException e)
		{//Not a long.
			DEBUG("NumberFormatException | Invalid content " + date
			         + " found in date string: " + e.getMessage(),err);
			return false;
		}
		catch(NullPointerException e)
		{//is null.
			DEBUG("NullPointerException | Null value found in date string: " + e.getMessage(),err);
			return false;
		}
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Goes through a date string and validates it using existing methods.
	 * 
	 * @param dateString the CSV formatted string containing the dates
	 * @return a validated string (scrubbed of invalid dates), or null if no valid dates
	 */
	private String validateDateString(final String dateString)
	{
		String[] splitDates = splitString(dateString);
		String[] scrubbedArray = validateDateArray(splitDates);
		String returnString = buildDateString(scrubbedArray);
		return returnString;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Goes through a String[] of dates and removes all the invalid date objects.
	 * @param dateArray
	 * @return a validated string array (scrubbed of invalid dates), or null if no valid dates
	 */
	public String[] validateDateArray(final String[] dateArray)
	{
		int validCount = 0;	//Count the number of valid
		//Check to see if there is an actual dateArray object.
		if(dateArray==null)
		{
			return null;
		}
		//If it exists, go through the array and validate each date individually.
		
		for(int index=0;index<dateArray.length;index++)
		{
			if(validateDate(dateArray[index])==true)
			{
				validCount++;
			}
			else
			{
				dateArray[index]=null;
			}
		}
		//Check to see if there is at least 1 good date.
		if(validCount<=0)
		{
			return null;
		}
		//If there is at least one good date object, go through the original array and transfer good to new array.
		String[] scrubbedArray = new String[validCount];
		validCount = 0;
		for(int index=0;index<dateArray.length;index++)
		{
			if(dateArray[index]!=null)
			{
				scrubbedArray[validCount] = dateArray[index];
				validCount++;
			}
		}
		return scrubbedArray;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Check to see that the passed string contains a valid numeric ID.
	 * @param id the ID to test
	 * @return true if valid, false otherwise.
	 */
	private boolean validateID(final String id)
	{//Check to see that the ID is a valid ID.
		try
		{
			Integer.parseInt(id);
			return true;
		}
		catch(NumberFormatException e)
		{
			DEBUG("NumberFormatException | Invalid ID: " + e.getMessage(),err);
			return false;
		}
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Get the input path that is used when the in-memory record is being populated by a CSV file.
	 * 
	 * @return the input path of the file.
	 */
	public String getInputPathCSV()
	{
		return inputPathCSV;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Get the output path that is used when the in-memory record list is being saved as a CSV file.
	 * 
	 * @return the output path of the file.
	 */
	public String getOutputPathCSV()
	{
		return outputPathCSV;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Get the input path that is used when the in-memory record list is being populated by serialized objects.
	 * 
	 * @return the input path of the serialized file.
	 */
	public String getInputPathSerializable()
	{	
		return inputPathSerializable;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Get the output path that is used when the in-memory record list is being saved as a serialized file.
	 * 
	 * @return the output path of the file.
	 */
	public String getOutputPathSerializable()
	{	
		return outputPathSerializable;
	}//////////////////////////////////////////////////////////////////////////
	/**
	 * Display a message if DEBUG mode is turned on.
	 * 
	 * @param DBGMSG the message to display.
	 * @param type the type of message this is (0 = normal message, 1 = error message, 2 =  informative message)
	 */
	private void DEBUG(final String DBGMSG, final int type)
	{
		if(debugMode>0)
		{
			switch(type)
			{
				case 0:
					System.out.println("DBG | MSG: " + DBGMSG);
					break;
				case 1:
					System.err.println("DBG | ERR: " + DBGMSG);
					break;
				case 2:
					System.out.println("DBG | INF: " + DBGMSG);
			}
		}
	}//////////////////////////////////////////////////////////////////////////
}
//Sort ascending order by studentID.
class StudentIDComparator implements Comparator<StudentRecord>
{//-, 0, + <first arg <, =, > 2nd>
	public int compare(StudentRecord record1, StudentRecord record2)
	{
		int id1=0;
		int id2=0;
		try
		{
			id1 = Integer.parseInt(record1.getID());
			id2 = Integer.parseInt(record2.getID());
		}
		catch(NumberFormatException e)
		{
		}
		return id1-id2;
	}
}
class LastNameComparator implements Comparator<StudentRecord>
{
	public int compare(StudentRecord record1, StudentRecord record2)
	{
		int result = record1.getLastName().compareToIgnoreCase(record2.getLastName());
		int id1=0;
		int id2=0;
		if(result!=0)
		{
			return result;
		}
		else
		{
			try
			{//then by studentID
				id1 = Integer.parseInt(record1.getID());
				id2 = Integer.parseInt(record2.getID());
			}
			catch(NumberFormatException e)
			{
			}
			return id1-id2;
		}
	}
}
class FirstNameComparator implements Comparator<StudentRecord>
{
	public int compare(StudentRecord record1, StudentRecord record2)
	{
		int result = record1.getFirstName().compareToIgnoreCase(record2.getFirstName());
		int id1=0;
		int id2=0;
		if(result!=0)
		{
			return result;
		}
		else
		{
			try
			{//then by studentID
				id1 = Integer.parseInt(record1.getID());
				id2 = Integer.parseInt(record2.getID());
			}
			catch(NumberFormatException e)
			{
			}
			return id1-id2;
		}
	}
}
class EntriesComparator implements Comparator<StudentRecord>
{
	public int compare(StudentRecord record1, StudentRecord record2)
	{
		int entries1=0;
		int entries2=0;
		int id1=0;
		int id2=0;
		entries1 = record1.getEntriesTotal();
		entries2 = record2.getEntriesTotal();
		if(entries1!=entries2)
		{
			return entries1-entries2;
		}
		else
		{
			try
			{//then by studentID
				id1 = Integer.parseInt(record1.getID());
				id2 = Integer.parseInt(record2.getID());
			}
			catch(NumberFormatException e)
			{
			}
			return id1-id2;
		}
	}
}
