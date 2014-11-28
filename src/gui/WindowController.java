package gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import program.Information;
import backend.RecordController;
import backend.StudentRecord;
@SuppressWarnings("unchecked")

public class WindowController
{//Perhaps WindowController is a bad name for it.
	private static AboutWindow about = new AboutWindow();
	private static CreateWindow create = new CreateWindow();
	private static ExamineWindow examine = new ExamineWindow();
	private static SearchWindow search = new SearchWindow();
	private static WindowController instance = null;
	private static RecordController rc = null;
	
	private static ArrayList<StudentRecord> lastFound = null;
	private WindowController()
	{
		rc = new RecordController();
		rc.prepareRecordArray();
	}
	/**
	 * Return THE instance of this <code>WindowController</code> or creates one if it does not exist.
	 * 
	 * @return 
	 */
	public static WindowController getInstance()
	{
		if(instance==null)
		{
			instance = new WindowController();
		}
		return instance;
	}
	public static CreateWindow getCreateWindow()
	{
		return create;
	}
	public static AboutWindow getAboutWindow()
	{
		return about;
	}
	public static SearchWindow getSearchWindow()
	{
		return search;
	}
	public static ExamineWindow getExamineWindow()
	{
		return examine;
	}
	/**
	 * Update the status bar of the search window with the passed message.
	 * @param status
	 */
	public static void updateStatus(final String status)
	{
		search.updateStatus(status);
	}/////////////////////////////////////////////////////////////////////
	/**
	 * Update the examine window with relevant information from the record.
	 */
	public static void examineSelected()
	{
		String value = null;
		if(isEmpty()==false)
		{//If a search was conducted...
			value = search.getSelectedRowID();	//Get the studentID at the row selected in the JTable.
			for(StudentRecord cur:lastFound)
			{//For all of the StudentRecord objects in the search...
				if(cur.getID().equals(value))
				{//If a match is found based on the StudentID...
					examine.updateFields(cur);				//Update the examine window's fields with that record.
					examine.setCurrentlyExamined(cur);		//Save that record for later retrieval.
					break;
				}
			}
		}
	}/////////////////////////////////////////////////////////////////////
	public static void deleteRecord()
	{
		String studentID = null;
		if(isEmpty()==false)
		{
			studentID = search.getSelectedRowID();
			rc.deleteRecord(studentID);
			searchFor();
		}
	}
	/**
	 * Obtain data from the create window and create a record.
	 */
	public static void createRecord()
	{
		String id 		= create.getID();
		String last 	= create.getLast();
		String first 	= create.getFirst();
		String[] dates 	= create.getDates();
		String dateStr = null;
		StudentRecord createdRecord = null;
		try
		{
			if(dates!=null)
			{
				for(int index=0;index<dates.length;index++)
				{
					dates[index] = deformat(dates[index]);
				}
			}
			createdRecord = rc.createRecordInclude(id, last, first, dates);
			if(createdRecord!=null)
			{
				if(dates!=null&&dates.length==0)		
				{//If the date is considered "valid" but is empty (all dates removed)
					rc.updateRecordDates("", createdRecord.getID());
				}
				else if(dates!=null)
				{//if the date is valid...
					dateStr = rc.buildDateString(dates);
					rc.updateRecordDates(dateStr, createdRecord.getID());
				}
				rc.updateRecordEqualizeEntriesWithDates(createdRecord.getID());
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Error creating record with ID: " + id);
			}
		}
		catch(NullPointerException e)
		{
			
		}

	}/////////////////////////////////////////////////////////////////////
	public static void saveChanges()
	{
		rc.commitAsSerialized();
	}
	/**
	 * Update a record with a specific ID (should be called via GUI)
	 */
	public static void editRecord()
	{
		String id 		= examine.getID();
		String last 	= examine.getLast();
		String first 	= examine.getFirst();
		String[] dates 	= examine.getDates();
		String dateStr = null;
		String msgUpdated = "The following fields have been updated:\n";
		String msgFailed = "The following fields were not updated:\n";
		int msgFailedLength = msgFailed.length();
		StudentRecord recordBeingEdited = examine.getCurrentlyExamined();	/*Get the examined record's ID*/
		String editID = recordBeingEdited.getID();
		recordBeingEdited = rc.findRecordWithID(editID);					/*Get the in-memory StudentRecord w/ID*/
		if(recordBeingEdited!=null)
		{/*Update the in-memory record with the new information.*/
			if(recordBeingEdited.getLastName().equals(last)==false)
			{/*Update the last name if it has been changed.*/
				rc.updateRecordLastName(last, recordBeingEdited.getID());
				msgUpdated+="Last Name\n";
			}
			if(recordBeingEdited.getFirstName().equals(first)==false)
			{/*Update the first name if it has been changed.*/
				rc.updateRecordFirstName(first, recordBeingEdited.getID());
				msgUpdated+="First Name\n";
			}
			if(dates!=null&&dates.length==0)					
			{//If the date is considered "valid" but is empty (all dates removed)
				rc.updateRecordDates("", recordBeingEdited.getID());
				msgUpdated+="Dates\n";
			}
			else if(dates!=null)
			{//if the date is valid...
				for(int index=0;index<dates.length;index++)
				{
					dates[index] = deformat(dates[index]);
				}
				dateStr = rc.buildDateString(dates);
				rc.updateRecordDates(dateStr, recordBeingEdited.getID());
				msgUpdated+="Dates\n";
			}
			if(recordBeingEdited.getID().equals(id)==false&&rc.findRecordWithID(id)==null)
			{/*Update the ID if it has been changed and if the new one is not being used.*/
				rc.updateRecordID(id, recordBeingEdited.getID());				//Update the ID.
				msgUpdated+="ID\n";
			}
			else if(recordBeingEdited.getID().equals(id)==false)
			{
				msgFailed+="ID (new ID " + id + " already in use)\n";
			}
			rc.updateRecordEqualizeEntriesWithDates(recordBeingEdited.getID());	/*Sync #entries to #dates*/
			msgUpdated+="Entries (Synched with dates)\n";
		}
		if(msgFailedLength!=msgFailed.length())
		{
			JOptionPane.showMessageDialog(null, msgUpdated + "\n" + msgFailed);
		}
		else
		{
			JOptionPane.showMessageDialog(null, msgUpdated);
		}
		//Step 2: Update the search to display the new results.
		runLastSearch();
	}
	public static String deformat(final String formatted)
	{
		String returnString = null;
		if(formatted!=null&&formatted.length()==10)
		{
			returnString = formatted.replace("-", "");
		}
		return returnString;
	}
	/**
	 * Add a row to a specific table.
	 * @param windowToAddTo
	 * @param data
	 */
	public static void addRowToTable(final JFrame windowToAddTo, final String[] data)
	{
		if(windowToAddTo==null)
		{
			System.out.println("Null window");
		}
		else if(windowToAddTo==search && search.isEnabled()==true)
		{
			search.addData(data);
			System.out.println("Null window2");
		}
		else if(windowToAddTo==create && create.isEnabled()==true)
		{
			System.out.println("Null window3");
		}
		else if(windowToAddTo==examine && examine.isEnabled()==true)
		{
			examine.addData(data);
			System.out.println("Null window4");
		}

	}/////////////////////////////////////////////////////////////////////
	public static void runLastSearch()
	{
		searchFor();
	}
	/**
	 * Search for a specific record based on ID, last, first, or entries total, and updates the GUI.
	 * @return
	 */
	public static boolean searchFor()
	{//Search for a specific record...
		String input = search.getInputText();		//Get the search input.
		int selection = search.getSelectedRB();		//Get the search criteria determined by the radio buttons.
		int numberOfMatches = 0;					//Holds the number of matches found.
		ArrayList<StudentRecord> lastSearchResults = null;	//Holds the search results of the last conducted search.
		String[] data = null;
		
		search.clearTable();		//Clear the JTable.
		search.updateStatus("Searching for matching records...");

		switch(selection)
		{//Switch based on the criteria...
			case Information.FIELDID:		//If searching by StudentID...
				StudentRecord result = rc.findRecordWithID(input);		//Find the record that contains the studentID.
				lastSearchResults = new ArrayList<StudentRecord>();
				if(result!=null)
				{//If a matching record was located...
					lastSearchResults.add(result);		//Add to last found matches...
					data = result.toStringArray();			
					if(data!=null&&data.length==4)
					{//If the StudentRecord is complete...
						search.addData(data);	//Add the data to the JTable.
						numberOfMatches++;		//Increment the number of matches.
					}
				}
				break;
			case Information.FIELDLAST:
				lastSearchResults = rc.findRecordsWithLastName(input);
				if(lastSearchResults!=null)
				{
					for(StudentRecord cur:lastSearchResults)
					{
						if(cur!=null)
						{
							data = cur.toStringArray();
						}
						if(data!=null&&data.length==4)
						{
							search.addData(data);
							numberOfMatches++;
						}
					}
				}
				break;
			case Information.FIELDFIRST:
				lastSearchResults = rc.findRecordsWithFirstName(input);
				if(lastSearchResults!=null)
				{
					for(StudentRecord cur:lastSearchResults)
					{
						if(cur!=null)
						{
							data = cur.toStringArray();
						}
						if(data!=null&&data.length==4)
						{
							search.addData(data);
							numberOfMatches++;
						}
					}
				}
				break;
			case Information.FIELDENTRIES:
				lastSearchResults = rc.findRecordsWithTotalEntriesBetweenInc(input);
				if(lastSearchResults!=null)
				{
					for(StudentRecord cur:lastSearchResults)
					{
						if(cur!=null)
						{
							data = cur.toStringArray();
						}
						if(data!=null&&data.length==4)
						{
							search.addData(data);
							numberOfMatches++;
						}
					}
				}
				break;
		}
		if(lastSearchResults!=null)
		{
			lastFound = (ArrayList<StudentRecord>) lastSearchResults.clone();
		}
		search.updateStatus("Matching records found: " + numberOfMatches);
		//set selected to default
		if(numberOfMatches<=0)
		{
			String[] noMatch = {"No matches found.", "N/A", "N/A", "N/A"};
			search.addData(noMatch);
			WindowController.getSearchWindow().setEnabledExamine(false);
			WindowController.getSearchWindow().setDefaultSelection();
			return false;
		}
		else
		{
			WindowController.getSearchWindow().setEnabledExamine(true);
			WindowController.getSearchWindow().setDefaultSelection();
			return true;
		}

	}/////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns whether there is a set of matches in memory.
	 * @return
	 */
	public static boolean isEmpty()
	{
		if(lastFound!=null)
		{
			return lastFound.isEmpty();
		}
		return true;
	}/////////////////////////////////////////////////////////////////////
	public static void changeColor(final JComponent target, final int type)
	{
		JTextField txt = new JTextField();
		if(type==0)
		{/*Basic editable textfield.*/
			txt.setEditable(true);
			target.setBackground(txt.getBackground());
			target.setBorder(txt.getBorder());
			
		}
		else if(type==1)
		{/*Basic uneditable textfield.*/
			txt.setEditable(false);
			target.setBackground(txt.getBackground());
			target.setBorder(txt.getBorder());
		}
		else if(type==2)
		{/*Error editable field.*/
			target.setBackground(Color.red);
		}
	}
}
